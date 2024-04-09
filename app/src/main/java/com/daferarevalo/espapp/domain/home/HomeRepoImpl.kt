package com.daferarevalo.espapp.domain.home

import com.daferarevalo.espapp.data.model.ChannelServer
import com.daferarevalo.espapp.data.model.DatetimeServer
import com.daferarevalo.espapp.data.remote.home.HomeDataSource

class HomeRepoImpl(private val dataSource: HomeDataSource):HomeRepo {
    override suspend fun addChannel(channel: ChannelServer) = dataSource.addChannel(channel)
    override suspend fun checkChannel(channelPin: Int): ChannelServer =dataSource.checkChannel(channelPin)
    override suspend fun checkNumberChannels(): Int = dataSource.checkNumberChannels()
    override suspend fun turnChannel(channelPin: Int, stateChannel: Boolean) = dataSource.turnChannel(channelPin,stateChannel)

    override suspend fun updateChannel(channelPin: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun readSensors(): List<Float> = dataSource.readSensors()

    override suspend fun getDefinedChannels(): List<ChannelServer> = dataSource.getDefinedChannels()
    override suspend fun getDefinedTimers(channel:Int): List<DatetimeServer> = dataSource.getDefinedTimers(channel)
    override suspend fun setTimer(timerData: DatetimeServer) = dataSource.setTimer(timerData)

    override suspend fun addTimer(enableTimers: Int) = dataSource.addTimer(enableTimers)


}