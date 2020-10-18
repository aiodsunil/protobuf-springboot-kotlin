
package com.aiod.kafka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.messaging.Source
import org.springframework.cloud.stream.schema.client.EnableSchemaRegistryClient
import io.confluent.kafka.serializers.protobuf.KafkaProtobufSerializer

@EnableSchemaRegistryClient
@EnableBinding(Source::class)
@SpringBootApplication
class PersonServiceProducerApplication

fun main(args: Array<String>) {

    runApplication<PersonServiceProducerApplication>(*args)
}
