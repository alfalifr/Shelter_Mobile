package sidev.app.bangkit.capstone.sheltermobile.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp

@Parcelize
data class WarningStatus(
    val disaster: Disaster,
    val emergency: Emergency,
    val title: String,
    val timestamp: String,
    val location: Location,
    val imgLink: String,
): Parcelable


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