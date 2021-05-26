package sidev.app.bangkit.capstone.sheltermobile.core.domain.model

data class WarningStatus(
    val disaster: Disaster,
    val emergency: Emergency,
    val title: String,
    val date: String,
    val location: Location,
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