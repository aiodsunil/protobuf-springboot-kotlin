package com.aiod.kafka.web

//import com.aiod.kafka.service.PersonService

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
@RequestMapping("/api/person")
class PersonRestController {

	@PostMapping
	fun save(@RequestBody person: Person) {
		val props = Properties()
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringSerializer")
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				"io.confluent.kafka.serializers.protobuf.KafkaProtobufSerializer")
		props.put("schema.registry.url", "http://127.0.0.1:8081")

		val producer: Producer<String, PersonProto.Person> = KafkaProducer<String, PersonProto.Person>(props)

		val topic = "testproto1"
		val key = "1234"
		val person1: PersonProto.Person=PersonProto.Person.newBuilder().setId(person.id).setName(person.name).setAge(person.age).build()


		val record: ProducerRecord<String, PersonProto.Person> = ProducerRecord<String, PersonProto.Person>(topic, key, person1)
		producer.send(record).get()
		producer.close()
	//	return person-service.save(person)
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

		val topic = "testproto1"
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
