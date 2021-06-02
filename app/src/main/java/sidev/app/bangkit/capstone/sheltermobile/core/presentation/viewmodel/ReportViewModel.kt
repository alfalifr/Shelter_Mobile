package sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.runBlocking
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Report
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase.ReportUseCase
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.lib.`val`.SuppressLiteral
import java.lang.IllegalStateException

class ReportViewModel(app: Context?, private val useCase: ReportUseCase): AsyncVm(app) {
    companion object {
        fun getInstance(
            owner: ViewModelStoreOwner,
            app: Context?,
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

    val onSend: LiveData<Boolean> get()= mOnSend
    private val mOnSend = MutableLiveData<Boolean>()

    fun getCurrentLocation(): Location = runBlocking {
        when(val res = useCase.getCurrentLocation()) {
            is Success -> res.data
            else -> throw IllegalStateException("Error when getting the current location.")
        }
    }

    fun getReportList(top: Int = 5, forceLoad: Boolean = false) {
        if (reportHistory.value != null && !forceLoad) return
        startJob(Const.KEY_REPORT_LIST) {
            when(val result = useCase.getReportList()){
                is Success -> mReportHistory.postValue(if(top <= 0 || result.data.size < top) result.data else result.data.subList(0, top))
                is Fail -> doCallNotSuccess(result.code, result.error)
            }
        }
    }

    fun sendReport(data: Report){
        startJob(Const.KEY_SEND_REPORT) {
            //useCase. TODO ALIF: 31 Mei 2021
        }
    }
}