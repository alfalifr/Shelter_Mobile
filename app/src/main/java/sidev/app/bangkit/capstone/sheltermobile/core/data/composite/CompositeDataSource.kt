package sidev.app.bangkit.capstone.sheltermobile.core.data.composite

import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.lib.android.std.tool.util.`fun`.loge

abstract class CompositeDataSource<T> {
    protected open val shouldMapReturnedRemoteData: Boolean = false

    abstract suspend fun getLocalDataList(args: Map<String, Any?>): Result<List<T>>
    abstract suspend fun getRemoteDataList(args: Map<String, Any?>): Result<List<T>>
    open fun shouldFetch(localDataList: List<T>, args: Map<String, Any?>): Boolean = localDataList.isEmpty()
    open suspend fun mapNewReturnedData(remoteData: T, args: Map<String, Any?>): T = remoteData
    abstract suspend fun saveDataList(remoteDataList: List<T>): Result<Int>

    suspend fun getDataList(args: Map<String, Any?>): Result<List<T>> {
        val localRes = getLocalDataList(args).also { loge("CompositeSrc.getDataList() localRes = $it") }

        val local = if(localRes is Success) localRes.data else null
        //val shouldFetch = localRes is Success && shouldFetch(localRes.data, args) || localRes is Fail
        val shouldFetch = local == null || shouldFetch(local, args)

        when {
            shouldFetch -> {
                when(val remoteRes = getRemoteDataList(args).also { loge("CompositeSrc.getDataList() remoteRes = $it") }){
                    is Success -> {
                        val remote = remoteRes.data
                        when (val saveRes =
                            saveDataList(remote).also { loge("CompositeSrc.getDataList() saveRes = $it") }) {
                            is Success -> {
                                return when (saveRes.data) {
                                    remote.size -> {
                                        val returnedList = if(!shouldMapReturnedRemoteData) remote
                                        else remote.map { mapNewReturnedData(it, args) }
                                        Success(returnedList, 0)
                                    }
                                    0 -> Util.cantInsertFailResult()
                                    else -> Success(remote, 1)
                                }
                            }
                            is Fail -> return Fail(
                                "CompositeDataSource.getDataList() saveDataList()",
                                -1,
                                null
                            )
                        }
                    }
                    is Fail -> return Fail("CompositeDataSource.getDataList() from remote", -1, null)
                }
            }
            local != null -> {
                return Success(local, 0)
            }
            else -> {
                return Fail("CompositeDataSource.getDataList() from local", -1, null)
            }
        }
/*
        when(){
            is Success ->
            is Fail -> return Fail("CompositeDataSource.getDataList() from local", -1, null)
        }
 */
//        return Util.cantGetFailResult()
    }
}