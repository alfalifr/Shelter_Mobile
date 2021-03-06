package sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.*
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningStatus
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WeatherForecast
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase.DashboardUseCase
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.model.DisasterGroup
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.lib.`val`.SuppressLiteral
import sidev.lib.android.std.tool.util.`fun`.loge

class DashboardViewModel(app: Context?, val useCase: DashboardUseCase): AsyncVm(app) {
    companion object {
        fun getInstance(
            owner: ViewModelStoreOwner,
            app: Context?,
            useCase: DashboardUseCase,
        ): DashboardViewModel = ViewModelProvider(
            owner,
            object: ViewModelProvider.Factory {
                @Suppress(SuppressLiteral.UNCHECKED_CAST)
                override fun <T : ViewModel?> create(modelClass: Class<T>): T = DashboardViewModel(
                    app, useCase
                ) as T
            }
        ).get(DashboardViewModel::class.java)
    }

    val higlightedWarningStatus: LiveData<WarningStatus> get()= mhiglightedWarningStatus
    private val mhiglightedWarningStatus = MutableLiveData<WarningStatus>()

    val weatherForecast: LiveData<WeatherForecast> get()= mWeatherForecast
    private val mWeatherForecast = MutableLiveData<WeatherForecast>()

    val disasterStatusList: LiveData<List<DisasterGroup>> get()= mDisasterStatusList
    private val mDisasterStatusList = MutableLiveData<List<DisasterGroup>>()

    val currentLocation: LiveData<Location> get()= mCurrentLocation
    private val mCurrentLocation = MutableLiveData<Location>()

    val currentUser: LiveData<User> get()= mCurrentUser
    private val mCurrentUser = MutableLiveData<User>()

    fun getCurrentLocation(forceLoad: Boolean = false){
        if(currentLocation.value != null && !forceLoad) return
        startJob(Const.KEY_CURRENT_LOC) {
            when(val result = useCase.getCurrentLocation()){
                is Success -> mCurrentLocation.postValue(result.data)
                is Fail -> doCallNotSuccess(Const.KEY_CURRENT_LOC, result.code, result.error)
            }
        }
    }

    fun getCurrentUser(forceLoad: Boolean = false){
        if(currentUser.value != null && !forceLoad) return
        startJob(Const.KEY_CURRENT_USER) {
            when(val result = useCase.getCurrentUser()){
                is Success -> mCurrentUser.postValue(result.data)
                is Fail -> doCallNotSuccess(Const.KEY_CURRENT_USER, result.code, result.error)
            }
        }
    }

    fun getWeatherForecast(forceLoad: Boolean = false){
        if(weatherForecast.value != null && !forceLoad) return
        startJob(Const.KEY_WEATHER_FORECAST) {
            val timestamp = Util.getTimeString()
            when(val result = useCase.getHighlightedWeatherForecast(timestamp).also { loge("DashboardVm.getWeatherForecast() res= $it") }){
                is Success -> mWeatherForecast.postValue(result.data)
                is Fail -> doCallNotSuccess(Const.KEY_WEATHER_FORECAST, result.code, result.error)
            }
        }
    }

    fun getHighlightedWarningStatus(forceLoad: Boolean = false){
        if(higlightedWarningStatus.value != null && !forceLoad) return
        startJob(Const.KEY_WARNING_HIGHLIGHT) {
            val timestamp = Util.getTimeString()
            when(val result = useCase.getHighlightedWarningStatus(timestamp)){
                is Success -> mhiglightedWarningStatus.postValue(result.data)
                is Fail -> doCallNotSuccess(Const.KEY_WARNING_HIGHLIGHT, result.code, result.error)
            }
        }
    }

    fun getDisasterGroupList(forceLoad: Boolean = false){
        if(disasterStatusList.value != null && !forceLoad) return
        startJob(Const.KEY_DISASTER_GROUP_LIST) {
            val timestamp = Util.getTimeString()
            when(val result = useCase.getDisasterGroupList(timestamp)){
                is Success -> {
                    val disasterWarningList = DataMapper.toDisasterGroupList(result.data)
                    mDisasterStatusList.postValue(disasterWarningList)
                }
                is Fail -> doCallNotSuccess(Const.KEY_DISASTER_GROUP_LIST, result.code, result.error)
            }
        }
    }
/*
    fun getDisasterGroupList(forceLoad: Boolean = false){
        if(currentLocation.value != null && !forceLoad) return
        cancelJob()
        doOnPreAsyncTask()
        job = GlobalScope.launch(Dispatchers.IO) {
            when(val disasterListResult = useCase.getDisasterList()){
                is Success -> {
                    getCurrentLocation()
                    val location = currentLocation.waitForValue()
                    val timestamp = Util.getTimestamp()

                    val disasterList = disasterListResult.data
                    val warningStatusLists = mutableListOf<List<WarningStatus>>()
                    for(disaster in disasterList){
                        when(val warningStatusListResult = useCase.getWarningStatusBatch(disaster.id, location.coordinate, timestamp)) {
                            is Success -> warningStatusLists += warningStatusListResult.data
                            is Fail -> {
                                doCallNotSuccess(warningStatusListResult.code, warningStatusListResult.error)
                                break
                            }
                        }
                    }
                    if(warningStatusLists.size == disasterList.size) {
                        val disasterGroupList = DataMapper.toDisasterGroupList(disasterList, warningStatusLists)
                        mDisasterStatusList.postValue(disasterGroupList)
                    }
                }
                is Fail -> doCallNotSuccess(disasterListResult.code, disasterListResult.error)
            }
        }
    }
 */


}