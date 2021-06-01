package sidev.app.bangkit.capstone.sheltermobile.core.data.entity

import androidx.room.Entity
import java.sql.Timestamp

@Entity(tableName = "warning", primaryKeys = ["timestamp", "locationId", "disasterId"])
data class WarningEntity(
    val timestamp: String,
    val locationId: Int,
    val disasterId: Int,
    val emergencyId: Int,
    val title: String,
    val desc: String,
    val imgLink: String,
    val relatedNewsTimestamp: String,
)


/*
{
  disaster: 1
  emergency: 3
  title: "Anda terancam kebakar"
  date: "2021-07-14"
  location:
}
{
  disaster: {
     id: 1,
     name: "Forest Fire"
  }
  emergency: {
     id: 3,
     name: "",

  }
  title: "Anda terancam kebakar"
  date: "2021-07-14"
  location:
}
 */