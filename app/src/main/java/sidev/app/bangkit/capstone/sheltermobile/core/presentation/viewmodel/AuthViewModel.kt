package sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.AuthData
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase.UserUseCase
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.lib.`val`.SuppressLiteral

class AuthViewModel(app: Context?, private val useCase: UserUseCase): AsyncVm(app) {
    companion object {
        fun getInstance(
            owner: ViewModelStoreOwner,
            app: Context?,
            useCase: UserUseCase,
        ): AuthViewModel = ViewModelProvider(
            owner,
            object: ViewModelProvider.Factory {
                @Suppress(SuppressLiteral.UNCHECKED_CAST)
                override fun <T : ViewModel?> create(modelClass: Class<T>): T = AuthViewModel(
                    app, useCase
                ) as T
            }
        ).get(AuthViewModel::class.java)
    }

    val currentLocation: LiveData<Location> get()= mCurrentLocation
    private val mCurrentLocation = MutableLiveData<Location>()

    val onAuth: LiveData<Boolean> get()= mOnAuth
    private val mOnAuth = MutableLiveData<Boolean>()

    fun signup(user: User, authData: AuthData) {
        startJob(Const.KEY_SIGNUP) {
            when(useCase.signup(user, authData)){
                is Success -> mOnAuth.postValue(true)
                is Fail -> mOnAuth.postValue(false)
            }
        }
    }

    fun login(authData: AuthData) {
        startJob(Const.KEY_LOGIN) {
            when(useCase.login(authData)){
                is Success -> mOnAuth.postValue(true)
                is Fail -> mOnAuth.postValue(false)
            }
        }
    }

    fun checkLoginStatus(){
        startJob(Const.KEY_LOGIN_STATUS) {
            if(useCase.getCurrentUser() is Success){
                mOnAuth.postValue(true)
            }
        }
    }

    fun getCurrentLocation(forceLoad: Boolean = false) {
        if (mCurrentLocation.value != null && !forceLoad) return
        startJob(Const.KEY_CURRENT_LOC) {
            when(val result = useCase.getCurrentLocation()){
                is Success -> mCurrentLocation.postValue(result.data)
                is Fail -> doCallNotSuccess(Const.KEY_CURRENT_LOC, result.code, result.error)
            }
        }
    }
}