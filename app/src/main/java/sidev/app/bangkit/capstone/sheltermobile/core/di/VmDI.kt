package sidev.app.bangkit.capstone.sheltermobile.core.di

import android.app.Application
import androidx.lifecycle.ViewModelStoreOwner
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel.AuthViewModel

object VmDI {
    fun getAuthViewModel(owner: ViewModelStoreOwner, app: Application?): AuthViewModel
}