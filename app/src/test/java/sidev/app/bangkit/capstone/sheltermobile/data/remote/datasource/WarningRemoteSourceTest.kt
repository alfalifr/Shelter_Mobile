package sidev.app.bangkit.capstone.sheltermobile.data.remote.datasource

import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import sidev.app.bangkit.capstone.sheltermobile.core.data.dummy.Dummy
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.AppRetrofit
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.LocationApi
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.GeneralRequestBodyImpl
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.Requests
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.LocationRemoteSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.WarningRemoteSource
import sidev.app.bangkit.capstone.sheltermobile.core.di.RepoDi
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Coordinate
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Disaster
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.DisasterRepo
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.LocationRepo
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.remoteToDbTimeFormat
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.lib.console.prin

class WarningRemoteSourceTest {
    companion object {
        private val timestamp = "2021-01-01 00:00:00".remoteToDbTimeFormat()
        //private lateinit var location: Location
        private lateinit var location: Location
        val locationApi: LocationApi by lazy { AppRetrofit.locationApi }
        val locationSrc: LocationRepo = object: LocationRepo {
            override suspend fun getAllLocation(): Result<List<Location>> {
                TODO("Not yet implemented")
            }

            override suspend fun getLocation(coordinate: Coordinate): Result<Location> {
                TODO("Not yet implemented")
            }

            override suspend fun getLocationById(id: Int): Result<Location> = Success(location, 0)

            override suspend fun saveLocationList(list: List<Location>): Result<Int> {
                TODO("Not yet implemented")
            }
        }
        val disasterRepo: DisasterRepo = object: DisasterRepo {
            override suspend fun getDisasterList(): Result<List<Disaster>> {
                TODO("Not yet implemented")
            }

            override suspend fun getDisaster(id: Int): Result<Disaster> = Dummy.disasterList.find { it.id == id }
                ?.let { Success(it, 0) } ?: Util.noEntityFailResult()

            /**
             * Returns saved count.
             */
            override suspend fun saveDisasterList(list: List<Disaster>): Result<Int> {
                TODO("Not yet implemented")
            }
        }
        val src: WarningRemoteSource by lazy { RepoDi.Remote.getWarningSrc(locationSrc, disasterRepo) }
    }

    private lateinit var disaster: Disaster
    private lateinit var disLocReqBody: GeneralRequestBodyImpl

    @After
    fun check(){
        runBlocking {
            val res = locationApi.getLocationBasedOnDisaster(disLocReqBody).execute()

            assert(res.isSuccessful)

            val locName = res.body()!!.nestedcityList.first().cityList.first()
            location = Location(0, locName, Coordinate(0.0, 0.0))

            prin("location = $location")

            val predRes = src.getWarningStatusBatch(disaster.id, location.id, timestamp)

            assert(predRes is Success)
            val warningList = (predRes as Success).data

            prin("warningList= $warningList")

            assert(warningList.isNotEmpty())
        }
    }

    @Test
    fun getBanjirPredictions(){
        disaster = Dummy.getDisasterByName(Const.Disaster.FLOOD)
        disLocReqBody = Requests.getBanjirVillage
    }

    @Test
    fun getLongsorPredictions(){
        disaster = Dummy.getDisasterByName(Const.Disaster.LANDSLIDE)
        disLocReqBody = Requests.getLongsorCity
    }

    @Test
    fun getGempaPredictions(){
        disaster = Dummy.getDisasterByName(Const.Disaster.EARTH_QUAKE)
        disLocReqBody = Requests.getGempaCity
    }

    @Test
    fun getKarhutlaPredictions(){
        disaster = Dummy.getDisasterByName(Const.Disaster.FOREST_FIRE_SHORT)
        disLocReqBody = Requests.getKarhutlaCity
    }
}