package com.daferarevalo.espapp.data.model

import java.io.Serializable

data class DatetimeServer(
    var channel: Int = -1,
    var state: Boolean = false,
    var eventNum: Int = 1,
    var hour: Int = 12,
    var min: Int = 0,
    var action: Boolean = false,
    var sun: Boolean = false,
    var mon: Boolean = false,
    var thurs: Boolean = false,
    var wend: Boolean = false,
    var tues: Boolean = false,
    var fri: Boolean = false,
    var satu: Boolean = false
) : Serializable