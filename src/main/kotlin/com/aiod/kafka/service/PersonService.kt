

package com.aiod.kafka.service

import com.aiod.kafka.dto.Person


interface PersonService {

    fun save(person: Person)

}
