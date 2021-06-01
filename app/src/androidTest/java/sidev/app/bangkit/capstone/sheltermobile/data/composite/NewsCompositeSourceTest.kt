package sidev.app.bangkit.capstone.sheltermobile.data.composite

import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import sidev.app.bangkit.capstone.sheltermobile.core.data.composite.EmergencyCompositeSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.composite.NewsCompositeSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.dummy.Dummy
import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.EmergencyLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.NewsLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.EmergencyRemoteSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.NewsRemoteSource
import sidev.app.bangkit.capstone.sheltermobile.core.di.ConfigDi
import sidev.app.bangkit.capstone.sheltermobile.core.di.RepoDi
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.News
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.console.prin
import sidev.lib.console.prine

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class NewsCompositeSourceTest {
    companion object {
        val compositeSrc: NewsCompositeSource by lazy { RepoDi.getNewsRepo() }
        val localSrc: NewsLocalSource by lazy { RepoDi.Local.getNewsSrc() }
        val remoteSrc: NewsRemoteSource by lazy { RepoDi.Remote.getNewsSrc() }
/*
        val dummyList = Dummy.emergencyList
        val dummyItem = dummyList.random()
 */
        val timestamp = ""
        private lateinit var dataList: List<News>

        @BeforeClass
        @JvmStatic
        fun classSetup(){
            ConfigDi.defaultCtx = ApplicationProvider.getApplicationContext()
            ConfigDi.isTest = true
        }

        @AfterClass
        @JvmStatic
        fun classFinish(){
            ConfigDi.defaultCtx = null
            ConfigDi.isTest = false
        }
    }

    @Test
    fun _1_getArticleList(){
        runBlocking {
            val fromLocal1 = localSrc.getArticleList(timestamp)
            assert(fromLocal1 is Success)
            assert((fromLocal1 as Success).data.isEmpty())

            val fromRemote = remoteSrc.getArticleList(timestamp)
            loge("fromRemote= $fromRemote")

            assert(fromRemote is Success)

            val list = (fromRemote as Success).data
            assert(list.isNotEmpty())

            val randomItem = list.random()
            randomItem.apply {
                assert(title.isNotBlank())
                assert(briefDesc.isNotBlank())
                assert(link.isNotBlank())
                assert(linkImage.isNotBlank())
                assert(timestamp.time.isNotBlank())
            }

            dataList = list

            val fromComposite = compositeSrc.getArticleList(timestamp)
            assert(fromComposite is Success)
            assertEquals(fromRemote, fromComposite)

            val fromLocal2 = localSrc.getArticleList(timestamp)
            assert(fromLocal2 is Success)
            val dataFromLocal2 = (fromLocal2 as Success).data
            assert((fromComposite as Success).data.containsAll(dataFromLocal2))
            //assertEquals((fromComposite as Success).data, (fromLocal2 as Success).data)
        }
    }

    @Test
    fun _2_getEmergency(){
        runBlocking {
            val randomItem = dataList.random()

            val fromComposite = compositeSrc.getArticle(randomItem.timestamp.time)
            assert(fromComposite is Success)
            assertEquals(randomItem, (fromComposite as Success).data)

            val fromLocal = localSrc.getArticle(randomItem.timestamp.time)
            assert(fromLocal is Success)
            assertEquals(randomItem, (fromLocal as Success).data)
        }
    }
}