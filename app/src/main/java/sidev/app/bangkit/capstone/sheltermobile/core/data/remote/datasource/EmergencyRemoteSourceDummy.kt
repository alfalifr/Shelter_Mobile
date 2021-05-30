package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.dummy.Dummy
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Emergency
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

object EmergencyRemoteSourceDummy: EmergencyRemoteSource {
    override suspend fun getAllEmergencies(): Result<List<Emergency>> = Success(Dummy.emergencyList, 0)

    override suspend fun getEmergency(id: Int): Result<Emergency> = Dummy.emergencyList.find { it.id == id }
        ?.let { Success(it, 0) } ?: Util.noEntityFailResult()

    /**
     * Returns saved count.
     */
    override suspend fun saveEmergencyList(list: List<Emergency>): Result<Int> = Util.operationNotAvailableFailResult()
}