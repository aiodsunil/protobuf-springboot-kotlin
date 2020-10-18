
package com.aiod.kafka.service

import com.aiod.kafka.dto.Person
import com.aiod.kafka.producer.PersonProducer
import org.springframework.stereotype.Service


@Service
class PersonServiceImpl(private val personProducer: PersonProducer) : PersonService {

    override fun save(person: Person) {
        personProducer.publishPersonCreatedEvent(person)
    }
}
