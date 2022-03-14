package blck.domain

import com.github.blck.khtmling.HtmlIgnore
import com.github.blck.khtmling.HtmlProperty
import com.github.blck.khtmling.HtmlStyle
import com.github.blck.khtmling.enums.CssProperty
import com.github.blck.khtmling.enums.HtmlTag
import io.quarkus.mongodb.panache.common.MongoEntity
import org.bson.types.ObjectId
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@MongoEntity(collection = "messages")
class Message {

    var id: ObjectId? = null
    lateinit var username: String
    lateinit var text: String
    lateinit var sendAt: LocalDateTime

}

@HtmlProperty(
    name = "message",
    tag = HtmlTag.DIV,
    styles = [
        HtmlStyle(CssProperty.BORDER, "4px dotted brown"),
        HtmlStyle(CssProperty.MARGIN, "5px")
    ]
)
data class MessageDto(
    @HtmlIgnore val id: String,
    @HtmlProperty(tag = HtmlTag.H2) val username: String,
    @HtmlProperty(styles = [HtmlStyle(CssProperty.FONT_STYLE, "italic")]) val text: String,
    @HtmlProperty(
        tag = HtmlTag.H5,
        styles = [HtmlStyle(CssProperty.COLOR, "blue")]
    ) val sendAt: String
)

fun Message.toDto(): MessageDto =
    MessageDto(id.toString(), username, text, sendAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
