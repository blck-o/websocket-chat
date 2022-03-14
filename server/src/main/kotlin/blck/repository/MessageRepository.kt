package blck.repository

import blck.domain.Message
import io.quarkus.mongodb.panache.kotlin.PanacheMongoRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class MessageRepository : PanacheMongoRepository<Message>
