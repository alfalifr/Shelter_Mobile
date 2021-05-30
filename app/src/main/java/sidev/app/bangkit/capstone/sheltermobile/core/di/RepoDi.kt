package sidev.app.bangkit.capstone.sheltermobile.core.di

import sidev.app.bangkit.capstone.sheltermobile.core.data.composite.*
import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.*
import sidev.app.bangkit.capstone.sheltermobile.core.data.local.room.RoomDb
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.AppRetrofit
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.NewsRemoteSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.NewsRemoteSourceImpl
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.UserRemoteSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.UserRemoteSourceImpl

object RepoDi {
    object Local {
        fun getDb(): RoomDb = RoomDb.getInstance(AppDi.getContext())
        fun getDisasterSrc(): DisasterLocalSource = DisasterLocalSourceImpl(getDb().getDisasterDao())
        fun getEmergencySrc(): EmergencyLocalSource = EmergencyLocalSourceImpl(getDb().getEmergencyDao())
        fun getLocationSrc(): LocationLocalSource = LocationLocalSourceImpl(getDb().getLocationDao(), AppDi.getContext())
        fun getNewsSrc(): NewsLocalSource = NewsLocalSourceImpl(getDb().getNewsDao())
        fun getReportSrc(): ReportLocalSource = ReportLocalSourceImpl(getDb().getReportDao())
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
        fun getNewsSrc(): NewsRemoteSource = NewsRemoteSourceImpl(AppRetrofit.newsApi)
        fun getUserSrc(): UserRemoteSource = UserRemoteSourceImpl(AppRetrofit.authApi)
    }


    fun getDisasterRepo(): DisasterCompositeSource = DisasterCompositeSource(Local.getDisasterSrc(), )
    fun getEmergencyRepo(): EmergencyCompositeSource = EmergencyCompositeSource(Local.getEmergencySrc(), )
    fun getLocationRepo(): LocationCompositeSource = LocationCompositeSource(Local.getLocationSrc(), )
    fun getNewsRepo(): NewsCompositeSource = NewsCompositeSource(Local.getNewsSrc(), Remote.getNewsSrc())
    fun getReportRepo(): ReportCompositeSource = ReportCompositeSource(Local.getReportSrc(), )
    fun getUserRepo(): UserCompositeSource = UserCompositeSource(Local.getUserSrc(), Remote.getUserSrc())
    fun getWarningRepo(): WarningCompositeSource = WarningCompositeSource(Local.getWarningSrc(), )
    fun getWeatherRepo(): WeatherCompositeSource = WeatherCompositeSource(Local.getWeatherSrc(), )
}