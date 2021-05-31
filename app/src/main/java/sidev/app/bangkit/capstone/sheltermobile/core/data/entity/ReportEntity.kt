package sidev.app.bangkit.capstone.sheltermobile.core.data.entity

import androidx.room.Entity
import java.sql.Timestamp

@Entity(tableName = "report")
data class ReportEntity(
    val timestamp: Timestamp,
    val method: Int, //1 for call, 2 for form
    val response: String,
    val locationId: Int,
    //val formEntityId: Int, //-1 if this Report doesn't have form, like `method` is 1 for call.
)