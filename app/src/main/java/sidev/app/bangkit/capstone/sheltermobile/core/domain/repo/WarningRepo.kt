package sidev.app.bangkit.capstone.sheltermobile.core.domain.repo

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Coordinate
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Disaster
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningDetail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningStatus

interface WarningRepo {
    suspend fun getWarningStatus(disasterId: String, coordinate: Coordinate, timestamp: String): Result<WarningStatus>
    suspend fun getWarningDetail(disasterId: String, coordinate: Coordinate, timestamp: String): Result<WarningDetail>

    suspend fun getWarningStatusBatch(disasterId: String, coordinate: Coordinate, timestamp: String): Result<List<WarningStatus>>
    suspend fun getWarningDetailBatch(disasterId: String, coordinate: Coordinate, timestamp: String): Result<List<WarningDetail>>

    suspend fun saveWarningDetailList(list: List<WarningDetail>): Result<Int>

    /**
     * For warning status on map.
     */
    suspend fun getWarningStatusList(
        disasterId: String,
        coordinate: Coordinate,
        timestampFrom: String,
        timestampTo: String,
    ): Result<List<WarningStatus>>
}