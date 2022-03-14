package blck

import blck.domain.MessageDto
import blck.service.MessageService
import com.github.blck.khtmling.htmling
import javax.enterprise.context.ApplicationScoped
import javax.websocket.*
import javax.websocket.server.PathParam
import javax.websocket.server.ServerEndpoint

@ApplicationScoped
@ServerEndpoint("/chat/{name}")
class ChatSocket(private val messageService: MessageService) {

    private val sessions: MutableMap<String, Session> = mutableMapOf()

    @OnOpen
    fun onOpen(session: Session?, @PathParam("name") name: String) {
        println("enter> $name")
        session?.also {
            sessions[name] = it
            messageService.findAll().forEach { message -> it.sendMessage(message) }
            val msg = messageService.createSystemMessage("$name joined")
            broadcastMessage(msg)
        }
    }

    @OnClose
    fun onClose(session: Session?, @PathParam("name") name: String) {
        println("exit> $name")
        session?.also {
            sessions -= name
            val msg = messageService.createSystemMessage("$name exit")
            broadcastMessage(msg)
        }
    }

    @OnError
    fun onError(session: Session?, @PathParam("name") name: String, throwable: Throwable) {
        println("error> $name: $throwable")
    }

    @OnMessage
    fun onMessage(message: String, @PathParam("name") name: String) {
        println("message> $name")
        val msg = messageService.save(name, message)
        broadcastMessage(msg)
    }

    private fun broadcastMessage(message: MessageDto) =
        sessions.map { it.value }.forEach {
            it.sendMessage(message)
        }

    private fun Session.sendMessage(message: MessageDto) =
        asyncRemote.sendObject(message.htmling()) {
            if (it.isOK) {
                println("message sent")
            } else {
                println("message error: ${it.exception}")
            }
        }

}
