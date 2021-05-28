package sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Report
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase.ReportUseCase

class ReportViewModel(app: Application?, private val useCase: ReportUseCase): AsyncVm(app) {
    val reportHistory: LiveData<List<Report>> get()= mReportHistory
    private val mReportHistory = MutableLiveData<List<Report>>()

    fun getReportList(top: Int = -1, forceLoad: Boolean = false) {
        if (reportHistory.value != null && !forceLoad) return
        cancelJob()
        doOnPreAsyncTask()
        job = GlobalScope.launch(Dispatchers.IO) {
            when(val result = useCase.getReportList()){
                is Success -> mReportHistory.postValue(if(top <= 0) result.data else result.data.subList(0, top))
                is Fail -> doCallNotSuccess(result.code, result.error)
            }
        }
    }
}