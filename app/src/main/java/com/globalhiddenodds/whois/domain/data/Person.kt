package com.globalhiddenodds.whois.domain.data

import com.globalhiddenodds.whois.presentation.extension.empty

data class Person(var id: Int,
                  var name: String,
                  var document: String) {
    companion object{
        fun empty() = Person(0, String.empty(),
            String.empty())
    }
}