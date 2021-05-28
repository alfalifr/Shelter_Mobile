package sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase.UserUseCase

class ProfileViewModel(app: Application?, private val useCase: UserUseCase): AsyncVm(app) {
    val currentUser: LiveData<User> get()= mCurrentUser
    private val mCurrentUser = MutableLiveData<User>()

    val currentLocation: LiveData<Location> get()= mCurrentLocation
    private val mCurrentLocation = MutableLiveData<Location>()

    val changePasswordStatus: LiveData<Boolean> get()= mChangePasswordStatus
    private val mChangePasswordStatus = MutableLiveData<Boolean>()


    fun getCurrentUser(forceLoad: Boolean = false){
        if(mCurrentUser.value != null && !forceLoad) return
        cancelJob()
        doOnPreAsyncTask()
        job = GlobalScope.launch(Dispatchers.IO) {
            when(val result = useCase.getCurrentUser()){
                is Success -> mCurrentUser.postValue(result.data)
                is Fail -> doCallNotSuccess(result.code, result.error)
            }
        }
    }

    fun getCurrentLocation(forceLoad: Boolean = false) {
        if (mCurrentLocation.value != null && !forceLoad) return
        cancelJob()
        doOnPreAsyncTask()
        job = GlobalScope.launch(Dispatchers.IO) {
            when(val result = useCase.getCurrentLocation()){
                is Success -> mCurrentLocation.postValue(result.data)
                is Fail -> doCallNotSuccess(result.code, result.error)
            }
        }
    }

    fun changePassword(newPswd: String) {
        cancelJob()
        doOnPreAsyncTask()
        job = GlobalScope.launch(Dispatchers.IO) {
            when(val oldPswdResult = useCase.getPassword()){
                is Success -> {
                    val old = oldPswdResult.data
                    when(val result = useCase.changePassword(old, newPswd)){
                        is Success -> mChangePasswordStatus.postValue(true)
                        is Fail -> doCallNotSuccess(result.code, result.error)
                    }
                }
                is Fail -> doCallNotSuccess(oldPswdResult.code, oldPswdResult.error)
            }
        }
    }
}