package com.daferarevalo.espapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.daferarevalo.espapp.core.Result
import com.daferarevalo.espapp.data.model.ChannelServer
import com.daferarevalo.espapp.data.model.DatetimeServer
import com.daferarevalo.espapp.domain.home.HomeRepo
import kotlinx.coroutines.Dispatchers

class HomeViewModel(private val repo: HomeRepo):ViewModel() {
    fun addChannelModel(channel:ChannelServer)= liveData(viewModelScope.coroutineContext + Dispatchers.Main){
        emit(Result.Loading())
        try {
            emit(Result.Success(repo.addChannel(channel)))
        }catch (e: Exception){
            emit(Result.Failure(e))
        }
    }

    fun checkChannelModel(channelPin:Int)= liveData(viewModelScope.coroutineContext + Dispatchers.Main){
        emit(Result.Loading())
        try {
            emit(Result.Success(repo.checkChannel(channelPin)))
        }catch (e: Exception){
            emit(Result.Failure(e))
        }
    }

    fun checkNumberChannelsModel()= liveData(viewModelScope.coroutineContext + Dispatchers.Main){
        emit(Result.Loading())
        try {
            emit(Result.Success(repo.checkNumberChannels()))
        }catch (e: Exception){
            emit(Result.Failure(e))
        }
    }

    fun turnChannelModel(channelPin:Int,stateChannel:Boolean)= liveData(viewModelScope.coroutineContext + Dispatchers.Main){
        emit(Result.Loading())
        try {
            emit(Result.Success(repo.turnChannel(channelPin,stateChannel)))
        }catch (e: Exception){
            emit(Result.Failure(e))
        }
    }

    fun getDefinedChannelsModel() = liveData(viewModelScope.coroutineContext + Dispatchers.Main){
        emit(Result.Loading())
        try{
            emit(Result.Success(repo.getDefinedChannels()))
        }catch (e: Exception){
            emit(Result.Failure(e))
        }
    }

    fun readSensors() = liveData(viewModelScope.coroutineContext + Dispatchers.Main){
        emit(Result.Loading())
        try{
            emit(Result.Success(repo.readSensors()))
        }catch (e: Exception){
            emit(Result.Failure(e))
        }
    }

    fun getDefinedTimersModel(channel:Int) = liveData(viewModelScope.coroutineContext + Dispatchers.Main){
        emit(Result.Loading())
        try{
            emit(Result.Success(repo.getDefinedTimers(channel)))
        }catch (e: Exception){
            emit(Result.Failure(e))
        }
    }

    fun addTimerModel(enableTimers:Int) = liveData(viewModelScope.coroutineContext + Dispatchers.Main){
        emit(Result.Loading())
        try{
            emit(Result.Success(repo.addTimer(enableTimers)))
        }catch (e: Exception){
            emit(Result.Failure(e))
        }
    }

    fun setTimerModel(timerData: DatetimeServer) = liveData(viewModelScope.coroutineContext + Dispatchers.Main){
        emit(Result.Loading())
        try{
            emit(Result.Success(repo.setTimer(timerData)))
        }catch (e: Exception){
            emit(Result.Failure(e))
        }
    }
}

class HomeViewModelFactory(private val repo: HomeRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(HomeRepo::class.java).newInstance(repo)
    }
}