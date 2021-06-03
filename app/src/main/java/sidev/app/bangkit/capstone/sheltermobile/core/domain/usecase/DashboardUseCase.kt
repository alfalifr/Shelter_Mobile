package sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Disaster
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningStatus
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WeatherForecast
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.DisasterRepo
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result

interface DashboardUseCase: WarningUseCase, DisasterRepo, WeatherUseCase, UserUseCase {
    suspend fun getDisasterGroupList(timestamp: String): Result<List<Pair<Disaster, List<WarningStatus>>>>
    suspend fun getHighlightedWarningStatus(timestamp: String): Result<WarningStatus>
    suspend fun getHighlightedWeatherForecast(startTimestamp: String): Result<WeatherForecast>
    suspend fun getAllBasicDataList(): Result<Boolean>
}