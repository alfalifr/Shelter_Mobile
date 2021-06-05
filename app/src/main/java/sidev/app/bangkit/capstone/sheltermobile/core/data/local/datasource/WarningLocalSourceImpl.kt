package sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.room.WarningDao
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.TimeString
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningDetail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningStatus
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toEntity
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModelDetail
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.collection.asList

class WarningLocalSourceImpl(
    private val dao: WarningDao,
    private val disasterLocalSrc: DisasterLocalSource,
    private val emergencyLocalSrc: EmergencyLocalSource,
    private val locationLocalSrc: LocationLocalSource,
    private val newsLocalSrc: NewsLocalSource,
): WarningLocalSource {
    override suspend fun getWarningStatusBatch(
        disasterId: Int,
        locationId: Int,
        startTimestamp: TimeString
    ): Result<List<WarningStatus>> {
        val disaster = when(val res = disasterLocalSrc.getDisaster(disasterId).also { loge("WarningLocal.getWarningStatusBatch() disasterRes= $it") }){
            is Success -> res.data
            else -> return Util.noEntityFailResult()
        }
        val location = when(val res = locationLocalSrc.getLocationById(locationId).also { loge("WarningLocal.getWarningStatusBatch() locRes= $it disasterId= $disasterId locationId= $locationId") }){
            is Success -> res.data
            else -> return Util.noEntityFailResult()
        }
        val list = dao.getWarningStatusBatch(disasterId, locationId, startTimestamp.timeLong).map {
            val emergency = when(val res = emergencyLocalSrc.getEmergency(it.emergencyId).also { loge("WarningLocal.getWarningStatusBatch() emergencyRes= $it") }){
                is Success -> res.data
                else -> return Util.noEntityFailResult()
            }
            it.toModel(disaster, emergency, location)
        }
        loge("WarningLocal.getWarningStatusBatch() list.size = ${list.size} disasterId= $disasterId locationId= $locationId")
        return Success(list, 0)
    }

    override suspend fun getWarningDetailBatch(
        disasterId: Int,
        locationId: Int,
        startTimestamp: TimeString
    ): Result<List<WarningDetail>> {
        loge("WarningLocal.getWarningDetailBatch()")
        val disaster = when(val res = disasterLocalSrc.getDisaster(disasterId).also { loge("WarningLocal.getWarningDetailBatch() disasterRes= $it") }){
            is Success -> res.data
            else -> return Util.noEntityFailResult()
        }
        val location = when(val res = locationLocalSrc.getLocationById(locationId).also { loge("WarningLocal.getWarningDetailBatch() locRes= $it") }){
            is Success -> res.data
            else -> return Util.noEntityFailResult()
        }
        val list = dao.getWarningStatusBatch(disasterId, locationId, startTimestamp.timeLong).map {
            val emergency = when(val res = emergencyLocalSrc.getEmergency(it.emergencyId).also { loge("WarningLocal.getWarningDetailBatch() emergRes= $it") }){
                is Success -> res.data
                else -> return Util.noEntityFailResult()
            }
            val timeStr = Util.getStandardTimeString(it.timestamp)
            val news = when(val res = newsLocalSrc.getNews(timeStr).also { loge("WarningLocal.getWarningDetailBatch() newsRes= $it") }){
                is Success -> res.data
                else -> return Util.noEntityFailResult()
            }
            it.toModelDetail(disaster, emergency, location, news)
        }
        return Success(list, 0)
    }

    override suspend fun saveWarningDetailList(list: List<WarningDetail>): Result<Int> {
        val entityList = list.map { it.toEntity() }
        val insertedCount = dao.saveWarningList(entityList)
        val emergencyList = list.map { it.status.emergency }.toSet().asList()
        emergencyLocalSrc.saveEmergencyList(emergencyList)
        val newsList = list.map { it.relatedNews }.toSet().asList()
        newsLocalSrc.saveNewsList(newsList)
        return Util.getInsertResult(insertedCount, entityList.size)
    }
}