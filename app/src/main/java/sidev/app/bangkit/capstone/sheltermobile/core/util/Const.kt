package sidev.app.bangkit.capstone.sheltermobile.core.util

object Const {
    const val TYPE_NEWS = 1
    const val TYPE_ARTICLE = 2

    const val METHOD_CALL = 1
    const val METHOD_FORM = 2

    const val CHAR_LINK_SEPARATOR = '|'

    const val TIME_OFFSET: Long = 30L * 24L * 3600 * 1000
    const val PREFIX_DRAWABLE = "R.drawable."
    const val KEY_PASSWORD = "password"
    const val KEY_LOCATION_ID = "location_id"
    const val KEY_USER_EMAIL = "user_email"
    const val KEY_EMAIL = "email"
    const val KEY_ID = "id"
    const val KEY_COORDINATE = "coordinate"
    const val KEY_TYPE = "type"
    const val KEY_TIMESTAMP = "timestamp"
    const val KEY_TOP = "top"
    const val KEY_DISASTER_ID = "disaster_id"
    const val KEY_IS_SINGLE = "is_single"
    const val SHARED_PREF_NAME = "_shared_pref_"

    const val KEY_CURRENT_LOC = "current_location"
    const val KEY_WEATHER_FORECAST = "getWeatherForecast"
    const val KEY_WARNING_HIGHLIGHT = "getHighlightedWarningStatus"
    const val KEY_DISASTER_GROUP_LIST = "getDisasterGroupList"
    const val KEY_SIGNUP = "signup"
    const val KEY_LOGIN = "login"
    const val KEY_LOGIN_STATUS = "login_status"
    const val KEY_ARTICLE_LIST = "getArticleList"
    const val KEY_NEWS_LIST = "getNewsList"
    const val KEY_CURRENT_USER = "getCurrentUser"
    const val KEY_CHANGE_PSWD = "changePassword"
    const val KEY_REPORT_LIST = "getReportList"
    const val KEY_SEND_REPORT = "sendReport"
    const val KEY_SEARCH_NEWS = "search"

    const val CODE_USER_NOT_FOUND = 101
    const val CODE_BAD_PARAMETER = 102
    const val CODE_USER_ALREADY_REGISTERED = 103
    const val CODE_REGIS_SUCCESS = 104
    const val CODE_REGIS_FAIL = 105


    const val GENDER_MALE = 'M'
    const val GENDER_FEMALE = 'F'

    const val DAY_PATTERN = "EEEE"
    const val VIEW_DATE_PATTERN = "dd MMMM yyyy"
    const val VIEW_DATE_PATTERN_WITH_DAY = "$DAY_PATTERN, $VIEW_DATE_PATTERN"
    const val DB_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS"
    const val REMOTE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss"

    //const val LEN_FORM_TITLE = 30
    //const val LEN_FORM_DESC = 300

    const val API_ROOT = "http://35.240.165.229/API/v1/"

    /*
    =====================
    Mella
    =====================
     */


}