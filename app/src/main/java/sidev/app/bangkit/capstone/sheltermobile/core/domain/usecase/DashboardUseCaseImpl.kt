package sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Disaster
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningStatus
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WeatherForecast
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.DisasterRepo
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success

class DashboardUseCaseImpl(
    private val warningUseCase: WarningUseCase,
    private val disasterRepo: DisasterRepo,
    private val weatherUseCase: WeatherUseCase,
    private val userUseCase: UserUseCase
): DashboardUseCase,
    WarningUseCase by warningUseCase,
    DisasterRepo by disasterRepo,
    WeatherUseCase by weatherUseCase,
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

    override suspend fun getCurrentLocation(): Result<Location> {
        val result = userUseCase.getCurrentLocation()
        if(result is Success)
            currentLocation = result.data
        return result
    }

    override suspend fun getWeatherForecast(timestamp: String): Result<WeatherForecast> {
        if(currentLocation == null) {
            val res = getCurrentLocation()
            if(res is Fail)
                return res
        }
        return getWeatherForecast(timestamp, currentLocation!!.id)
    }

    override suspend fun getDisasterGroupList(timestamp: String): Result<List<Pair<Disaster, List<WarningStatus>>>> {
        val res = getCurrentLocation()
        if(res is Fail) return res
        if(currentLocation == null) return Fail("currentLocation == null", -1, null)

        val location = currentLocation!!

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
            return Success(disasterGroupList, 0)
        }

        return unknownErrorResult
    }

    override suspend fun getHighlightedWarningStatus(timestamp: String): Result<WarningStatus> {
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
        else Fail("No warning status available", -1, null)
    }
}