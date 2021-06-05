package sidev.app.bangkit.capstone.sheltermobile.core.domain.repo

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.*

interface WarningRepo {
/*
    suspend fun getWarningStatus(disasterId: Int, coordinate: Coordinate, timestamp: String): Result<WarningStatus>
    suspend fun getWarningDetail(disasterId: Int, coordinate: Coordinate, timestamp: String): Result<WarningDetail>

    suspend fun getWarningStatusBatch(disasterId: Int, coordinate: Coordinate, timestamp: String): Result<List<WarningStatus>>
    suspend fun getWarningDetailBatch(disasterId: Int, coordinate: Coordinate, timestamp: String): Result<List<WarningDetail>>
 */
    suspend fun getWarningStatusBatch(disasterId: Int, locationId: Int, startTimestamp: TimeString): Result<List<WarningStatus>>
    suspend fun getWarningDetailBatch(disasterId: Int, locationId: Int, startTimestamp: TimeString): Result<List<WarningDetail>>

    suspend fun saveWarningDetailList(list: List<WarningDetail>): Result<Int>
/*
    /**
     * For warning status on map.
     */
    suspend fun getWarningStatusList(
        disasterId: String,
        coordinate: Coordinate,
        timestampFrom: String,
        timestampTo: String,
    ): Result<List<WarningStatus>>
 */
}