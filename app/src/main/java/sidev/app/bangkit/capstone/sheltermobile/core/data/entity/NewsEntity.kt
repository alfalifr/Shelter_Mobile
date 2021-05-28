package sidev.app.bangkit.capstone.sheltermobile.core.data.entity

import androidx.room.Entity
import java.sql.Timestamp

@Entity(tableName = "news", primaryKeys = ["timestamp", "type"])
data class NewsEntity(
    val timestamp: Timestamp,
    val title: String,
    val briefDesc: String,
    val linkImage: String,
    val link: String,
    val type: Int, //1 for news, 2 for article
)