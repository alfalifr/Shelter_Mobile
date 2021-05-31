package sidev.app.bangkit.capstone.sheltermobile.core.di

import sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase.*

object UseCaseDi {
    fun getDashbosrdUseCase(): DashboardUseCase = DashboardUseCaseImpl(
        getWarningUseCase(), RepoDi.getDisasterRepo(), getWeatherUseCase(), getUserUseCase()
    )
    fun getLocationUseCase(): LocationUseCase = LocationUseCaseImpl(RepoDi.getLocationRepo())
    fun getNewsUseCase(): NewsUseCase = NewsUseCaseImpl(RepoDi.getNewsRepo())
    fun getReportUseCase(): ReportUseCase = ReportUseCaseImpl(RepoDi.getReportRepo(), RepoDi.Local.getLocationSrc())
    fun getUserUseCase(): UserUseCase = UserUseCaseImpl(
        RepoDi.getUserRepo(),
        RepoDi.Local.getUserSrc(),
        RepoDi.Remote.getUserSrc(),
        RepoDi.Local.getLocationSrc(),
    )
    fun getWarningUseCase(): WarningUseCase = WarningUseCaseImpl(RepoDi.getWarningRepo())
    fun getWeatherUseCase(): WeatherUseCase = WeatherUseCaseImpl(RepoDi.getWeatherRepo())
}