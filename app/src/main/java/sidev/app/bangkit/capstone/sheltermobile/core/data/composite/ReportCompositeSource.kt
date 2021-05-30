package sidev.app.bangkit.capstone.sheltermobile.core.data.composite

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.ReportLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.ReportRemoteSource
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Report
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.ReportDetail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.ReportRepo
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toListResult
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toReportListResult
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toSingleResult
import java.lang.IllegalArgumentException

class ReportCompositeSource(
    private val localSrc: ReportLocalSource,
    private val remoteSrc: ReportRemoteSource,
): CompositeDataSource<ReportDetail>(), ReportRepo {
    override suspend fun getLocalDataList(args: Map<String, Any?>): Result<List<ReportDetail>> = getDataList(localSrc, args)

    override suspend fun getRemoteDataList(args: Map<String, Any?>): Result<List<ReportDetail>> = getDataList(remoteSrc, args)

    private suspend fun getDataList(repo: ReportRepo, args: Map<String, Any?>): Result<List<ReportDetail>> {
        val isSingle = args[Const.KEY_IS_SINGLE] as? Boolean ?: false
        return if(!isSingle){
            val top = args[Const.KEY_TOP] as? Int ?: -1
            repo.getReportDetailList(top)
        } else {
            val timestamp = args[Const.KEY_TIMESTAMP] as? String ?: throw IllegalArgumentException("args[Const.KEY_TIMESTAMP] == null")
            repo.getReportDetail(timestamp).toListResult()
        }
    }

    override fun shouldFetch(localDataList: List<ReportDetail>, args: Map<String, Any?>): Boolean = localDataList.isEmpty()

    override suspend fun saveDataList(remoteDataList: List<ReportDetail>): Result<Int> = localSrc.saveReportDetailList(remoteDataList)


    override suspend fun getReportList(top: Int): Result<List<Report>> = getDataList(mapOf(
        Const.KEY_TOP to top,
    )).toReportListResult()

    override suspend fun getReportDetailList(top: Int): Result<List<ReportDetail>> = getDataList(mapOf(
        Const.KEY_TOP to top,
    ))

    override suspend fun getReportDetail(timestamp: String): Result<ReportDetail> = getDataList(mapOf(
        Const.KEY_IS_SINGLE to true,
        Const.KEY_TIMESTAMP to timestamp,
    )).toSingleResult()

    override suspend fun saveReportDetail(data: ReportDetail): Result<Int> = saveDataList(listOf(data))
    override suspend fun saveReportDetailList(list: List<ReportDetail>): Result<Int> = saveDataList(list)
}