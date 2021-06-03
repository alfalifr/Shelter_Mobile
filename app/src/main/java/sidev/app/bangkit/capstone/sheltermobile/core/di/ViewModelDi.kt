package sidev.app.bangkit.capstone.sheltermobile.core.di

import android.app.Application
import androidx.lifecycle.ViewModelStoreOwner
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel.*

object ViewModelDi {
    fun getAuthViewModel(owner: ViewModelStoreOwner): AuthViewModel = AuthViewModel.getInstance(
        owner, AppDi.getContext(), UseCaseDi.getUserUseCase()
    )
    fun getDashboardViewModel(owner: ViewModelStoreOwner): DashboardViewModel = DashboardViewModel.getInstance(
        owner, AppDi.getContext(), UseCaseDi.getDashbosrdUseCase()
    )
    fun getNewsViewModel(owner: ViewModelStoreOwner): NewsViewModel = NewsViewModel.getInstance(
        owner, AppDi.getContext(), UseCaseDi.getNewsUseCase()
    )
    fun getProfileViewModel(owner: ViewModelStoreOwner): ProfileViewModel = ProfileViewModel.getInstance(
        owner, AppDi.getContext(), UseCaseDi.getUserUseCase()
    )
    fun getLocationViewModel(owner: ViewModelStoreOwner): LocationViewModel = LocationViewModel.getInstance(
        owner, AppDi.getContext(), UseCaseDi.getLocationUseCase()
    )
    fun getReportViewModel(owner: ViewModelStoreOwner): ReportViewModel = ReportViewModel.getInstance(
        owner, AppDi.getContext(), UseCaseDi.getReportUseCase()
    )
    fun getSearchArticleViewModel(owner: ViewModelStoreOwner): SearchArticleViewModel = SearchArticleViewModel.getInstance(
        owner, AppDi.getContext(), UseCaseDi.getNewsUseCase()
    )
}