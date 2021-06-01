package sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.room.WarningDao
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Coordinate
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningDetail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningStatus
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toEntity
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModelDetail
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

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
        startTimestamp: String
    ): Result<List<WarningStatus>> {
        val disaster = when(val res = disasterLocalSrc.getDisaster(disasterId)){
            is Success -> res.data
            else -> return Util.noEntityFailResult()
        }
        val location = when(val res = locationLocalSrc.getLocationById(locationId)){
            is Success -> res.data
            else -> return Util.noEntityFailResult()
        }
        val list = dao.getWarningStatusBatch(disasterId, locationId, startTimestamp).map {
            val emergency = when(val res = emergencyLocalSrc.getEmergency(it.emergencyId)){
                is Success -> res.data
                else -> return Util.noEntityFailResult()
            }
            it.toModel(disaster, emergency, location)
        }
        return Success(list, 0)
    }

    override suspend fun getWarningDetailBatch(
        disasterId: Int,
        locationId: Int,
        startTimestamp: String
    ): Result<List<WarningDetail>> {
        val disaster = when(val res = disasterLocalSrc.getDisaster(disasterId)){
            is Success -> res.data
            else -> return Util.noEntityFailResult()
        }
        val location = when(val res = locationLocalSrc.getLocationById(locationId)){
            is Success -> res.data
            else -> return Util.noEntityFailResult()
        }
        val list = dao.getWarningStatusBatch(disasterId, locationId, startTimestamp).map {
            val emergency = when(val res = emergencyLocalSrc.getEmergency(it.emergencyId)){
                is Success -> res.data
                else -> return Util.noEntityFailResult()
            }
            val news = when(val res = newsLocalSrc.getNews(it.timestamp)){
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
        return Util.getInsertResult(insertedCount, entityList.size)
    }
}