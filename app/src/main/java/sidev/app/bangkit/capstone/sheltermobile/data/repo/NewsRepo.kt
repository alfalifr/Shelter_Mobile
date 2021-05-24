package sidev.app.bangkit.capstone.sheltermobile.data.repo

import sidev.app.bangkit.capstone.sheltermobile.data.model.News
import java.sql.Timestamp

interface NewsRepo {
    suspend fun getNewsList(timestamp: String, page: Int): List<News>
}