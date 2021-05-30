package sidev.app.bangkit.capstone.sheltermobile.core.domain.repo

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Report
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.ReportDetail

interface ReportRepo {
    suspend fun getReportList(top: Int = -1): Result<List<Report>>
    suspend fun getReportDetailList(top: Int = -1): Result<List<ReportDetail>>
    suspend fun getReportDetail(timestamp: String): Result<ReportDetail>
    //suspend fun getMethod(id: Int): Result<ReportMethod>
    //suspend fun getMethodList(): Result<List<ReportMethod>>

    suspend fun saveReportDetail(data: ReportDetail): Result<Int>
    suspend fun saveReportDetailList(list: List<ReportDetail>): Result<Int>
    //suspend fun saveMethodList(list: List<ReportMethod>): Result<Int>
}