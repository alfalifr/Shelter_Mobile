package sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase

import sidev.app.bangkit.capstone.sheltermobile.core.data.dummy.Dummy
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.*
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.*
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toSingleResult
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

class DashboardUseCaseImpl(
    private val warningUseCase: WarningUseCase,
    private val disasterRepo: DisasterRepo,
    private val emergencyRepo: EmergencyRepo,
    private val weatherUseCase: WeatherForecastUseCase,
    private val userUseCase: UserUseCase
): DashboardUseCase,
    WarningUseCase by warningUseCase,
    DisasterRepo by disasterRepo,
    WeatherForecastUseCase by weatherUseCase,
    UserUseCase by userUseCase
{
    private var currentLocation: Location?= null
    private var disasterList: List<Disaster>?= null
    private var disasterWarningList: List<Pair<Disaster, List<WarningStatus>>>?= null

    override suspend fun getDisasterList(): Result<List<Disaster>> {
        val result = disasterRepo.getDisasterList()
        if(result is Success)
            disasterList = result.data
        return result
    }

    override suspend fun getAllBasicDataList(): Result<Boolean> {
        if(disasterList == null) {
            val isDisasterSuccess = getDisasterList() is Success
            val isEmergencySuccess = emergencyRepo.getAllEmergencies() is Success
            return when {
                isDisasterSuccess && isEmergencySuccess -> Success(true, 0)
                else -> Util.cantGetFailResult()
            }
        }
        return Success(true, 0)
    }

    override suspend fun getCurrentLocation(): Result<Location> {
        val result = userUseCase.getCurrentLocation()
        if(result is Success)
            currentLocation = result.data
        return result
    }

    override suspend fun getHighlightedWeatherForecast(startTimestamp: TimeString): Result<WeatherForecast> {
        if(currentLocation == null) {
            val res = getCurrentLocation()
            if(res is Fail)
                return res
        }
        return getWeatherForecastBatch(startTimestamp, currentLocation!!.id).toSingleResult()
    }

    override suspend fun getDisasterGroupList(timestamp: TimeString): Result<List<Pair<Disaster, List<WarningStatus>>>> {
        val res = getCurrentLocation()
        if(res is Fail) return res
        if(currentLocation == null) return Fail("currentLocation == null", -1, null)

        val location = currentLocation!!
/*
        val basicDataRes = getAllBasicDataList()
        if(basicDataRes is Fail)
            return basicDataRes
 */
        
        val unknownErrorResult = Fail("Unknown error", -1, null)
        val disasterList = this.disasterList ?: when(val disasterListResult = getDisasterList()) {
            is Success -> disasterListResult.data
            is Fail -> return disasterListResult
            else -> return unknownErrorResult
        }

        val warningStatusLists = mutableListOf<List<WarningStatus>>()
        for(disaster in disasterList){
            when(val warningStatusListResult = warningUseCase.getWarningStatusBatch(disaster.id, location.id, timestamp)) {
                is Success -> warningStatusLists += warningStatusListResult.data
                is Fail -> {
                    return warningStatusListResult
                }
            }
        }
        if(warningStatusLists.size == disasterList.size) {
            val disasterGroupList = disasterList.mapIndexed { i, disaster ->
                disaster to warningStatusLists[i]
            }
            disasterWarningList = disasterGroupList
            return Success(disasterGroupList, 0)
        }

        return unknownErrorResult
    }

    override suspend fun getHighlightedWarningStatus(timestamp: TimeString): Result<WarningStatus> {
        if(disasterWarningList == null) {
            val result = getDisasterGroupList(timestamp)
            if(result is Fail) return result
        }
        var highestSeverity = -1
        var mostSevereWarning: WarningStatus?= null
        for(disasterWarning in disasterWarningList!!){
            for(warning in disasterWarning.second){
                if(highestSeverity < warning.emergency.severity){
                    highestSeverity = warning.emergency.severity
                    mostSevereWarning = warning
                }
            }
        }
        return if(mostSevereWarning != null) Success(mostSevereWarning, 0)
        else Success(Dummy.emptySafeWarningList.first(), -1) //Fail("No warning status available", -1, null)
    }
}