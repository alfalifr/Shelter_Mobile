package sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import org.jetbrains.anko.runOnUiThread


/**
 * Template for all ViewModel in this project.
 * This class mimics [AndroidViewModel] but with optional [app] parameter for convinience in unit testing.
 */
@SuppressLint("StaticFieldLeak")
open class AsyncVm(app: Context?): ViewModel() {
    protected var ctx: Context? = app
        private set

    //private var isJobJoin = false
    protected var jobs: MutableMap<String, Job> = mutableMapOf()
/*
    protected var job: Job?= null
        set(v){
            if(!isJobJoin)
                field = v
        }
 */

    /**
     * Executed before any async task in `this` runs.
     */
    protected var onPreAsyncTask: ((key: String) -> Unit)?= null
    private var onCallNotSuccess: ((key: String, code: Int, e: Throwable?) -> Unit)?= null

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     *
     *
     * It is useful when ViewModel observes some data and you need to clear this subscription to
     * prevent a leak of this ViewModel.
     */
    @CallSuper
    override fun onCleared() {
        ctx = null //So there won't be a memory leak.
    }

    protected fun startJob(
        key: String,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) {
        jobs[key]?.cancel()
        doOnPreAsyncTask(key)
        GlobalScope.launch(dispatcher, start, block)
    }

    protected fun cancelJob(key: String){
        jobs[key]?.cancel()
    }

    fun onPreAsyncTask(f: ((key: String) -> Unit)?){
        onPreAsyncTask= f
    }
    fun onCallNotSuccess(f: ((key: String, code: Int, e: Throwable?) -> Unit)?){
        onCallNotSuccess= f
    }
    protected fun doOnPreAsyncTask(key: String){
        onPreAsyncTask?.also { ctx?.runOnUiThread { it(key) } }
    }
    protected fun doCallNotSuccess(key: String, code: Int, e: Throwable?){
        onCallNotSuccess?.also { ctx?.runOnUiThread { it(key, code, e) } }
    }
/*
    fun multipleJob(lazyBlock: () -> List<Job?>) {
        cancelJob()
        isJobJoin = false
        job = GlobalScope.launch(Dispatchers.IO) {
            isJobJoin = true
            val jobs = lazyBlock()
            if(jobs.isEmpty()) return@launch
            val firstJob = jobs.first()
            var prevJob = firstJob
            for(i in 1 until jobs.size) {
                val currJob = jobs[i]
                prevJob?.invokeOnCompletion { currJob?.start() }
                    ?: run {
                        currJob?.start()
                    }
                prevJob = currJob
            }
            firstJob?.start()
            isJobJoin = false
        }
    }
 */
}