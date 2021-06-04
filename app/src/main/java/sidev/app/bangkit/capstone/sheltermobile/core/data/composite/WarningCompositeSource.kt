package sidev.app.bangkit.capstone.sheltermobile.core.data.composite

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.WarningLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.WarningRemoteSource
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningDetail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningStatus
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.WarningRepo
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toWarningDetailListResult
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toWarningStatusListResult
import java.lang.IllegalArgumentException

class WarningCompositeSource(
    private val localSrc: WarningLocalSource,
    private val remoteSrc: WarningRemoteSource,
): CompositeDataSource<WarningDetail>(), WarningRepo {
    override suspend fun getLocalDataList(args: Map<String, Any?>): Result<List<WarningDetail>> = getDataList(localSrc, args)

    override suspend fun getRemoteDataList(args: Map<String, Any?>): Result<List<WarningDetail>> = getDataList(remoteSrc, args)

    private suspend fun getDataList(repo: WarningRepo, args: Map<String, Any?>): Result<List<WarningDetail>> {
        val disasterId = args[Const.KEY_DISASTER_ID] as? Int ?: throw IllegalArgumentException("args[Const.KEY_DISASTER_ID] == null")
        val locationId = args[Const.KEY_LOCATION_ID] as? Int ?: throw IllegalArgumentException("args[Const.KEY_LOCATION_ID] == null")
        val timestamp = args[Const.KEY_TIMESTAMP] as? String ?: throw IllegalArgumentException("args[Const.KEY_TIMESTAMP] == null")
        val noNews = args[Const.KEY_NO_NEWS] as? Boolean ?: true

        return if(noNews) repo.getWarningStatusBatch(disasterId, locationId, timestamp).toWarningDetailListResult()
        else repo.getWarningDetailBatch(disasterId, locationId, timestamp)
    }

    override fun shouldFetch(localDataList: List<WarningDetail>, args: Map<String, Any?>): Boolean = localDataList.isEmpty()

    override suspend fun saveDataList(remoteDataList: List<WarningDetail>): Result<Int> = localSrc.saveWarningDetailList(remoteDataList)


    override suspend fun getWarningStatusBatch(
        disasterId: Int,
        locationId: Int,
        startTimestamp: String
    ): Result<List<WarningStatus>> = getDataList(mapOf(
        Const.KEY_DISASTER_ID to disasterId,
        Const.KEY_LOCATION_ID to locationId,
        Const.KEY_TIMESTAMP to startTimestamp,
        Const.KEY_NO_NEWS to true,
    )).toWarningStatusListResult()

    override suspend fun getWarningDetailBatch(
        disasterId: Int,
        locationId: Int,
        startTimestamp: String
    ): Result<List<WarningDetail>> = getDataList(mapOf(
        Const.KEY_DISASTER_ID to disasterId,
        Const.KEY_LOCATION_ID to locationId,
        Const.KEY_TIMESTAMP to startTimestamp,
        Const.KEY_NO_NEWS to false,
    ))

    override suspend fun saveWarningDetailList(list: List<WarningDetail>): Result<Int> = saveDataList(list)
}