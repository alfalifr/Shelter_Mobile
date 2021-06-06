package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.LocationApi
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.Requests
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Coordinate
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.lib.collection.uniqueBy
import java.util.*

class LocationRemoteSourceImpl(private val api: LocationApi): LocationRemoteSource {
    override suspend fun getAllLocation(): Result<List<Location>> {
        val earthQuakeCall = api.getLocationBasedOnDisaster(Requests.getGempaCity) //api.getDisasterLocations(Requests.getGempaCity)
        val landslideCall = api.getLocationBasedOnDisaster(Requests.getLongsorCity)
        val fireCall = api.getLocationBasedOnDisaster(Requests.getKarhutlaCity)
        val floodCall = api.getLocationBasedOnDisaster(Requests.getBanjirVillage)

        val earthQuakeRes = earthQuakeCall.execute()
        if(!earthQuakeRes.isSuccessful)
            return Fail("Can't get city for earthquake from remote", earthQuakeRes.code(), null)
        val earthQuakeLocs = earthQuakeRes.body()!!.toModel()

        val landslideRes = landslideCall.execute()
        if(!landslideRes.isSuccessful)
            return Fail("Can't get city for landslide from remote", landslideRes.code(), null)
        val landslideLocs = landslideRes.body()!!.toModel()

        val fireRes = fireCall.execute()
        if(!fireRes.isSuccessful)
            return Fail("Can't get city for forest fire from remote", fireRes.code(), null)
        val fireLocs = fireRes.body()!!.toModel()

        val floodRes = floodCall.execute()
        if(!floodRes.isSuccessful)
            return Fail("Can't get village for flood from remote", floodRes.code(), null)
        val floodLocs = floodRes.body()!!.toModel()

        val list = earthQuakeLocs + landslideLocs + fireLocs + floodLocs
        val uniqueList = list.uniqueBy { it.name.toLowerCase(Locale.ROOT) }

        return Success(uniqueList, 0)
    }

    override suspend fun getLocation(coordinate: Coordinate): Result<Location> =
        Util.operationNotAvailableFailResult("LocationRemoteSourceImpl.getLocation(coordinate: Coordinate)")

    override suspend fun getLocationById(id: Int): Result<Location> =
        Util.operationNotAvailableFailResult("LocationRemoteSourceImpl.getLocationById(id: Int)")

    override suspend fun saveLocationList(list: List<Location>): Result<Int> =
        Util.operationNotAvailableFailResult("LocationRemoteSourceImpl.saveLocationList(list: List<Location>)")
}