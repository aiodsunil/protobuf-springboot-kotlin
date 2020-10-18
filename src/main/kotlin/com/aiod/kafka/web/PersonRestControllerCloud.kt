package com.aiod.kafka.web

import com.aiod.kafka.service.PersonService

import com.aiod.kafka.dto.Person
import io.aiod.kafka.PersonProto
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.protocol.Message
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/person")
class PersonRestControllerCloud(private val personService:PersonService) {

	@PostMapping
	fun save(@RequestBody person: Person) {

		return personService.save(person)
	}
	@GetMapping
	fun getPerson() {
		val props = Properties()

		props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
		props[ConsumerConfig.GROUP_ID_CONFIG] = "group1"
		props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = "org.apache.kafka.common.serialization.StringDeserializer"
		props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = "io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializer"
		props["schema.registry.url"] = "http://localhost:8081"
		props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"

		val topic = "test-1"
		val consumer: Consumer<String, Message> = KafkaConsumer<String, Message>(props)
		consumer.subscribe(Arrays.asList(topic))

		try {
			while (true) {
				val records: ConsumerRecords<String?, Message?> = consumer.poll(100)
				for (record in records) {
					System.out.printf("offset = %d, key = %s, value = %s \n", record.offset(), record.key(), record.value())
				}
			}
		} finally {
			consumer.close()
		}
	}
}
