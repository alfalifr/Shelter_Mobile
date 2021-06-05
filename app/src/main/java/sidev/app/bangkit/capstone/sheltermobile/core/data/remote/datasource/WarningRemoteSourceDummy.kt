package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.dummy.Dummy
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.TimeString
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningDetail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningStatus
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

object WarningRemoteSourceDummy: WarningRemoteSource {
    override suspend fun getWarningStatusBatch(
        disasterId: Int,
        locationId: Int,
        startTimestamp: TimeString
    ): Result<List<WarningStatus>> = Success(Dummy.getWarningStatusList(disasterId, locationId, startTimestamp), 0)

    override suspend fun getWarningDetailBatch(
        disasterId: Int,
        locationId: Int,
        startTimestamp: TimeString
    ): Result<List<WarningDetail>> = Success(Dummy.getWarningDetailList(disasterId, locationId, startTimestamp), 0)

    override suspend fun saveWarningDetailList(list: List<WarningDetail>): Result<Int> = Util.operationNotAvailableFailResult()
}