package sidev.app.bangkit.capstone.sheltermobile.core.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "report")
data class ReportEntity(
    @PrimaryKey
    val timestamp: Long,
    val method: Int, //1 for call, 2 for form
    val response: String,
    val locationId: Int,
    val userId: Int,
    //val formId: Int, //-1 if this Report doesn't have form, like `method` is 1 for call.
)