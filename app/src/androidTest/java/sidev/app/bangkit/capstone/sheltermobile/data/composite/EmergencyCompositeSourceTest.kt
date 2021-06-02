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
import sidev.app.bangkit.capstone.sheltermobile.core.data.dummy.Dummy
import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.EmergencyLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.EmergencyRemoteSource
import sidev.app.bangkit.capstone.sheltermobile.core.di.ConfigDi
import sidev.app.bangkit.capstone.sheltermobile.core.di.RepoDi
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.console.prin
import sidev.lib.console.prine

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class EmergencyCompositeSourceTest {
    companion object {
        val compositeSrc: EmergencyCompositeSource by lazy { RepoDi.getEmergencyRepo() }
        val localSrc: EmergencyLocalSource by lazy { RepoDi.Local.getEmergencySrc() }
        val remoteSrc: EmergencyRemoteSource by lazy { RepoDi.Remote.getEmergencySrc() }

        val dummyList = Dummy.emergencyList
        val dummyItem = dummyList.random()

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
    fun _1_getAllEmergencies(){
        runBlocking {
            val fromLocal1 = localSrc.getAllEmergencies()
            assert(fromLocal1 is Success)
            assert((fromLocal1 as Success).data.isEmpty())

            val fromRemote = remoteSrc.getAllEmergencies()
            assert(fromRemote is Success)
            assertEquals(dummyList, (fromRemote as Success).data)

            loge("fromRemote= $fromRemote")

            val fromComposite = compositeSrc.getAllEmergencies()
            assert(fromComposite is Success)
            assertEquals(fromRemote, fromComposite)

            val fromLocal2 = localSrc.getAllEmergencies()
            assert(fromLocal2 is Success)
            assertEquals((fromComposite as Success).data, (fromLocal2 as Success).data)

            loge("fromLocal2= $fromLocal2")
        }
    }

    @Test
    fun _2_getEmergency(){
        runBlocking {
            val fromComposite = compositeSrc.getEmergency(dummyItem.id)
            assert(fromComposite is Success)
            assertEquals(dummyItem, (fromComposite as Success).data)

            loge("fromComposite= $fromComposite")

            val fromLocal = localSrc.getEmergency(dummyItem.id)
            assert(fromLocal is Success)
            assertEquals(dummyItem, (fromLocal as Success).data)

            loge("fromLocal= $fromLocal")
        }
    }
}