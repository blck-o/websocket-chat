package blck.service

import blck.domain.Message
import blck.domain.MessageDto
import blck.domain.toDto
import blck.repository.MessageRepository
import java.time.LocalDateTime
import javax.enterprise.context.ApplicationScoped

interface MessageService {

    fun findAll(): List<MessageDto>

    fun save(username: String, message: String): MessageDto

    fun createSystemMessage(message: String): MessageDto

    @ApplicationScoped
    class Base(private val messageRepository: MessageRepository) : MessageService {

        override fun findAll(): List<MessageDto> =
            messageRepository.findAll()
                .list()
                .map { it.toDto() }

        override fun save(username: String, message: String): MessageDto {
            val msg = createMessage(username, message)
            messageRepository.persist(msg)
            return msg.toDto()
        }

        override fun createSystemMessage(message: String): MessageDto {
            val msg = createMessage("_system", message)
            messageRepository.persist(msg)
            return msg.toDto()
        }

        private fun createMessage(username: String, message: String): Message {
            val msg = Message()
            msg.username = username
            msg.text = message.trim()
            msg.sendAt = LocalDateTime.now()
            return msg
        }
    }

}
