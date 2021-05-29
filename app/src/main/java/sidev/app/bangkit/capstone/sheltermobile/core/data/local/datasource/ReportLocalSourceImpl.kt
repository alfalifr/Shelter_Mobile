package sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.room.ReportDao
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Report
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.ReportDetail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toEntity
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModelDetail
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

class ReportLocalSourceImpl(private val dao: ReportDao): ReportLocalSource {
    override suspend fun getReportList(top: Int): Result<List<Report>> {
        val entityList = if(top <= 0) dao.getAllReportList() else dao.getTopReportList(top)
        val list = entityList.map { it.toModel() }
        return Success(list, 0)
    }

    override suspend fun getReportDetailList(top: Int): Result<List<ReportDetail>> {
        val entityList = if(top <= 0) dao.getAllReportList() else dao.getTopReportList(top)
        val list = entityList.map { it.toModelDetail() }
        return Success(list, 0)
    }

    override suspend fun getReportDetail(timestamp: String): Result<ReportDetail> {
        val data = dao.getReport(timestamp)?.toModelDetail() ?: return Util.noEntityFailResult()
        return Success(data, 0)
    }

    override suspend fun saveReportDetail(data: ReportDetail): Result<Int> {
        val entity = data.toEntity() //ReportDetail(data, "").toEntity()
        val insertedCount = dao.saveReport(entity)
        return Util.getInsertResult(insertedCount, 1)
    }

    override suspend fun saveReportDetailList(list: List<ReportDetail>): Result<Int> {
        val entityList = list.map { it.toEntity() } //ReportDetail(data, "").toEntity()
        val insertedCount = dao.saveReportList(entityList)
        return Util.getInsertResult(insertedCount, entityList.size)
    }
}