package sidev.app.bangkit.capstone.sheltermobile.core.di

import sidev.app.bangkit.capstone.sheltermobile.core.data.composite.*
import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.*
import sidev.app.bangkit.capstone.sheltermobile.core.data.local.room.RoomDb
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.AppRetrofit
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.*

object RepoDi {
    object Local {
        fun getDb(): RoomDb = if(!ConfigDi.isTest) RoomDb.getInstance(AppDi.getContext()) else RoomDb.getInstanceForTest(AppDi.getContext())
        fun getDisasterSrc(): DisasterLocalSource = DisasterLocalSourceImpl(getDb().getDisasterDao())
        fun getEmergencySrc(): EmergencyLocalSource = EmergencyLocalSourceImpl(getDb().getEmergencyDao())
        fun getLocationSrc(): LocationLocalSource = LocationLocalSourceImpl(getDb().getLocationDao(), AppDi.getContext())
        fun getNewsSrc(): NewsLocalSource = NewsLocalSourceImpl(getDb().getNewsDao())
        fun getReportSrc(): ReportLocalSource = ReportLocalSourceImpl(getDb().getReportDao(), getLocationSrc(), getFormSrc())
        fun getFormSrc(): FormLocalSource = FormLocalSourceImpl(getDb().getFormDao())
        fun getUserSrc(): UserLocalSource = UserLocalSourceImpl(getDb().getUserDao(), AppDi.getContext())
        fun getWarningSrc(): WarningLocalSource = WarningLocalSourceImpl(
            getDb().getWarningDao(),
            getDisasterSrc(),
            getEmergencySrc(),
            getLocationSrc(),
            getNewsSrc(),
        )
        fun getWeatherSrc(): WeatherLocalSource = WeatherLocalSourceImpl(getDb().getWeatherDao(), AppDi.getContext())
    }
    object Remote {
        fun getDisasterSrc(): DisasterRemoteSource = DisasterRemoteSourceDummy
        fun getEmergencySrc(): EmergencyRemoteSource = EmergencyRemoteSourceDummy
        fun getLocationSrc(): LocationRemoteSource = LocationRemoteSourceDummy
        fun getReportSrc(): ReportRemoteSource = ReportRemoteSourceDummy
        fun getFormSrc(): FormRemoteSource = FormRemoteSourceDummy
        fun getWarningSrc(): WarningRemoteSource = WarningRemoteSourceDummy
        fun getWeatherSrc(): WeatherRemoteSource = WeatherRemoteSourceDummy
        fun getNewsSrc(): NewsRemoteSource = NewsRemoteSourceImpl(AppRetrofit.newsApi)
        fun getUserSrc(): UserRemoteSource = UserRemoteSourceImpl(AppRetrofit.authApi)
    }


    fun getDisasterRepo(): DisasterCompositeSource = DisasterCompositeSource(Local.getDisasterSrc(), Remote.getDisasterSrc())
    fun getEmergencyRepo(): EmergencyCompositeSource = EmergencyCompositeSource(Local.getEmergencySrc(), Remote.getEmergencySrc())
    fun getLocationRepo(): LocationCompositeSource = LocationCompositeSource(Local.getLocationSrc(), Remote.getLocationSrc())
    fun getNewsRepo(): NewsCompositeSource = NewsCompositeSource(Local.getNewsSrc(), Remote.getNewsSrc())
    fun getReportRepo(): ReportCompositeSource = ReportCompositeSource(Local.getReportSrc(), Remote.getReportSrc())
    fun getFormRepo(): FormCompositeSource = FormCompositeSource(Local.getFormSrc(), Remote.getFormSrc())
    fun getUserRepo(): UserCompositeSource = UserCompositeSource(Local.getUserSrc(), Remote.getUserSrc())
    fun getWarningRepo(): WarningCompositeSource = WarningCompositeSource(Local.getWarningSrc(), Remote.getWarningSrc())
    fun getWeatherRepo(): WeatherCompositeSource = WeatherCompositeSource(Local.getWeatherSrc(), Remote.getWeatherSrc())
}