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

class NewsViewModel(app: Context?, private val useCase: NewsUseCase): AsyncVm(app) {
    companion object {
        fun getInstance(
            owner: ViewModelStoreOwner,
            app: Context?,
            useCase: NewsUseCase,
        ): NewsViewModel = ViewModelProvider(
            owner,
            object: ViewModelProvider.Factory {
                @Suppress(SuppressLiteral.UNCHECKED_CAST)
                override fun <T : ViewModel?> create(modelClass: Class<T>): T = NewsViewModel(
                    app, useCase
                ) as T
            }
        ).get(NewsViewModel::class.java)
    }

    val newsList: LiveData<List<News>> get()= mNewsList
    private val mNewsList = MutableLiveData<List<News>>()

    val articleList: LiveData<List<News>> get()= mArticleList
    private val mArticleList = MutableLiveData<List<News>>()


    fun getArticleList(page: Int = 1, forceLoad: Boolean = false){
        if(articleList.value != null && !forceLoad) return
        startJob(Const.KEY_ARTICLE_LIST) {
            val timestamp = Util.getTimeString()
            when(val result = useCase.getArticleList(timestamp)){
                is Success -> mArticleList.postValue(result.data)
                is Fail -> doCallNotSuccess(Const.KEY_ARTICLE_LIST, result.code, result.error)
            }
        }
    }
    fun getNewsList(page: Int = 1, forceLoad: Boolean = false){
        if(newsList.value != null && !forceLoad) return
        startJob(Const.KEY_NEWS_LIST) {
            val timestamp = Util.getTimeString()
            when(val result = useCase.getNewsList(timestamp)){
                is Success -> mNewsList.postValue(result.data)
                is Fail -> doCallNotSuccess(Const.KEY_NEWS_LIST, result.code, result.error)
            }
        }
    }
}