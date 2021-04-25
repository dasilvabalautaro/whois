package com.globalhiddenodds.whois.presentation.plataform

import android.content.Context
import com.globalhiddenodds.whois.presentation.extension.networkInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkHandler @Inject constructor(private val context: Context) {
    val isConnected get() = context.networkInfo != null
}