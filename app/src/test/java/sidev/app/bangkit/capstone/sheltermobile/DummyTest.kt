package sidev.app.bangkit.capstone.sheltermobile

import sidev.app.bangkit.capstone.sheltermobile.core.data.dummy.Dummy
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.EarthQuakeBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.LandslideBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.NewsBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.WeatherForecastBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.*
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.AuthData
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

object DummyTest {
    const val MSG_SUCCESS = "success"
    const val MSG_FAILED = "failed"

    val dummyTimeString = Util.getTimeString()

    //Login
    //{"response":"success","data":{"id":"2","email":"test111@mail.com","full_name":"test sata","gender":"M"}}
    //{"response":"failed","response_code":101}
    val userIndex = 1
    val loginBody = Dummy.getLoginBody(userIndex)
    val userData = Dummy.userList[userIndex]

    val loginDataResponse_success = userData.let {
        LoginDataResponse(1, it.email, it.name, it.gender)
    }
    val loginResponse_success = LoginResponse(MSG_SUCCESS, loginDataResponse_success)
    val loginResponse_fail = GeneralResponse(MSG_FAILED, Const.CODE_USER_NOT_FOUND)

    val userAuthData = AuthData(userData.email, Dummy.userPswd[userIndex])


    //Signup
    //{"response":"success","response_code":104}
    //{"response":"failed","response_code":103}
    val signupBody = Dummy.getSignupBody(userIndex)
    val signupResponse_success = GeneralResponse(MSG_SUCCESS, Const.CODE_REGIS_SUCCESS)
    val signupResponse_fail = GeneralResponse(MSG_FAILED, Const.CODE_REGIS_FAIL)


    val newsBody = NewsBody(Const.TYPE_NEWS.toString())
    val articleBody = NewsBody(Const.TYPE_ARTICLE.toString())

    val landslideBody = LandslideBody("Sungai Korang")
    val earthQuakeBody = EarthQuakeBody("Pulo Batal")

    val landslideResponse = LandslideResponse("Agak Rawan", "Sungai Korang") //{"kondisi":"Agak Rawan","lat":"100.1147994996","lon":"1.0629122256","lokasi":"Sungai Korang"}
    val earthQuakeResponse = EarthQuakeResponse("2021-01-01 00:00:00", "3.44573412879278".toDouble(), "Pulo Batal")

    val fireForestCityFirstRaw = "Asahan"
    val earthQuakeCityResponsesRaw = listOf(
        listOf("Pulo Batal"),
        listOf("Batangtoru"),
        listOf("Kuala Baru"),
        listOf("Barus"),
        listOf("Muara Sipongi"),
        listOf("Sampuran"),
        listOf("Parlilitan"),
        listOf("Pintupadang"),
        listOf("Muara Soma"),
        listOf("Teluk Dalam"),
        listOf("Suak Bakung"),
        listOf("Lotu"),
        listOf("Dolok Sanggul"),
        listOf("Onan Ganjang Satu"),
        listOf("Siabu"),
        listOf("Kotanopan"),
        listOf("Pandan"),
        listOf("Sibolga"),
        listOf("Lintongnihuta"),
        listOf("Pakkat"),
        listOf("Siborong-Borong"),
        listOf("Sibuhuan"),
        listOf("Panti"),
        listOf("Parmonangan")
    )

    val earthQuakeCityResponses = earthQuakeCityResponsesRaw.map { GeneralCityResponse(it) }


    const val weatherMinDate = "2021-06-05 03:35:17"
    const val weatherMaxDate = "2021-06-11 03:35:17"

    val weatherForecastBody = WeatherForecastBody(weatherMinDate, weatherMaxDate)

    val weatherForecastResponse = WeatherForecastResponse(
        "2021-06-06 00:00:00", //weatherMinDate,
        windSpeed = "2.35409512149502".toDouble(),
        humidity = "93.2299787751501".toDouble(),
        rainfall = "3.9550015413852".toDouble(),
        ultraviolet = "3.62638400630404".toDouble(),
        temperature = "26.3031779426394".toDouble(),
        weatherName = "Mendung",
    )
}