package sidev.app.bangkit.capstone.sheltermobile.core.data.composite

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.NewsLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.NewsRemoteSource
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Coordinate
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.News
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.TimeString
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.LocationRepo
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.NewsRepo
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toListResult
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toSingleResult
import java.lang.IllegalArgumentException

class NewsCompositeSource(
    private val localSrc: NewsLocalSource,
    private val remoteSrc: NewsRemoteSource,
): CompositeDataSource<News>(), NewsRepo {
    override suspend fun getLocalDataList(args: Map<String, Any?>): Result<List<News>> = getDataList(localSrc, args)

    override suspend fun getRemoteDataList(args: Map<String, Any?>): Result<List<News>> = getDataList(remoteSrc, args)

    private suspend fun getDataList(repo: NewsRepo, args: Map<String, Any?>): Result<List<News>> {
        val type = args[Const.KEY_TYPE] as? Int ?: throw IllegalArgumentException("args[Const.KEY_TYPE] == null")
        val timestamp = args[Const.KEY_TIMESTAMP] as? TimeString ?: throw IllegalArgumentException("args[Const.KEY_TIMESTAMP] == null")
        val isSingle = args[Const.KEY_IS_SINGLE] as? Boolean ?: false

        return when(type){
            Const.TYPE_ARTICLE -> if(!isSingle) repo.getArticleList(timestamp) else repo.getArticle(timestamp).toListResult()
            Const.TYPE_NEWS -> if(!isSingle) repo.getNewsList(timestamp) else repo.getNews(timestamp).toListResult()
            else -> throw IllegalArgumentException("Unknown args[Const.KEY_TYPE] ($type)")
        }
    }

    override fun shouldFetch(localDataList: List<News>, args: Map<String, Any?>): Boolean = localDataList.isEmpty()

    override suspend fun saveDataList(remoteDataList: List<News>): Result<Int> = localSrc.saveNewsList(remoteDataList)


    override suspend fun getNewsList(startTimestamp: TimeString): Result<List<News>> = getDataList(mapOf(
        Const.KEY_TYPE to Const.TYPE_NEWS,
        Const.KEY_TIMESTAMP to startTimestamp,
    ))

    override suspend fun getArticleList(startTimestamp: TimeString): Result<List<News>> = getDataList(mapOf(
        Const.KEY_TYPE to Const.TYPE_ARTICLE,
        Const.KEY_TIMESTAMP to startTimestamp,
    ))

    override suspend fun getArticle(timestamp: TimeString): Result<News> = getDataList(mapOf(
        Const.KEY_TYPE to Const.TYPE_ARTICLE,
        Const.KEY_TIMESTAMP to timestamp,
        Const.KEY_IS_SINGLE to true,
    )).toSingleResult()

    override suspend fun getNews(timestamp: TimeString): Result<News> = getDataList(mapOf(
        Const.KEY_TYPE to Const.TYPE_NEWS,
        Const.KEY_TIMESTAMP to timestamp,
        Const.KEY_IS_SINGLE to true,
    )).toSingleResult()

    override suspend fun searchNews(keyword: String, startTimestamp: TimeString): Result<List<News>> = remoteSrc.searchNews(keyword, startTimestamp)

    override suspend fun saveNewsList(list: List<News>): Result<Int> = saveDataList(list)
}