package com.globalhiddenodds.whois.domain.data

import com.globalhiddenodds.whois.presentation.extension.empty

data class Face(var id: Int,
                var name: String,
                var document: String,
                var base64: String,
                var latitude: Double,
                var longitude: Double,
                var date: Long,
                var state: Int) {
    companion object{
        fun empty() = Face(0, String.empty(),
            String.empty(), String.empty(),
            0.0, 0.0, 0, 0)
    }
}