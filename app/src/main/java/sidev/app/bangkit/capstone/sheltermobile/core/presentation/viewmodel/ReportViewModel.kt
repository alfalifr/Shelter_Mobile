package sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.runBlocking
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Report
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.ReportDetail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase.ReportUseCase
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.lib.`val`.SuppressLiteral
import sidev.lib.android.std.tool.util.`fun`.loge
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

    val reportHistoryCount: LiveData<Int> get()= mReportHistoryCount
    private val mReportHistoryCount = MutableLiveData<Int>()

    val reportDetail: LiveData<ReportDetail> get()= mReportDetail
    private val mReportDetail = MutableLiveData<ReportDetail>()

    val onSend: LiveData<Boolean> get()= mOnSend
    private val mOnSend = MutableLiveData<Boolean>()

    val currentLocation: LiveData<Location> get()= mCurrentLocation
    private val mCurrentLocation = MutableLiveData<Location>()

    fun getCurrentLocation() {
        startJob(Const.KEY_CURRENT_LOC) {
            when(val res = useCase.getCurrentLocation()) {
                is Success -> mCurrentLocation.postValue(res.data)
                else -> doCallNotSuccess(-1, null) //throw IllegalStateException("Error when getting the current location.")
            }
        }
    }

    fun getReportList(top: Int = -1, forceLoad: Boolean = false) {
        if (reportHistory.value != null && !forceLoad) return
        startJob(Const.KEY_REPORT_LIST) {
            when(val result = useCase.getReportList().also { loge("ReportVm.getReportList() reportList= $it") }){
                is Success -> {
                    val list = result.data
                    mReportHistoryCount.postValue(list.size)
                    mReportHistory.postValue(
                        if (top <= 0 || list.size < top) list else list.subList(0, top)
                    )
                }
                is Fail -> doCallNotSuccess(result.code, result.error)
            }
        }
    }

    fun getReportDetail(timestamp: String, forceLoad: Boolean = false) {
        loge("Vm.getReportDetail() timestamp= $timestamp forceLoad= $forceLoad")
        if (reportDetail.value != null && !forceLoad) return
        startJob(Const.KEY_REPORT_DETAIL) {
            when(val res = useCase.getReportDetail(timestamp).also { loge("Vm.getReportDetail() useCase.getReportDetail res= $it") }) {
                is Success -> mReportDetail.postValue(res.data)
                is Fail -> doCallNotSuccess(res.code, res.error)
            }
        }
    }

    fun sendReport(data: Report){
        startJob(Const.KEY_SEND_REPORT) {
            when(useCase.sendReport(data)) {
                is Success -> mOnSend.postValue(true)
                is Fail -> mOnSend.postValue(false)
            }
        }
    }
}