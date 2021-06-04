package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.LocationLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.DisasterApi
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.LandslideBody
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningDetail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningStatus
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.LocationRepo
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toWarningDetailListResult
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

class WarningRemoteSourceImpl(
    private val disasterApi: DisasterApi,
    private val localRepo: LocationRepo,
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

        val landslideReqBody = LandslideBody(location.name)
        val landslideRes = disasterApi.getLandslidePredictions(landslideReqBody).execute()
        if(!landslideRes.isSuccessful)
            return Fail("Can't get landslide warning status", landslideRes.code(), null)

        val landslideList = landslideRes.body()!!.map { it.toModel(location) }

        val earthQuakeReqBody = LandslideBody(locRes.data.name)
        val earthQuakeRes = disasterApi.getLandslidePredictions(landslideReqBody).execute()
        if(!earthQuakeRes.isSuccessful)
            return Fail("Can't get earth quake warning status", earthQuakeRes.code(), null)

        val earthQuakeList = earthQuakeRes.body()!!.map { it.toModel(location) }

        //TODO Alif 4 Juni 2021: Tambahi predictions bencana lain

        return Success(landslideList + earthQuakeList, 0)
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