package sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase.NewsUseCase
import sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase.UserUseCase
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.lib.`val`.SuppressLiteral

class ProfileViewModel(app: Context?, private val useCase: UserUseCase): AsyncVm(app) {
    companion object {
        fun getInstance(
            owner: ViewModelStoreOwner,
            app: Context?,
            useCase: UserUseCase,
        ): ProfileViewModel = ViewModelProvider(
            owner,
            object: ViewModelProvider.Factory {
                @Suppress(SuppressLiteral.UNCHECKED_CAST)
                override fun <T : ViewModel?> create(modelClass: Class<T>): T = ProfileViewModel(
                    app, useCase
                ) as T
            }
        ).get(ProfileViewModel::class.java)
    }

    val currentUser: LiveData<User> get()= mCurrentUser
    private val mCurrentUser = MutableLiveData<User>()

    val currentLocation: LiveData<Location> get()= mCurrentLocation
    private val mCurrentLocation = MutableLiveData<Location>()

    val changePasswordStatus: LiveData<Boolean> get()= mChangePasswordStatus
    private val mChangePasswordStatus = MutableLiveData<Boolean>()

    val onLogout: LiveData<Boolean> get()= mOnLogout
    private val mOnLogout = MutableLiveData<Boolean>()

    val onSaveProfile: LiveData<Boolean> get()= mOnSaveProfile
    private val mOnSaveProfile = MutableLiveData<Boolean>()


    fun getCurrentUser(forceLoad: Boolean = false){
        if(mCurrentUser.value != null && !forceLoad) return
        startJob(Const.KEY_CURRENT_USER) {
            when(val result = useCase.getCurrentUser()){
                is Success -> mCurrentUser.postValue(result.data)
                is Fail -> doCallNotSuccess(result.code, result.error)
            }
        }
    }

    fun saveCurrentUser(user: User, pswd: String?= null){
        startJob(Const.KEY_SAVE_CURRENT_USER) {
            when(useCase.saveCurrentProfile(user, pswd)) {
                is Success -> {
                    mCurrentUser.postValue(user)
                    mOnSaveProfile.postValue(true)
                }
                is Fail -> mOnSaveProfile.postValue(false)
            }
        }
    }

    fun getCurrentLocation(forceLoad: Boolean = false) {
        if (mCurrentLocation.value != null && !forceLoad) return
        startJob(Const.KEY_CURRENT_LOC) {
            when(val result = useCase.getCurrentLocation()){
                is Success -> mCurrentLocation.postValue(result.data)
                is Fail -> doCallNotSuccess(result.code, result.error)
            }
        }
    }

    fun changePassword(newPswd: String) {
        startJob(Const.KEY_CHANGE_PSWD) {
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

    fun logout() {
        startJob(Const.KEY_LOGOUT) {
            mOnLogout.postValue(useCase.logout() is Success)
        }
    }
}