package sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningStatus
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WeatherForecast
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase.DashboardUseCase
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.model.DisasterGroup
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.lib.`val`.SuppressLiteral

class DashboardViewModel(app: Application?, val useCase: DashboardUseCase): AsyncVm(app) {
    companion object {
        fun getInstance(
            owner: ViewModelStoreOwner,
            app: Application?,
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

    fun getCurrentLocation(forceLoad: Boolean = false){
        if(currentLocation.value != null && !forceLoad) return
        cancelJob()
        doOnPreAsyncTask()
        job = GlobalScope.launch(Dispatchers.IO) {
            when(val result = useCase.getCurrentLocation()){
                is Success -> mCurrentLocation.postValue(result.data)
                is Fail -> doCallNotSuccess(result.code, result.error)
            }
        }
    }

    fun getWeatherForecast(forceLoad: Boolean = false){
        if(weatherForecast.value != null && !forceLoad) return
        cancelJob()
        doOnPreAsyncTask()
        job = GlobalScope.launch(Dispatchers.IO) {
            val timestamp = Util.getTimestampStr()
            when(val result = useCase.getWeatherForecast(timestamp)){
                is Success -> mWeatherForecast.postValue(result.data)
                is Fail -> doCallNotSuccess(result.code, result.error)
            }
        }
    }

    fun getHighlightedWarningStatus(forceLoad: Boolean = false){
        if(higlightedWarningStatus.value != null && !forceLoad) return
        cancelJob()
        doOnPreAsyncTask()
        job = GlobalScope.launch(Dispatchers.IO) {
            val timestamp = Util.getTimestampStr()
            when(val result = useCase.getHighlightedWarningStatus(timestamp)){
                is Success -> mhiglightedWarningStatus.postValue(result.data)
                is Fail -> doCallNotSuccess(result.code, result.error)
            }
        }
    }

    fun getDisasterGroupList(forceLoad: Boolean = false){
        if(disasterStatusList.value != null && !forceLoad) return
        cancelJob()
        doOnPreAsyncTask()
        job = GlobalScope.launch(Dispatchers.IO) {
            val timestamp = Util.getTimestampStr()
            when(val result = useCase.getDisasterGroupList(timestamp)){
                is Success -> {
                    val disasterWarningList = DataMapper.toDisasterGroupList(result.data)
                    mDisasterStatusList.postValue(disasterWarningList)
                }
                is Fail -> doCallNotSuccess(result.code, result.error)
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