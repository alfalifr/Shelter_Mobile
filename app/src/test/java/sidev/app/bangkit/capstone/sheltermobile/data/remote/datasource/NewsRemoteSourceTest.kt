package sidev.app.bangkit.capstone.sheltermobile.data.remote.datasource

import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.AppRetrofit
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.NewsRemoteSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.NewsRemoteSourceImpl
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.News
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.lib.console.prin

class NewsRemoteSourceTest {
    companion object {
        val src: NewsRemoteSource by lazy { NewsRemoteSourceImpl(AppRetrofit.newsApi) }
    }

    private lateinit var result: Result<List<News>>

    @Test
    fun _1_getNews(){
        runBlocking {
            result = src.getNewsList("")
        }
    }
    @Test
    fun _2_getArticles(){
        runBlocking {
            result = src.getArticleList("")
        }
    }

    @After
    fun after(){
        assert(result is Success)

        val data = (result as Success).data
        prin("data = $data")

        assert(data.isNotEmpty())

        data.random().apply {
            assert(title.isNotBlank())
            assert(briefDesc.isNotBlank())
            assert(linkImage.isNotBlank())
            assert(link.isNotBlank())
            assert(timestamp.time.isNotBlank())
        }
    }
}