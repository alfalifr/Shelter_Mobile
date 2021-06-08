package sidev.app.bangkit.capstone.sheltermobile.core.util

import android.app.AlarmManager

object Const {
    object Disaster {
        const val EARTH_QUAKE = "Gempa"
        const val LANDSLIDE = "Longsor"
        const val FLOOD = "Banjir"
        const val FOREST_FIRE = "Kebakaran Hutan"
        const val FOREST_FIRE_SHORT = "Karhutla"
    }
    object Emergency {
        const val SEVERITY_GREEN = 0
        const val SEVERITY_YELLOW = 1
        const val SEVERITY_RED = 2

        const val LANDSLIDE_NORMAL = "sedang"
        const val LANDSLIDE_BIT_DANGER = "agak rawan"
        const val LANDSLIDE_DANGER = "rawan"
        const val LANDSLIDE_VERY_DANGER = "sangat rawan"

        val FIRE_FOREST_GREEN = 0 .. 30
        val FIRE_FOREST_YELLOW = 31 .. 80
        val FIRE_FOREST_RED = 81 .. 100
///*
        //TODO Alif 4 Juni 2021: Tanya ttg skala keparahan akibat gempa
        val EARTH_QUAKE_GREEN = 0.0 .. 3.9 //3.0
        val EARTH_QUAKE_YELLOW = 3.9 .. 5.9
        val EARTH_QUAKE_RED = 5.9 .. 100.0

        val FLOOD_GREEN = "rendah"
        val FLOOD_YELLOW = "tinggi"
        val FLOOD_RED = "ekstrim"
// */
    }

    const val PKG_APP_MAIN = "sidev.app.bangkit.capstone.sheltermobile"
    const val ACTION_ALARM_NOTIF = "$PKG_APP_MAIN.NOTIF"
    const val ACTION_ALARM_NOTIF_ACT = "$ACTION_ALARM_NOTIF.Activity"

    const val TYPE_NEWS = 1
    const val TYPE_ARTICLE = 2

    const val METHOD_CALL = 1
    const val METHOD_FORM = 2

    const val CODE_WAIT_FOR_ACT_RES = 1
    const val CHAR_LINK_SEPARATOR = '|'
    const val REGEX_EMAIL_STR = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]+$" //"^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$"
    val REGEX_EMAIL = REGEX_EMAIL_STR.toRegex()

    const val TIME_DAY_OFFSET: Long = 24L * 3600 * 1000
    const val TIME_STANDARD_OFFSET: Long = TIME_DAY_OFFSET * 6L
    const val TIME_OFFSET: Long = 30L * 24L * 3600 * 1000

    const val PREFIX_DRAWABLE = "R.drawable."
    const val KEY_DATA = "DATA"
    const val KEY_TITLE = "title"
    const val KEY_DESC = "desc"
    const val KEY_PASSWORD = "password"
    const val KEY_LOCATION_ID = "location_id"
    const val KEY_USER_EMAIL = "user_email"
    const val KEY_USER_ID = "user_id"
    const val KEY_EMAIL = "email"
    const val KEY_ID = "id"
    const val KEY_COORDINATE = "coordinate"
    const val KEY_TYPE = "type"
    const val KEY_TIMESTAMP = "timestamp"
    const val KEY_TOP = "top"
    const val KEY_DISASTER_ID = "disaster_id"
    const val KEY_NO_NEWS = "no_news"
    const val KEY_IS_SINGLE = "is_single"
    const val KEY_URL = "url"
    const val SHARED_PREF_NAME = "_shared_pref_"

    const val KEY_CURRENT_LOC = "current_location"
    const val KEY_LOCATION_LIST = "location_list"
    const val KEY_SAVE_CURRENT_LOC = "save_current_loc"
    const val KEY_SAVE_CURRENT_USER = "save_current_user"
    const val KEY_WEATHER_FORECAST = "getWeatherForecast"
    const val KEY_WARNING_HIGHLIGHT = "getHighlightedWarningStatus"
    const val KEY_DISASTER_GROUP_LIST = "getDisasterGroupList"
    const val KEY_SIGNUP = "signup"
    const val KEY_LOGIN = "login"
    const val KEY_LOGOUT = "logout"
    const val KEY_LOGIN_STATUS = "login_status"
    const val KEY_ARTICLE_LIST = "getArticleList"
    const val KEY_NEWS_LIST = "getNewsList"
    const val KEY_CURRENT_USER = "getCurrentUser"
    const val KEY_CHANGE_PSWD = "changePassword"
    const val KEY_REPORT_LIST = "getReportList"
    const val KEY_REPORT_DETAIL = "getReportDetail"
    const val KEY_SEND_REPORT = "sendReport"
    const val KEY_SEARCH_NEWS = "search"

    const val CODE_USER_NOT_FOUND = 101
    const val CODE_BAD_PARAMETER = 102
    const val CODE_USER_ALREADY_REGISTERED = 103
    const val CODE_REGIS_SUCCESS = 104
    const val CODE_REGIS_FAIL = 105

    const val REQ_CALL = 2

    const val INT_TOP_REPORT_LIMIT = 5


    const val GENDER_MALE = 'M'
    const val GENDER_FEMALE = 'F'

    const val DAY_PATTERN = "EEEE"
    const val VIEW_DATE_PATTERN = "dd MMMM yyyy"
    const val VIEW_DATE_PATTERN_WITH_DAY = "$DAY_PATTERN, $VIEW_DATE_PATTERN"
    const val DB_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss"
    const val REMOTE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss"

    const val INTERVAL_2_WEEKS = AlarmManager.INTERVAL_DAY * 14

    //const val LEN_FORM_TITLE = 30
    //const val LEN_FORM_DESC = 300


    const val numberTextSatgas = "1231231231231231211" //"0618468469"

    const val API_ROOT = "http://35.240.165.229/API/v1/"
    const val API_SHELTER = "shelter_api.php"

    /*
    =====================
    Mella
    =====================
     */


}