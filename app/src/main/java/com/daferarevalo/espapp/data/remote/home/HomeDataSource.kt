package com.daferarevalo.espapp.data.remote.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import com.daferarevalo.espapp.data.model.ChannelServer
import com.daferarevalo.espapp.data.model.DatetimeServer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class HomeDataSource{

    suspend fun addChannel(channel:ChannelServer) {
        withContext(Dispatchers.IO) {
            val user = FirebaseAuth.getInstance().currentUser

            user?.let {
                var db = FirebaseDatabase.getInstance()
                    .getReference("users/${user.uid}/channels/channel${channel.number+1}")
                val channel = ChannelServer(channel.name,channel.number+1,false, false)
                db.setValue(channel).await()

                /*db = FirebaseDatabase.getInstance()
                    .getReference("users/${user.uid}/channels/numberChannels")

                db.setValue(channel).await()*/
            }
        }
    }

    suspend fun checkChannel(channelPin: Int):ChannelServer{
        var channelState=ChannelServer()
        withContext(Dispatchers.IO){
            val user = FirebaseAuth.getInstance().currentUser

            user?.let {
                val database = FirebaseDatabase.getInstance().reference
                database.keepSynced(true)

                val state = database.child("users/${user.uid}/channels/channel${channelPin}").get().await()

                state.let { dato ->
                    channelState = dato.getValue(ChannelServer::class.java) as ChannelServer
                }
            }
        }
        return channelState
    }

    suspend fun checkNumberChannels():Int{
        var numberChannels:Int = 0
        withContext(Dispatchers.IO) {
            val user = FirebaseAuth.getInstance().currentUser
            user?.let {

                val db = FirebaseDatabase.getInstance().reference
                db.keepSynced(true)

                val state = db.child("users/${user.uid}/channels/numberChannels").get().await()

                state.let { dato->
                    numberChannels = dato.value as Int
                }
            }
        }
        return numberChannels
    }

    suspend fun turnChannel(channelPin: Int, stateChannel:Boolean){
        withContext(Dispatchers.IO) {
            val user = FirebaseAuth.getInstance().currentUser

            user?.let {
                var db = FirebaseDatabase.getInstance()
                    .getReference("users/${user.uid}/channels/channel${channelPin}/state")
                db.setValue(stateChannel).await()
            }
        }
    }

    suspend fun updateChanel(channelPin:Int):ChannelServer?{
        var channel=ChannelServer()
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val db = FirebaseDatabase.getInstance().getReference("users/${user.uid}/channels/channel${channelPin}").get().await()
            channel = db.getValue(ChannelServer::class.java)!!
            Log.d("channel","$channel")
        }
        return channel
    }

    suspend fun getDefinedChannels():List<ChannelServer>{
        //var timersListAux = mutableListOf<DatetimeServer>()
        val channelList = mutableListOf<ChannelServer>()
        //var enableTimers = 0
        withContext(Dispatchers.IO) {
            val user = FirebaseAuth.getInstance().currentUser
            user?.let {

                val db = FirebaseDatabase.getInstance().reference
                db.keepSynced(true)

                val ch = db.child("users/${user.uid}/channels").get().await()

                for(channel in ch.children){
                    channel.getValue(ChannelServer::class.java)?.let { channelList.add(it) }

                }

                //Organiza la lista de forma descendente y se cambia el valor de retorno por timerListAux
                //timersListAux= timersList.sortedBy { it.hour} as MutableList<DatetimeServer>
            }
        }
        return channelList
    }

    suspend fun readSensors():List<Float>{

        var sensorList = mutableListOf<Float>()

        withContext(Dispatchers.IO) {
            val user = FirebaseAuth.getInstance().currentUser
            user?.let {
                val db = FirebaseDatabase.getInstance().reference
                db.keepSynced(true)

                val sensorValues = db.child("users/${user.uid}/sensors").get().await()

                for(sensor in sensorValues.children){
                    sensor.getValue(Float::class.java)?.let { sensorList.add(it) }
                }
            }
        }
        return sensorList
    }

    suspend fun getDefinedTimers(channel:Int):List<DatetimeServer>{ // Se debe añadir un parametro entero que permita establecer la ruta exacta del canal que se quiere leer
        //var timersListAux = mutableListOf<DatetimeServer>()
        val timersList = mutableListOf<DatetimeServer>()
        //var enableTimers = 0
        withContext(Dispatchers.IO) {
            val user = FirebaseAuth.getInstance().currentUser
            user?.let {

                val db = FirebaseDatabase.getInstance().reference
                db.keepSynced(true)

                val tm = db.child("users/${user.uid}/channels/channel${channel}/timers").get().await()

                for(timer in tm.children){
                    timer.getValue(DatetimeServer::class.java)?.let { timersList.add(it) }

                }

                //Organiza la lista de forma descendente y se cambia el valor de retorno por timerListAux
                //timersListAux= timersList.sortedBy { it.hour} as MutableList<DatetimeServer>
            }
        }
        return timersList
    }

    suspend fun addTimer(enableTimers:Int) {
        withContext(Dispatchers.IO) {
            val user = FirebaseAuth.getInstance().currentUser

            user?.let {
                val db = FirebaseDatabase.getInstance()
                    .getReference("users/${user.uid}/channels/channel1/timers/enableTimers")

                db.setValue(enableTimers).await()
            }
        }
    }

    suspend fun setTimer(timerData : DatetimeServer) {
        withContext(Dispatchers.IO) {
            val user = FirebaseAuth.getInstance().currentUser

            user?.let {
                val db = FirebaseDatabase.getInstance()

                val url1 = db.getReference("users/${user.uid}/channels/channel${timerData.channel}/timers/timer${timerData.eventNum}")

                url1.setValue(timerData).await()
            }
        }
    }

}