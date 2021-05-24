package sidev.app.bangkit.capstone.sheltermobile.data.repo

import sidev.app.bangkit.capstone.sheltermobile.data.model.Coordinate
import sidev.app.bangkit.capstone.sheltermobile.data.model.WarningDetail
import sidev.app.bangkit.capstone.sheltermobile.data.model.WarningStatus

interface WarningRepo {
    suspend fun getWarningStatus(disasterId: String, coordinate: Coordinate, timestamp: String): WarningStatus
    suspend fun getWarningDetail(disasterId: String, coordinate: Coordinate, timestamp: String): WarningDetail

    /**
     * For warning status on map.
     */
    suspend fun getWarningStatusList(
        disasterId: String,
        coordinate: Coordinate,
        timestampFrom: String,
        timestampTo: String,
    ): List<WarningStatus>
}