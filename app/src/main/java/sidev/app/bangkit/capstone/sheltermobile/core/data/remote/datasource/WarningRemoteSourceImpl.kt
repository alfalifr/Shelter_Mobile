package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.LocationLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.DisasterApi
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.EarthQuakeBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.FloodBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.LandslideBody
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.TimeString
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningDetail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningStatus
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.*
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.remoteToDbTimeFormat
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toWarningDetailListResult
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.lib.android.std.tool.util.`fun`.loge
import java.lang.IllegalStateException

class WarningRemoteSourceImpl(
    private val disasterApi: DisasterApi,
    private val localRepo: LocationRepo,
    private val disasterRepo: DisasterRepo,
): WarningRemoteSource {
    override suspend fun getWarningStatusBatch(
        disasterId: Int,
        locationId: Int,
        startTimestamp: TimeString
    ): Result<List<WarningStatus>> {
        val locRes = localRepo.getLocationById(locationId)
        if(locRes !is Success)
            return locRes as Fail

        val location = locRes.data
        loge("WarningRemoteSourceImpl.getWarningStatusBatch() location= $location")

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

                earthQuakeRes.body()!!.filter { it.date.remoteToDbTimeFormat() >= startTimestamp }.map { it.toModel(location) }
            }
            //TODO Alif 6 Juni 2021: Test
            Const.Disaster.FLOOD -> {
                val floodReqBody = FloodBody(locRes.data.name)
                val floodRes = disasterApi.getFloodPredictions(floodReqBody).execute()
                if(!floodRes.isSuccessful)
                    return Fail("Can't get flood warning status", floodRes.code(), null)

                floodRes.body()!!.map { it.toModel(location) }
            }
            //TODO Alif 4 Juni 2021: Tambahi predictions bencana lain
            Const.Disaster.FOREST_FIRE -> emptyList()
            else -> throw IllegalStateException("No such disaster name ($disasterName)")
        }
        loge("WarningRemote.getWarningStatusBatch() list.size = ${list.size} disasterId= $disasterId locationId= $locationId")
        return Success(list, 0)
    }

    override suspend fun getWarningDetailBatch(
        disasterId: Int,
        locationId: Int,
        startTimestamp: TimeString
    ): Result<List<WarningDetail>> =
        getWarningStatusBatch(disasterId, locationId, startTimestamp).toWarningDetailListResult()

    override suspend fun saveWarningDetailList(list: List<WarningDetail>): Result<Int> =
        Util.operationNotAvailableFailResult("WarningRemoteSourceImpl.saveWarningDetailList(list: List<WarningDetail>)")
}