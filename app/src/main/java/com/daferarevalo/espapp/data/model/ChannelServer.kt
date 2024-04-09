package com.daferarevalo.espapp.data.model

import java.io.Serializable

data class ChannelServer(
    var name: String = "",
    var number: Int = 0,
    val activar: Boolean = false,
    var state: Boolean = false
): Serializable