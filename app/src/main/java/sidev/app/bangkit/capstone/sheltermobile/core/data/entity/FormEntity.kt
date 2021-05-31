package sidev.app.bangkit.capstone.sheltermobile.core.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import java.sql.Timestamp

@Entity(tableName = "form")
data class FormEntity(
    @PrimaryKey
    val timestamp: Timestamp,
    //val id: Int,
    val title: String,
    val desc: String,
    /**
     * Can contains multiple link. If so, links are separated by [Const.CHAR_LINK_SEPARATOR]
     */
    val photoLinkList: String,
)