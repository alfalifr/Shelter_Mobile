package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.dummy.Dummy
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Disaster
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

object DisasterRemoteSourceDummy: DisasterRemoteSource {
    override suspend fun getDisasterList(): Result<List<Disaster>> = Success(Dummy.disasterList, 0)

    override suspend fun getDisaster(id: Int): Result<Disaster> = Dummy.disasterList.find { it.id == id }
        ?.let { Success(it, 0) } ?: Util.noEntityFailResult()

    /**
     * Returns saved count.
     */
    override suspend fun saveDisasterList(list: List<Disaster>): Result<Int> = Util.operationNotAvailableFailResult()
}