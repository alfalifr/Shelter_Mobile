package sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.room.NewsDao
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.News
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.TimeString
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toEntity
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

class NewsLocalSourceImpl(private val dao: NewsDao): NewsLocalSource {
    override suspend fun getNewsList(startTimestamp: TimeString): Result<List<News>> {
        val list = dao.getNewsList(startTimestamp.timeLong).map { it.toModel() }
        return Success(list, 0)
    }

    override suspend fun getArticleList(startTimestamp: TimeString): Result<List<News>> {
        val list = dao.getArticleList(startTimestamp.timeLong).map { it.toModel() }
        return Success(list, 0)
    }

    override suspend fun getArticle(timestamp: TimeString): Result<News> {
        val result = dao.getArticle(timestamp.timeLong)?.toModel() ?: return Util.noEntityFailResult()
        return Success(result, 0)
    }

    override suspend fun getNews(timestamp: TimeString): Result<News> {
        val result = dao.getNews(timestamp.timeLong)?.toModel() ?: return Util.noEntityFailResult()
        return Success(result, 0)
    }

    override suspend fun searchNews(keyword: String, startTimestamp: TimeString): Result<List<News>> {
        val list = dao.searchNews(keyword, startTimestamp.timeLong).map { it.toModel() }
        return Success(list, 0)
    }

    override suspend fun saveNewsList(list: List<News>): Result<Int> {
        val entityList = list.map { it.toEntity() }
        val insertedCount = dao.saveNewsList(entityList)
        return Util.getInsertResult(insertedCount, entityList.size)
    }
}