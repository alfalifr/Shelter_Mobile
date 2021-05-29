package sidev.app.bangkit.capstone.sheltermobile.core.data.entity

import androidx.room.Entity
import java.sql.Timestamp

@Entity(tableName = "report")
data class ReportEntity(
    val timestamp: Timestamp,
    val method: Int, //1 for call, 2 for form
    val response: String,
)