package sidev.app.bangkit.capstone.sheltermobile.core.domain.repo

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Emergency

interface EmergencyRepo {
    suspend fun getAllEmergencies(): Result<List<Emergency>>
    suspend fun getEmergency(id: Int): Result<Emergency>
    /**
     * Returns saved count.
     */
    suspend fun saveEmergencyList(list: List<Emergency>): Result<Int>
}