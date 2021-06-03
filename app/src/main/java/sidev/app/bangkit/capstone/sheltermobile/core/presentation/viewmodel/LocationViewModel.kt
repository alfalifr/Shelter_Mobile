package sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.*
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase.LocationUseCase
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.lib.`val`.SuppressLiteral

class LocationViewModel(c: Context?, private val useCase: LocationUseCase): AsyncVm(c) {
    companion object {
        fun getInstance(
            owner: ViewModelStoreOwner,
            c: Context?,
            useCase: LocationUseCase,
        ): LocationViewModel = ViewModelProvider(
            owner,
            object: ViewModelProvider.Factory {
                @Suppress(SuppressLiteral.UNCHECKED_CAST)
                override fun <T : ViewModel?> create(modelClass: Class<T>): T = LocationViewModel(
                    c, useCase
                ) as T
            }
        ).get(LocationViewModel::class.java)
    }

    val locationList: LiveData<List<Location>> get()= mLocationList
    private val mLocationList = MutableLiveData<List<Location>>()

    val onSaveCurrentLocation: LiveData<Boolean> get()= mOnSaveCurrentLocation
    private val mOnSaveCurrentLocation = MutableLiveData<Boolean>()

    fun getLocations(forceLoad: Boolean = false) {
        if(locationList.value != null && !forceLoad) return
        startJob(Const.KEY_LOCATION_LIST) {
            when(val res = useCase.getAllLocation()) {
                is Success -> mLocationList.postValue(res.data)
                is Fail -> doCallNotSuccess(res.code, res.error)
            }
        }
    }

    fun saveCurrentLocations(location: Location) {
        startJob(Const.KEY_SAVE_CURRENT_LOC) {
            when(useCase.saveCurrentLocation(location)) {
                is Success -> mOnSaveCurrentLocation.postValue(true)
                is Fail -> mOnSaveCurrentLocation.postValue(false)
            }
        }
    }
}