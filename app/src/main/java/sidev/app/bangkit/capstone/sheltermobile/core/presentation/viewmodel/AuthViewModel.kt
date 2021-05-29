package sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.AuthData
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase.UserUseCase
import sidev.lib.`val`.SuppressLiteral

class AuthViewModel(app: Application?, private val useCase: UserUseCase): AsyncVm(app) {
    companion object {
        fun getInstance(
            owner: ViewModelStoreOwner,
            app: Application?,
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

    val onAuth: LiveData<Boolean> get()= mOnAuth
    private val mOnAuth = MutableLiveData<Boolean>()

    fun signup(user: User, authData: AuthData) {
        cancelJob()
        doOnPreAsyncTask()
        job = GlobalScope.launch(Dispatchers.IO) {
            when(useCase.signup(user, authData)){
                is Success -> mOnAuth.postValue(true)
                is Fail -> mOnAuth.postValue(false)
            }
        }
    }

    fun login(authData: AuthData) {
        cancelJob()
        doOnPreAsyncTask()
        job = GlobalScope.launch(Dispatchers.IO) {
            when(useCase.login(authData)){
                is Success -> mOnAuth.postValue(true)
                is Fail -> mOnAuth.postValue(false)
            }
        }
    }

    fun saveUser(user: User) {
        //TODO Alif
    }
}