package sidev.app.bangkit.capstone.sheltermobile.core.data.composite

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.DisasterLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Disaster
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.DisasterRepo
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toListResult
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toSingleResult

class DisasterCompositeSource(
    private val localSrc: DisasterLocalSource,
): CompositeDataSource<Disaster>(), DisasterRepo {
    override suspend fun getLocalDataList(args: Map<String, Any?>): Result<List<Disaster>> {
        val id = args[Const.KEY_LOCATION_ID]
        return if(id != null) localSrc.getDisaster(id as Int).toListResult()
        else localSrc.getDisasterList()
    }

    override suspend fun getRemoteDataList(args: Map<String, Any?>): Result<List<Disaster>> {
        TODO("Not yet implemented")
    }

    override fun shouldFetch(localDataList: List<Disaster>, args: Map<String, Any?>): Boolean = localDataList.isEmpty()

    override suspend fun saveDataList(remoteDataList: List<Disaster>): Result<Int> = localSrc.saveDisasterList(remoteDataList)

    override suspend fun getDisasterList(): Result<List<Disaster>> = getDataList(emptyMap())

    override suspend fun getDisaster(id: Int): Result<Disaster> = getDataList(mapOf(Const.KEY_ID to id)).toSingleResult()

    /**
     * Returns saved count.
     */
    override suspend fun saveDisasterList(list: List<Disaster>): Result<Int> = saveDataList(list)
}