package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.dummy.Dummy
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Report
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.ReportDetail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

object ReportRemoteSourceDummy: ReportRemoteSource {
    override suspend fun getReportList(top: Int): Result<List<Report>> = Dummy.getEmptyListResult()

    override suspend fun getReportDetailList(top: Int): Result<List<ReportDetail>> = Dummy.getEmptyListResult()

    override suspend fun getReportDetail(timestamp: String): Result<ReportDetail> = Util.noEntityFailResult()

    override suspend fun saveReportDetail(data: ReportDetail): Result<Int> = Util.operationNotAvailableFailResult()

    override suspend fun saveReportDetailList(list: List<ReportDetail>): Result<Int> = Util.operationNotAvailableFailResult()

    override suspend fun sendReport(data: Report, user: User): Result<Boolean> = Success(true, 0)
}