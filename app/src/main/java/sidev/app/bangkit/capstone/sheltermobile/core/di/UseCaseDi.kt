package sidev.app.bangkit.capstone.sheltermobile.core.di

import sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase.*

object UseCaseDi {
    fun getDashbosrdUseCase(): DashboardUseCase = DashboardUseCaseImpl(
        getWarningUseCase(), RepoDi.getDisasterRepo(), RepoDi.getEmergencyRepo(), getWeatherUseCase(), getUserUseCase()
    )
    fun getLocationUseCase(): LocationUseCase = LocationUseCaseImpl(RepoDi.getLocationRepo(), RepoDi.Local.getLocationSrc())
    fun getNewsUseCase(): NewsUseCase = NewsUseCaseImpl(RepoDi.getNewsRepo())
    fun getReportUseCase(): ReportUseCase = ReportUseCaseImpl(
        repo = RepoDi.getReportRepo(),
        localSrc = RepoDi.Local.getReportSrc(),
        remoteSrc = RepoDi.Remote.getReportSrc(),
        locationLocalSrc =  RepoDi.Local.getLocationSrc(),
        userLocalSrc = RepoDi.Local.getUserSrc(),
    )
    fun getUserUseCase(): UserUseCase = UserUseCaseImpl(
        RepoDi.getUserRepo(),
        RepoDi.Local.getUserSrc(),
        RepoDi.Remote.getUserSrc(),
        RepoDi.Local.getLocationSrc(),
        RepoDi.getLocationRepo(),
    )
    fun getWarningUseCase(): WarningUseCase = WarningUseCaseImpl(RepoDi.getWarningRepo())
    fun getWeatherUseCase(): WeatherForecastUseCase = WeatherForecastUseCaseImpl(RepoDi.getWeatherForecastRepo())
}