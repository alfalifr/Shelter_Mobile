package sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.News
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase.NewsUseCase
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

class SearchArticleViewModel(app: Application?, private val useCase: NewsUseCase): AsyncVm(app) {
    val searchResult: LiveData<List<News>> get()= mSearchResult
    private val mSearchResult = MutableLiveData<List<News>>()

    private var currentKeyword: String = ""

    fun search(keyword: String, forceLoad: Boolean = false) {
        if(keyword == currentKeyword && !forceLoad) return
        currentKeyword = keyword
        cancelJob()
        doOnPreAsyncTask()
        job = GlobalScope.launch(Dispatchers.IO) {
            val timestamp = Util.getTimestamp()
            when(val result = useCase.searchNews(keyword, timestamp)){
                is Success -> mSearchResult.postValue(result.data)
                is Fail -> doCallNotSuccess(result.code, result.error)
            }
        }
    }
}