package blck

import blck.service.MessageService
import blck.service.broadcastMessage
import blck.service.sendMessage
import javax.enterprise.context.ApplicationScoped
import javax.websocket.*
import javax.websocket.server.PathParam
import javax.websocket.server.ServerEndpoint

@ApplicationScoped
@Suppress("unused")
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
            sessions.broadcastMessage(msg)
        }
    }

    @OnClose
    fun onClose(session: Session?, @PathParam("name") name: String) {
        println("exit> $name")
        session?.also {
            sessions -= name
            val msg = messageService.createSystemMessage("$name exit")
            sessions.broadcastMessage(msg)
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
        sessions.broadcastMessage(msg)
    }

}
