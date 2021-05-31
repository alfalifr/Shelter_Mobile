package sidev.app.bangkit.capstone.sheltermobile.core.data.entity

import java.sql.Timestamp
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const

data class FormEntity(
    val timestamp: Timestamp,
    val title: String,
    val desc: String,
    /**
     * Can contains multiple link. If so, links are separated by [Const.CHAR_LINK_SEPARATOR]
     */
    val photoLinkList: String,
)