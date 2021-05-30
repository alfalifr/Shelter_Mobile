package sidev.app.bangkit.capstone.sheltermobile.core.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sidev.app.bangkit.capstone.sheltermobile.core.data.entity.ReportEntity

@Dao
interface ReportDao {
    @Query("SELECT * FROM report")
    fun getAllReportList(): List<ReportEntity>

    @Query("SELECT * FROM report LIMIT :top")
    fun getTopReportList(top: Int): List<ReportEntity>

    @Query("SELECT * FROM report WHERE timestamp = :timestamp")
    fun getReport(timestamp: String): ReportEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReport(data: ReportEntity): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReportList(list: List<ReportEntity>): Int
}