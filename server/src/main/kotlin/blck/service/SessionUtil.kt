package blck.service

import blck.domain.MessageDto
import com.github.blck.khtmling.htmling
import javax.websocket.Session

fun Map<String, Session>.broadcastMessage(message: MessageDto) =
    map { it.value }.forEach {
        it.sendMessage(message)
    }

fun Session.sendMessage(message: MessageDto) =
    asyncRemote.sendObject(message.htmling()) {
        if (it.isOK) {
            println("message sent")
        } else {
            println("message error: ${it.exception}")
        }
    }
