package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request

data class NewsBody(
    override val _requestType: String, //1 for news, 2 for article
): GeneralRequestBody