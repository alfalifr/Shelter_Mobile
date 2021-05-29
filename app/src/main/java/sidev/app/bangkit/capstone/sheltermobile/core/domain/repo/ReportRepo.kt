package sidev.app.bangkit.capstone.sheltermobile.core.domain.repo

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Report
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.ReportDetail

interface ReportRepo {
    suspend fun getReportList(): Result<List<Report>>
    suspend fun getReportDetail(timestamp: String): Result<ReportDetail>
    //suspend fun getMethod(id: Int): Result<ReportMethod>
    //suspend fun getMethodList(): Result<List<ReportMethod>>

    suspend fun saveReport(data: Report): Result<Int>
    //suspend fun saveMethodList(list: List<ReportMethod>): Result<Int>
}