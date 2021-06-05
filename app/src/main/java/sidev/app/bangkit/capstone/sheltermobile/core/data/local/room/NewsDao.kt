package sidev.app.bangkit.capstone.sheltermobile.core.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sidev.app.bangkit.capstone.sheltermobile.core.data.entity.NewsEntity
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const

@Dao
interface NewsDao {
    @Query("SELECT * FROM news WHERE type = ${Const.TYPE_NEWS} AND timestamp >= :startTimestamp")
    fun getNewsList(startTimestamp: Long): List<NewsEntity>

    @Query("SELECT * FROM news WHERE type = ${Const.TYPE_ARTICLE} AND timestamp >= :startTimestamp")
    fun getArticleList(startTimestamp: Long): List<NewsEntity>

    @Query("SELECT * FROM news WHERE type = ${Const.TYPE_ARTICLE} AND timestamp = :timestamp")
    fun getArticle(timestamp: Long): NewsEntity?

    @Query("SELECT * FROM news WHERE type = ${Const.TYPE_NEWS} AND timestamp = :timestamp")
    fun getNews(timestamp: Long): NewsEntity?

    @Query("SELECT * FROM news WHERE timestamp >= :startTimestamp AND (title LIKE '%' || :keyword || '%' OR briefDesc LIKE '%' || :keyword || '%')")
    fun searchNews(keyword: String, startTimestamp: Long): List<NewsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveNews(data: NewsEntity): Long

    fun saveNewsList(list: List<NewsEntity>): Int {
        var inserted = 0
        for(e in list) {
            if(saveNews(e) >= 0)
                inserted++
        }
        return inserted
    }
}