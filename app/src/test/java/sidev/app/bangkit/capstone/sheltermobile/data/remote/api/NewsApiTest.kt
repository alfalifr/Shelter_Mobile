package sidev.app.bangkit.capstone.sheltermobile.data.remote.api

import org.junit.After
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import sidev.app.bangkit.capstone.sheltermobile.DummyTest
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.AppRetrofit
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.NewsApi
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.NewsBody
import sidev.lib.console.prin

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class NewsApiTest {
    companion object {
        val api: NewsApi by lazy { AppRetrofit.newsApi }
    }

    private lateinit var body: NewsBody

    @Test
    fun _1_getNews(){
        body = DummyTest.newsBody
    }
    @Test
    fun _2_getArticles(){
        body = DummyTest.articleBody
    }

    @After
    fun after(){
        val call = api.getNews(body)
        val resp = call.execute()

        assert(resp.isSuccessful)

        val data = resp.body()!!
        prin("NEWS data = $data")

        assert(data.isNotEmpty())

        data.random().apply {
            assert(title.isNotBlank())
            assert(desc.isNotBlank())
            assert(imgLink.isNotBlank())
            assert(link.isNotBlank())
            assert(timestamp.isNotBlank())
            assert(id.isNotBlank())
        }
    }
}