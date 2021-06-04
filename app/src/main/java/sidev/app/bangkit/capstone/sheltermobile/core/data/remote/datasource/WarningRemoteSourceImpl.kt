package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.LocationLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.DisasterApi
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.EarthQuakeBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.LandslideBody
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningDetail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningStatus
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.*
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toWarningDetailListResult
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import java.lang.IllegalStateException

class WarningRemoteSourceImpl(
    private val disasterApi: DisasterApi,
    private val localRepo: LocationRepo,
    private val disasterRepo: DisasterRepo,
): WarningRemoteSource {
    override suspend fun getWarningStatusBatch(
        disasterId: Int,
        locationId: Int,
        startTimestamp: String
    ): Result<List<WarningStatus>> {
        val locRes = localRepo.getLocationById(locationId)
        if(locRes !is Success)
            return locRes as Fail

        val location = locRes.data

        val disasterRes = disasterRepo.getDisaster(disasterId)
        if(disasterRes !is Success)
            return disasterRes as Fail


        val list = when(val disasterName = disasterRes.data.name) {
            Const.Disaster.LANDSLIDE -> {
                val landslideReqBody = LandslideBody(location.name)
                val landslideRes = disasterApi.getLandslidePredictions(landslideReqBody).execute()
                if(!landslideRes.isSuccessful)
                    return Fail("Can't get landslide warning status", landslideRes.code(), null)

                landslideRes.body()!!.map { it.toModel(location) }
            }
            Const.Disaster.EARTH_QUAKE -> {
                val earthQuakeReqBody = EarthQuakeBody(locRes.data.name)
                val earthQuakeRes = disasterApi.getEarthQuakePredictions(earthQuakeReqBody).execute()
                if(!earthQuakeRes.isSuccessful)
                    return Fail("Can't get earth quake warning status", earthQuakeRes.code(), null)

                earthQuakeRes.body()!!.map { it.toModel(location) }
            }
            //TODO Alif 4 Juni 2021: Tambahi predictions bencana lain
            Const.Disaster.FLOOD -> emptyList()
            Const.Disaster.FOREST_FIRE -> emptyList()
            else -> throw IllegalStateException("No such disaster name ($disasterName)")
        }
        return Success(list, 0)
    }

    override suspend fun getWarningDetailBatch(
        disasterId: Int,
        locationId: Int,
        startTimestamp: String
    ): Result<List<WarningDetail>> =
        getWarningStatusBatch(disasterId, locationId, startTimestamp).toWarningDetailListResult()

    override suspend fun saveWarningDetailList(list: List<WarningDetail>): Result<Int> =
        Util.operationNotAvailableFailResult("WarningRemoteSourceImpl.saveWarningDetailList(list: List<WarningDetail>)")
}