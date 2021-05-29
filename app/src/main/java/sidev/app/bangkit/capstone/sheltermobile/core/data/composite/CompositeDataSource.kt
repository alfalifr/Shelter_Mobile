package sidev.app.bangkit.capstone.sheltermobile.core.data.composite

import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

abstract class CompositeDataSource<T> {
    abstract suspend fun getLocalDataList(args: Map<String, Any?>): Result<List<T>>
    abstract suspend fun getRemoteDataList(args: Map<String, Any?>): Result<List<T>>
    abstract fun shouldFetch(localDataList: List<T>, args: Map<String, Any?>): Boolean
    abstract suspend fun saveDataList(remoteDataList: List<T>): Result<Int>

    suspend fun getDataList(args: Map<String, Any?>): Result<List<T>> {
        when(val localRes = getLocalDataList(args)){
            is Success -> {
                val local = localRes.data
                if(shouldFetch(local, args)) {
                    when(val remoteRes = getRemoteDataList(args)){
                        is Success -> {
                            val remote = remoteRes.data
                            when(val saveRes = saveDataList(remote)){
                                is Success -> {
                                    return when(saveRes.data){
                                        remote.size -> Success(remote, 0)
                                        0 -> Util.cantInsertFailResult()
                                        else -> Success(remote, 1)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    return Success(local, 0)
                }
            }
        }
        return Util.cantGetFailResult()
    }
}