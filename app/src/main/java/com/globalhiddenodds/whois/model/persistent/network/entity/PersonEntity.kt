package com.globalhiddenodds.whois.model.persistent.network.entity

import com.globalhiddenodds.whois.domain.data.Person

data class PersonEntity(private var id: Int,
                        private var name: String,
                        private var document: String) {
    fun toPerson() = Person(id, name, document)
}