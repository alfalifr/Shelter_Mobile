package sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.room.ReportDao
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Report
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.ReportDetail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.TimeString
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toEntity
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModelDetail
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.lib.android.std.tool.util.`fun`.loge

class ReportLocalSourceImpl(
    private val dao: ReportDao,
    private val locationLocalSrc: LocationLocalSource,
    private val formLocalSrc: FormLocalSource,
): ReportLocalSource {
    override suspend fun getReportList(top: Int): Result<List<Report>> {
        val entityList = if(top <= 0) dao.getAllReportList() else dao.getTopReportList(top)
        val locationList = mutableListOf<Location>()
        val list = entityList.map { reportEntity ->
            val location = locationList.find { it.id == reportEntity.locationId }
                ?: run {
                    when(val res = locationLocalSrc.getLocationById(reportEntity.locationId)) {
                        is Success -> {
                            locationList += res.data
                            res.data
                        }
                        is Fail -> return res
                    }
                }
            val timeStr = Util.getStandardTimeString(reportEntity.timestamp)
            val form = when(val res = formLocalSrc.getForm(timeStr)){
                is Success -> res.data
                is Fail -> {
                    if(reportEntity.method == Const.METHOD_FORM) return res
                    else null
                }
            }
            reportEntity.toModel(location, form)
        }
        return Success(list, 0)
    }

    override suspend fun getReportDetailList(top: Int): Result<List<ReportDetail>> {
        val entityList = if(top <= 0) dao.getAllReportList() else dao.getTopReportList(top)
        loge("ReportLocalSourceImpl.getReportDetailList() entityList= $entityList")
        val locationList = mutableListOf<Location>()
        val list = entityList.map { reportEntity ->
            val location = locationList.find { it.id == reportEntity.locationId }
                ?: run {
                    when(val res = locationLocalSrc.getLocationById(reportEntity.locationId)) {
                        is Success -> {
                            locationList += res.data
                            res.data
                        }
                        is Fail -> return res
                    }
                }
            loge("ReportLocalSourceImpl.getReportDetailList() location= $location")
            val timeStr = Util.getStandardTimeString(reportEntity.timestamp)
            val form = when(val res = formLocalSrc.getForm(timeStr)){
                is Success -> res.data
                is Fail -> {
                    if(reportEntity.method == Const.METHOD_FORM) return res.also { loge("ReportLocalSourceImpl.getReportDetailList() getForm() ERROR= $it") }
                    else null
                }
            }. also {
                loge("ReportLocalSourceImpl.getReportDetailList() getForm()= $it")
            }
            reportEntity.toModelDetail(location, form)
        }
        return Success(list, 0)
    }

    override suspend fun getReportDetail(timestamp: TimeString): Result<ReportDetail> {
        val entity = dao.getReport(timestamp.timeLong) ?: return Util.noEntityFailResult()

        val locRes = locationLocalSrc.getLocationById(entity.locationId)
        if(locRes !is Success)
            return locRes as Fail

        val timeStr = Util.getStandardTimeString(entity.timestamp)
        val formRes = formLocalSrc.getForm(timeStr)

        val formModel = if(formRes !is Success) {
            if(entity.method == Const.METHOD_FORM)
                return formRes as Fail
            else null
        } else formRes.data

        val locModel = locRes.data

        val data = entity.toModelDetail(locModel, formModel)
        return Success(data, 0)
    }

    override suspend fun saveReportDetail(data: ReportDetail): Result<Int> {
        loge("ReportLocalSourceImpl.saveReportDetail() data = $data")
        val entity = data.toEntity() //ReportDetail(data, "").toEntity()
        val form = data.report.form

        if(form != null) {
            when(val formRes = formLocalSrc.saveForm(form).also { loge("ReportLocalSourceImpl.saveReportDetail() saveForm res= $it") } ) {
                is Fail -> return formRes
            }
        }
        val rowId = dao.saveReport(entity)
        val insertedCount = if(rowId >= 0) 1 else 0
        return Util.getInsertResult(insertedCount, 1). also {
            loge("ReportLocalSourceImpl.saveReportDetail() detail = $it")
        }
    }

    override suspend fun saveReportDetailList(list: List<ReportDetail>): Result<Int> {
        val entityList = list.map { it.toEntity() } //ReportDetail(data, "").toEntity()
        val insertedCount = dao.saveReportList(entityList)
        return Util.getInsertResult(insertedCount, entityList.size)
    }
}