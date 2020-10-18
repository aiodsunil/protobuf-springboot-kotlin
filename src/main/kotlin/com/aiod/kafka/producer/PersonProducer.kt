
package com.aiod.kafka.producer


import com.aiod.kafka.dto.Person
import io.aiod.kafka.PersonProto
import org.springframework.cloud.stream.messaging.Source
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.support.GenericMessage
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component


@Component
class PersonProducer(private val source: Source) {

    fun publishPersonCreatedEvent(person: Person) {
        		val person1: PersonProto.Person=PersonProto.Person.newBuilder().setId(person.id).setName(person.name).setAge(person.age).build()

        val message: GenericMessage<Any> = GenericMessage(person1)
        source.output().send(message);
//        source.output().send(MessageBuilder.withPayload(PersonProto.Person.newBuilder().setId(person.id).setName(person.name).setAge(person.age).build())
//                .setHeader(KafkaHeaders.MESSAGE_KEY, person.id).build())
    }

}
