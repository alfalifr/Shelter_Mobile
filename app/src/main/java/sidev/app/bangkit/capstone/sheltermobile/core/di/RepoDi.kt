package sidev.app.bangkit.capstone.sheltermobile.core.di

import sidev.app.bangkit.capstone.sheltermobile.core.data.composite.*
import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.*
import sidev.app.bangkit.capstone.sheltermobile.core.data.local.room.RoomDb
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.AppRetrofit
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.*
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.DisasterRepo
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.LocationRepo

object RepoDi {
    object Local {
        fun getDb(): RoomDb = if(!ConfigDi.isTest) RoomDb.getInstance(AppDi.getContext()) else RoomDb.getInstanceForTest(AppDi.getContext())
        fun getDisasterSrc(): DisasterLocalSource = DisasterLocalSourceImpl(getDb().getDisasterDao())
        fun getEmergencySrc(): EmergencyLocalSource = EmergencyLocalSourceImpl(getDb().getEmergencyDao())
        fun getLocationSrc(): LocationLocalSource = LocationLocalSourceImpl(getDb().getLocationDao(), AppDi.getContext())
            //else LocationLocalSourceDummy
        fun getNewsSrc(): NewsLocalSource = NewsLocalSourceImpl(getDb().getNewsDao())
        fun getReportSrc(): ReportLocalSource = ReportLocalSourceImpl(getDb().getReportDao(), getLocationSrc(), getFormSrc(), getUserSrc())
        fun getFormSrc(): FormLocalSource = FormLocalSourceImpl(getDb().getFormDao(), getUserSrc())
        fun getUserSrc(): UserLocalSource = UserLocalSourceImpl(getDb().getUserDao(), getLocationSrc(), AppDi.getContext())
        fun getWarningSrc(): WarningLocalSource = WarningLocalSourceImpl(
            getDb().getWarningDao(),
            getDisasterSrc(),
            getEmergencySrc(),
            getLocationSrc(),
            getNewsSrc(),
        )
        fun getWeatherForecastSrc(): WeatherForecastLocalSource = WeatherForecastLocalSourceImpl(
            getDb().getWeatherForecastDao(), getWeatherSrc(), AppDi.getContext()
        )
        fun getWeatherSrc(): WeatherLocalSource = WeatherLocalSourceImpl(getDb().getWeatherDao())
    }
    object Remote {
        fun getDisasterSrc(): DisasterRemoteSource = DisasterRemoteSourceDummy
        fun getEmergencySrc(): EmergencyRemoteSource = EmergencyRemoteSourceDummy
        fun getLocationSrc(): LocationRemoteSource = LocationRemoteSourceImpl(AppRetrofit.locationApi) //LocationRemoteSourceDummy //
        fun getReportSrc(): ReportRemoteSource = ReportRemoteSourceImpl(AppRetrofit.reportApi, AppRetrofit.imgurApi) //ReportRemoteSourceDummy
        fun getFormSrc(): FormRemoteSource = FormRemoteSourceDummy
        fun getWarningSrc(locationRepo: LocationRepo?= null, disasterRepo: DisasterRepo?= null): WarningRemoteSource =
            WarningRemoteSourceImpl(AppRetrofit.disasterApi, locationRepo ?: getLocationRepo(), disasterRepo ?: getDisasterRepo()) //WarningRemoteSourceDummy //
        fun getWeatherForecastSrc(): WeatherForecastRemoteSource = WeatherForecastRemoteSourceImpl(AppRetrofit.weatherApi) //WeatherForecastRemoteSourceDummy //
        fun getWeatherSrc(): WeatherRemoteSource = WeatherRemoteSourceDummy
        fun getNewsSrc(): NewsRemoteSource = NewsRemoteSourceImpl(AppRetrofit.newsApi) //NewsRemoteSourceDummy //
        fun getUserSrc(): UserRemoteSource = UserRemoteSourceImpl(AppRetrofit.authApi) //UserRemoteSourceDummy //
    }


    fun getDisasterRepo(): DisasterCompositeSource = DisasterCompositeSource(Local.getDisasterSrc(), Remote.getDisasterSrc())
    fun getEmergencyRepo(): EmergencyCompositeSource = EmergencyCompositeSource(Local.getEmergencySrc(), Remote.getEmergencySrc())
    fun getLocationRepo(): LocationCompositeSource = LocationCompositeSource(Local.getLocationSrc(), Remote.getLocationSrc())
    fun getNewsRepo(): NewsCompositeSource = NewsCompositeSource(Local.getNewsSrc(), Remote.getNewsSrc())
    fun getReportRepo(): ReportCompositeSource = ReportCompositeSource(Local.getReportSrc(), Remote.getReportSrc())
    fun getFormRepo(): FormCompositeSource = FormCompositeSource(Local.getFormSrc(), Remote.getFormSrc())
    fun getUserRepo(): UserCompositeSource = UserCompositeSource(Local.getUserSrc(), Remote.getUserSrc())
    fun getWarningRepo(): WarningCompositeSource = WarningCompositeSource(Local.getWarningSrc(), Remote.getWarningSrc())
    fun getWeatherForecastRepo(): WeatherForecastCompositeSource = WeatherForecastCompositeSource(Local.getWeatherForecastSrc(), Remote.getWeatherForecastSrc())
    fun getWeatherRepo(): WeatherCompositeSource = WeatherCompositeSource(Local.getWeatherSrc(), Remote.getWeatherSrc())
}