package sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.room.LocationDao
import sidev.app.bangkit.capstone.sheltermobile.core.data.local.room.ReportDao
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Report
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.ReportDetail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toEntity
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModelDetail
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

class ReportLocalSourceImpl(private val dao: ReportDao, private val locationDao: LocationDao): ReportLocalSource {
    override suspend fun getReportList(top: Int): Result<List<Report>> {
        val entityList = if(top <= 0) dao.getAllReportList() else dao.getTopReportList(top)
        val locationList = mutableListOf<Location>()
        val list = entityList.map { loc ->
            val location = locationList.find { it.id == loc.locationId }
                ?: run {
                    val queriedLoc = locationDao.getLocationById(loc.locationId)
                        ?: return Util.noEntityFailResult()
                    val locModel = queriedLoc.toModel()
                    locationList += locModel
                    locModel
                }
            loc.toModel(location)
        }
        return Success(list, 0)
    }

    override suspend fun getReportDetailList(top: Int): Result<List<ReportDetail>> {
        val entityList = if(top <= 0) dao.getAllReportList() else dao.getTopReportList(top)
        val locationList = mutableListOf<Location>()
        val list = entityList.map { loc ->
            val location = locationList.find { it.id == loc.locationId }
                ?: run {
                    val queriedLoc = locationDao.getLocationById(loc.locationId)
                        ?: return Util.noEntityFailResult()
                    val locModel = queriedLoc.toModel()
                    locationList += locModel
                    locModel
                }
            loc.toModelDetail(location)
        }
        return Success(list, 0)
    }

    override suspend fun getReportDetail(timestamp: String): Result<ReportDetail> {
        val entity = dao.getReport(timestamp) ?: return Util.noEntityFailResult()

        val queriedLoc = locationDao.getLocationById(entity.locationId)
            ?: return Util.noEntityFailResult()
        val locModel = queriedLoc.toModel()

        val data = entity.toModelDetail(locModel)
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