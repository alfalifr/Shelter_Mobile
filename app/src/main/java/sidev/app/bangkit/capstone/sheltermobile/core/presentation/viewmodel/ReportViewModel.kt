package sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Report
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase.ReportUseCase
import sidev.lib.`val`.SuppressLiteral
import java.lang.IllegalStateException

class ReportViewModel(app: Application?, private val useCase: ReportUseCase): AsyncVm(app) {
    companion object {
        fun getInstance(
            owner: ViewModelStoreOwner,
            app: Application?,
            useCase: ReportUseCase,
        ): ReportViewModel = ViewModelProvider(
            owner,
            object: ViewModelProvider.Factory {
                @Suppress(SuppressLiteral.UNCHECKED_CAST)
                override fun <T : ViewModel?> create(modelClass: Class<T>): T = ReportViewModel(
                    app, useCase
                ) as T
            }
        ).get(ReportViewModel::class.java)
    }

    val reportHistory: LiveData<List<Report>> get()= mReportHistory
    private val mReportHistory = MutableLiveData<List<Report>>()

    fun getCurrentLocation(): Location = runBlocking {
        when(val res = useCase.getCurrentLocation()) {
            is Success -> res.data
            else -> throw IllegalStateException("Error when getting the current location.")
        }
    }

    fun getReportList(top: Int = 5, forceLoad: Boolean = false) {
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

    fun sendReport(data: Report){
        cancelJob()
        doOnPreAsyncTask()
        job = GlobalScope.launch(Dispatchers.IO) {
            //useCase. TODO ALIF: 31 Mei 2021
        }
    }
}