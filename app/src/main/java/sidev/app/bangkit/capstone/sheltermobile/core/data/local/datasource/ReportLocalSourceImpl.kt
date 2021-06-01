package sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.room.ReportDao
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Report
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.ReportDetail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toEntity
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModelDetail
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

class ReportLocalSourceImpl(
    private val dao: ReportDao,
    private val locationLocalSrc: LocationLocalSource,
    private val formLocalSrc: FormLocalSource,
): ReportLocalSource {
    override suspend fun getReportList(top: Int): Result<List<Report>> {
        val entityList = if(top <= 0) dao.getAllReportList() else dao.getTopReportList(top)
        val locationList = mutableListOf<Location>()
        val list = entityList.map { loc ->
            val location = locationList.find { it.id == loc.locationId }
                ?: run {
                    when(val res = locationLocalSrc.getLocationById(loc.locationId)) {
                        is Success -> {
                            locationList += res.data
                            res.data
                        }
                        is Fail -> return res
                    }
                }
            when(val res = formLocalSrc.getForm(loc.timestamp)){
                is Success -> loc.toModel(location, res.data)
                is Fail -> return res
            }
        }
        return Success(list, 0)
    }

    override suspend fun getReportDetailList(top: Int): Result<List<ReportDetail>> {
        val entityList = if(top <= 0) dao.getAllReportList() else dao.getTopReportList(top)
        val locationList = mutableListOf<Location>()
        val list = entityList.map { loc ->
            val location = locationList.find { it.id == loc.locationId }
                ?: run {
                    when(val res = locationLocalSrc.getLocationById(loc.locationId)) {
                        is Success -> {
                            locationList += res.data
                            res.data
                        }
                        is Fail -> return res
                    }
                }
            when(val res = formLocalSrc.getForm(loc.timestamp)){
                is Success -> loc.toModelDetail(location, res.data)
                is Fail -> return res
            }
        }
        return Success(list, 0)
    }

    override suspend fun getReportDetail(timestamp: String): Result<ReportDetail> {
        val entity = dao.getReport(timestamp) ?: return Util.noEntityFailResult()

        val locRes = locationLocalSrc.getLocationById(entity.locationId)
        if(locRes !is Success)
            return locRes as Fail

        val formRes = formLocalSrc.getForm(entity.timestamp)
        if(formRes !is Success)
            return formRes as Fail

        val locModel = locRes.data
        val formModel = formRes.data

        val data = entity.toModelDetail(locModel, formModel)
        return Success(data, 0)
    }

    override suspend fun saveReportDetail(data: ReportDetail): Result<Int> {
        val entity = data.toEntity() //ReportDetail(data, "").toEntity()
        val rowId = dao.saveReport(entity)
        val insertedCount = if(rowId >= 0) 1 else 0
        return Util.getInsertResult(insertedCount, 1)
    }

    override suspend fun saveReportDetailList(list: List<ReportDetail>): Result<Int> {
        val entityList = list.map { it.toEntity() } //ReportDetail(data, "").toEntity()
        val insertedCount = dao.saveReportList(entityList)
        return Util.getInsertResult(insertedCount, entityList.size)
    }
}