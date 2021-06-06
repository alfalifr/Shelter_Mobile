package sidev.app.bangkit.capstone.sheltermobile.data.remote.datasource

import kotlinx.coroutines.runBlocking
import org.junit.Test
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.AppRetrofit
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.LocationApi
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.LocationRemoteSource
import sidev.app.bangkit.capstone.sheltermobile.core.di.RepoDi
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.lib.collection.countDuplication
import sidev.lib.collection.countDuplicationBy
import sidev.lib.console.prin
import java.util.*

class LocationRemoteSourceTest {
    companion object {
        //val api: LocationApi by lazy { AppRetrofit.locationApi }
        val src: LocationRemoteSource by lazy { RepoDi.Remote.getLocationSrc() }
    }


    @Test
    fun getAllLocs(){
        runBlocking {
            val res = src.getAllLocation()
            prin("res = $res")

            assert(res is Success)

            val list = (res as Success).data

            assert(list.isNotEmpty())
            val dups = list.countDuplicationBy { it.name.toLowerCase(Locale.ROOT) }
            prin("dups= $dups")

            val dupsMore = dups.filterValues { it >= 1 }
            prin("dupsMore= $dupsMore")
        }
    }
}