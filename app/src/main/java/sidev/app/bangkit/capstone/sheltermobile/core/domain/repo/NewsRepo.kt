package sidev.app.bangkit.capstone.sheltermobile.core.domain.repo

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.News

interface NewsRepo {
    suspend fun getNewsList(timestamp: String, page: Int): Result<List<News>>
    suspend fun getArticleList(timestamp: String, page: Int): Result<List<News>>
    suspend fun getArticle(timestamp: String): Result<News>
    suspend fun getNews(timestamp: String): Result<News>

    suspend fun saveNewsList(list: List<News>): Result<Int>
}