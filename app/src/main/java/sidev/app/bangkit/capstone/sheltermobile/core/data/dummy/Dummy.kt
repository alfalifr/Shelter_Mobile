package sidev.app.bangkit.capstone.sheltermobile.core.data.dummy

import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.LoginBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.SignupBody
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.*
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

object Dummy {
    val userList = listOf<User>(
        User("b@b.b", "bayu", 'M'),
        User("a@a.a", "ayu", 'M'),
    )

    val userPswd = listOf<String>(
        "abc123",
        "a1",
    )

    fun getLoginBody(i: Int): LoginBody = LoginBody(userList[i].email, userPswd[i])
    fun getSignupBody(i: Int): SignupBody = userList[i].let { SignupBody(it.email, userPswd[i], it.name, it.gender) }

    val disasterList = listOf<Disaster>(
        Disaster(1, "Kebakaran Hutan"),
        Disaster(2, "Banjir"),
        Disaster(3, "Longsor"),
        Disaster(4, "Gunung Meletus"),
    )

    val emergencyList = listOf<Emergency>(
        Emergency(1, "Hijau", "#32a83e", 0),
        Emergency(2, "Kuning", "#e6de09", 1),
        Emergency(3, "Merah", "#e61809", 2),
    )

    val locationList = listOf<Location>(
        Location(1, "Medan", Coordinate(0.0,0.0)),
        Location(2, "Palembang", Coordinate(0.0,0.0)),
        Location(3, "Bandung", Coordinate(0.0,0.0)),
        //Location(4, "Surabaya", Coordinate(0.0,0.0)),
        //Location(5, "Jakart", Coordinate(0.0,0.0)),
    )

    val newsList = listOf<News>(
        News(Util.getTimestamp(), "Ini CLickbait", "Ini juga sama aja", "https://i.pinimg.com/originals/e1/1b/f8/e11bf8a6206dda58d61dfc004b813516.jpg", "https://i.pinimg.com/originals/e1/1b/f8/e11bf8a6206dda58d61dfc004b813516.jpg", Const.TYPE_NEWS),
        News(Util.getTimestamp(), "Ini CLickbait 2", "Ini ya gitu", "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/1a052c60-b6a8-476f-87d9-04b7f5f0f903/d52ha02-7eebab18-a875-4185-94de-6fd44be0206a.jpg/v1/fill/w_900,h_675,q_75,strp/smiling_cat_by_omniamohamed_d52ha02-fullview.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9Njc1IiwicGF0aCI6IlwvZlwvMWEwNTJjNjAtYjZhOC00NzZmLTg3ZDktMDRiN2Y1ZjBmOTAzXC9kNTJoYTAyLTdlZWJhYjE4LWE4NzUtNDE4NS05NGRlLTZmZDQ0YmUwMjA2YS5qcGciLCJ3aWR0aCI6Ijw9OTAwIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmltYWdlLm9wZXJhdGlvbnMiXX0.uEgwgBfq7faRJ9GPWtsJLmc8_kqG11HHAfdtNX5j56A", "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/1a052c60-b6a8-476f-87d9-04b7f5f0f903/d52ha02-7eebab18-a875-4185-94de-6fd44be0206a.jpg/v1/fill/w_900,h_675,q_75,strp/smiling_cat_by_omniamohamed_d52ha02-fullview.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9Njc1IiwicGF0aCI6IlwvZlwvMWEwNTJjNjAtYjZhOC00NzZmLTg3ZDktMDRiN2Y1ZjBmOTAzXC9kNTJoYTAyLTdlZWJhYjE4LWE4NzUtNDE4NS05NGRlLTZmZDQ0YmUwMjA2YS5qcGciLCJ3aWR0aCI6Ijw9OTAwIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmltYWdlLm9wZXJhdGlvbnMiXX0.uEgwgBfq7faRJ9GPWtsJLmc8_kqG11HHAfdtNX5j56A", Const.TYPE_ARTICLE)
    )

    val warningList1 = locationList[0].let { location ->
        listOf<WarningStatus>(
            WarningStatus(disasterList[0], emergencyList[1], "Mayan", Util.getTimestamp(), location, ""),
            WarningStatus(disasterList[0], emergencyList[2], "Hati2 Gosong", Util.getTimestamp(), location, ""),
            WarningStatus(disasterList[1], emergencyList[0], "Aman Bro", Util.getTimestamp(), location, ""),
            WarningStatus(disasterList[3], emergencyList[1], "Jangan lengah", Util.getTimestamp(), location, ""),
        )
    }
    val warningList2 = locationList[1].let { location ->
        listOf<WarningStatus>(
            WarningStatus(disasterList[0], emergencyList[0], "Tidur Nyenyak", Util.getTimestamp(), location, ""),
            WarningStatus(disasterList[0], emergencyList[1], "Ok", Util.getTimestamp(), location, ""),
            WarningStatus(disasterList[1], emergencyList[0], "Aman Bro", Util.getTimestamp(), location, ""),
            WarningStatus(disasterList[3], emergencyList[2], "Tidur tak sleeping", Util.getTimestamp(), location, ""),
        )
    }
    val warningList3 = locationList[2].let { location ->
        listOf<WarningStatus>(
            WarningStatus(disasterList[0], emergencyList[0], "Sante", Util.getTimestamp(), location, ""),
            WarningStatus(disasterList[0], emergencyList[1], "Ok", Util.getTimestamp(), location, ""),
            WarningStatus(disasterList[1], emergencyList[2], "Banjir kan datang", Util.getTimestamp(), location, ""),
            WarningStatus(disasterList[3], emergencyList[0], "Santuy", Util.getTimestamp(), location, ""),
            WarningStatus(disasterList[4], emergencyList[2], "Bumi mau marah", Util.getTimestamp(), location, ""),
        )
    }
    val warningListAll = warningList1 + warningList2 + warningList3

    val weatherList = listOf<WeatherForecast>(
        WeatherForecast(10f, 11f, 25f, 180f, 10f, Util.getTimestamp()),
        WeatherForecast(14f, 12f, 256f, 110f, 3012f, Util.getTimestamp()),
    )

    val formList = listOf<Form>(
        Form(1, "Ini judul 1", "Ini desc 1", emptyList()),
        Form(2, "Ini judul 2", "Ini desc 2", emptyList()),
    )


    fun getWarningStatusList(disasterId: Int, locationId: Int, startTimestamp: String): List<WarningStatus> = warningListAll.filter {
        it.disaster.id == disasterId && it.location.id == locationId
    }
    fun getWarningDetailList(disasterId: Int, locationId: Int, startTimestamp: String): List<WarningDetail> = warningListAll.filter {
        it.disaster.id == disasterId && it.location.id == locationId
    }.map { WarningDetail(it, "Ini deskripsi", newsList[0]) }

    fun <T> getEmptyListResult(): Success<List<T>> = Success(emptyList(), 0)
}