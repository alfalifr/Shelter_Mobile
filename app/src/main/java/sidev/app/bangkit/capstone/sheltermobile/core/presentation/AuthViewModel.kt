package sidev.app.bangkit.capstone.sheltermobile.core.presentation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.AuthData
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase.UserUseCase

class AuthViewModel(app: Application?, private val useCase: UserUseCase): AsyncVm(app) {
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
}