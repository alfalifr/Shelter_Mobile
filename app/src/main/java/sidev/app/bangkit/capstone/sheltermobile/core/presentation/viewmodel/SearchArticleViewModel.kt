package sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.News
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase.NewsUseCase
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.lib.`val`.SuppressLiteral

class SearchArticleViewModel(app: Context?, private val useCase: NewsUseCase): AsyncVm(app) {
    companion object {
        fun getInstance(
            owner: ViewModelStoreOwner,
            app: Context?,
            useCase: NewsUseCase,
        ): SearchArticleViewModel = ViewModelProvider(
            owner,
            object: ViewModelProvider.Factory {
                @Suppress(SuppressLiteral.UNCHECKED_CAST)
                override fun <T : ViewModel?> create(modelClass: Class<T>): T = SearchArticleViewModel(
                    app, useCase
                ) as T
            }
        ).get(SearchArticleViewModel::class.java)
    }

    val searchResult: LiveData<List<News>> get()= mSearchResult
    private val mSearchResult = MutableLiveData<List<News>>()

    private var currentKeyword: String = ""

    fun search(keyword: String, forceLoad: Boolean = false) {
        if(keyword == currentKeyword && !forceLoad) return
        currentKeyword = keyword
        startJob(Const.KEY_SEARCH_NEWS) {
            val timestamp = Util.getTimeString()
            when(val result = useCase.searchNews(keyword, timestamp)){
                is Success -> mSearchResult.postValue(result.data)
                is Fail -> doCallNotSuccess(Const.KEY_SEARCH_NEWS, result.code, result.error)
            }
        }
    }
}