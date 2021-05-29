package sidev.app.bangkit.capstone.sheltermobile.core.data.composite

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.EmergencyLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Emergency
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.EmergencyRepo
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toListResult
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toSingleResult

class EmergencyCompositeSource(
    private val localSrc: EmergencyLocalSource,
): CompositeDataSource<Emergency>(), EmergencyRepo {
    override suspend fun getLocalDataList(args: Map<String, Any?>): Result<List<Emergency>> {
        val id = args[Const.KEY_LOCATION_ID]
        return if(id != null) localSrc.getEmergency(id as Int).toListResult()
        else localSrc.getAllEmergencies()
    }

    override suspend fun getRemoteDataList(args: Map<String, Any?>): Result<List<Emergency>> {
        TODO("Not yet implemented")
    }

    override fun shouldFetch(localDataList: List<Emergency>, args: Map<String, Any?>): Boolean = localDataList.isEmpty()

    override suspend fun saveDataList(remoteDataList: List<Emergency>): Result<Int> = localSrc.saveEmergencyList(remoteDataList)

    override suspend fun getAllEmergencies(): Result<List<Emergency>> = getDataList(emptyMap())

    override suspend fun getEmergency(id: Int): Result<Emergency> = getDataList(mapOf(Const.KEY_ID to id)).toSingleResult()

    /**
     * Returns saved count.
     */
    override suspend fun saveEmergencyList(list: List<Emergency>): Result<Int> = saveDataList(list)
}