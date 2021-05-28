package sidev.app.bangkit.capstone.sheltermobile.core.domain.repo

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Disaster

interface DisasterRepo {
    suspend fun getDisasterList(): Result<List<Disaster>>
    suspend fun getDisaster(id: Int): Result<Disaster>

    /**
     * Returns saved count.
     */
    suspend fun saveDisasterList(list: List<Disaster>): Result<Int>
}