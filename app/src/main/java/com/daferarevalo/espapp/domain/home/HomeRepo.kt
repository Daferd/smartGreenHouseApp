package com.daferarevalo.espapp.domain.home

import com.daferarevalo.espapp.data.model.ChannelServer
import com.daferarevalo.espapp.data.model.DatetimeServer
import com.daferarevalo.espapp.data.remote.home.HomeDataSource

interface HomeRepo {
    suspend fun addChannel(channel: ChannelServer)
    suspend fun checkChannel(channelPin: Int):ChannelServer
    suspend fun checkNumberChannels():Int

    suspend fun turnChannel(channelPin: Int, stateChannel:Boolean)

    suspend fun updateChannel(channelPin:Int)
    suspend fun getDefinedChannels():List<ChannelServer>
    suspend fun readSensors():List<Float>
    suspend fun getDefinedTimers(channel:Int):List<DatetimeServer>
    suspend fun setTimer(timerData: DatetimeServer)
    suspend fun addTimer(enableTimers: Int)
}