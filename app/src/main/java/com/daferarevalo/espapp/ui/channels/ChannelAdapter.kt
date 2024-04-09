package com.daferarevalo.espapp.ui.channels

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.daferarevalo.espapp.core.BaseViewHolder
import com.daferarevalo.espapp.data.model.ChannelServer
import com.daferarevalo.espapp.data.model.DatetimeServer
import com.daferarevalo.espapp.databinding.ChannelItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ChannelAdapter(
    private val channelList: List<ChannelServer>,
    private val itemClickListener: OnChannelsClickListener
): RecyclerView.Adapter<BaseViewHolder<*>>() {

    interface OnChannelsClickListener{
        fun onTimersClick(channel:ChannelServer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding = ChannelItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val holder = TimersViewHolder(itemBinding,parent.context)
        itemBinding.root.setOnClickListener {
            val position = holder.adapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }
                ?: return@setOnClickListener
            itemClickListener.onTimersClick(channelList[position])
        }
        return holder
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is TimersViewHolder -> holder.bind(channelList[position])
        }
    }

    override fun getItemCount(): Int = channelList.size

    private inner class TimersViewHolder(
        val binding: ChannelItemBinding,
        val context: Context
    ):BaseViewHolder<ChannelServer>(binding.root){

        override fun bind(item: ChannelServer) {
            binding.nameChannelTextView.text = item.name
            binding.stateActionSwitch.apply {
                isChecked = item.state
            }

            binding.stateActionSwitch.setOnCheckedChangeListener{ _, isChecked ->
                writeFirebase(item,isChecked) // Se debe integrar funcion a la arquitectura MVVM
            }
        }
    }

    private fun writeFirebase(item: ChannelServer, checked: Boolean) {

        val user = FirebaseAuth.getInstance().currentUser

        user?.let {
            val db = FirebaseDatabase.getInstance()

            val url1 = db.getReference("users/${user.uid}/channels/channel${item.number}/state")

            url1.setValue(checked)
        }

    }

}