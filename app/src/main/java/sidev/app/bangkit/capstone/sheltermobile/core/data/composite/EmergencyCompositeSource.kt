package sidev.app.bangkit.capstone.sheltermobile.core.data.composite

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.EmergencyLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.EmergencyRemoteSource
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Disaster
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Emergency
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.*
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toListResult
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toSingleResult

class EmergencyCompositeSource(
    private val localSrc: EmergencyLocalSource,
    private val remoteSrc: EmergencyRemoteSource,
): CompositeDataSource<Emergency>(), EmergencyRepo {
    override suspend fun getLocalDataList(args: Map<String, Any?>): Result<List<Emergency>> = getDataList(localSrc, args)

    override suspend fun getRemoteDataList(args: Map<String, Any?>): Result<List<Emergency>> = getDataList(remoteSrc, args)

    private suspend fun getDataList(repo: EmergencyRepo, args: Map<String, Any?>): Result<List<Emergency>> {
        val id = args[Const.KEY_LOCATION_ID]
        return if(id != null) repo.getEmergency(id as Int).toListResult()
        else repo.getAllEmergencies()
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