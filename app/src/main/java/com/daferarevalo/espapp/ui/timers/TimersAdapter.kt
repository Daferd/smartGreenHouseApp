package com.daferarevalo.espapp.ui.timers

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.daferarevalo.espapp.core.BaseViewHolder
import com.daferarevalo.espapp.data.model.DatetimeServer
import com.daferarevalo.espapp.databinding.TimerItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class TimersAdapter(
    private val timersList: List<DatetimeServer>,
    private val itemClickListener: OnTimersClickListener
): RecyclerView.Adapter<BaseViewHolder<*>>() {
    interface OnTimersClickListener{
        fun onTimersClick(timer:DatetimeServer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding = TimerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val holder = TimersViewHolder(itemBinding,parent.context)
        itemBinding.root.setOnClickListener {
            val position = holder.adapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }
                ?: return@setOnClickListener
            itemClickListener.onTimersClick(timersList[position])
        }
        return holder
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is TimersViewHolder -> holder.bind(timersList[position])
        }
    }

    override fun getItemCount(): Int = timersList.size

    private inner class TimersViewHolder(
        val binding: TimerItemBinding,
        val context: Context
    ):BaseViewHolder<DatetimeServer>(binding.root){

        override fun bind(item: DatetimeServer) {
            val listDays = mutableListOf<Boolean>()
            var daysStr = ""

            listDays.add(item.sun)
            listDays.add(item.mon)
            listDays.add(item.tues)
            listDays.add(item.wend)
            listDays.add(item.thurs)
            listDays.add(item.fri)
            listDays.add(item.satu)

            daysStr = generateDaysOfTheWeekString(listDays)

            binding.timeTextView.text = "${item.hour}:${item.min}"
            binding.dateTextView.text = if (item.action) "$daysStr: Encender" else "$daysStr: Apagar"
            //binding.actionTextView.text = if (item.action) "Encender" else "Apagar"
            binding.stateTimerSwitch.apply {
                isChecked = item.state
            }

            binding.stateTimerSwitch.setOnCheckedChangeListener { _, isChecked ->


                writeFirebase(item,isChecked) // Se debe integrar funcion a la arquitectura MVVM

                /*val timerData = DatetimeServer()
                timerData.modo = isChecked
                viewModel.setTimerModel(timerData).observe(lifecycleScope, Observer { result ->
                    when(result){
                        is Result.Loading -> {
                            //binding.progressBar.show()
                            //binding.saveButton.isEnabled = false
                        }
                        is Result.Success -> {
                            Toast.makeText(context,"OKK",Toast.LENGTH_SHORT).show()

                        }
                        is Result.Failure -> {
                            //binding.progressBar.hide()
                            //binding.saveButton.isEnabled = true
                            Toast.makeText(context,"Error: ${result.exception}",Toast.LENGTH_SHORT).show()
                        }
                    }
                })*/
            }
        }
    }

    private fun writeFirebase(item: DatetimeServer, checked: Boolean) {

        val user = FirebaseAuth.getInstance().currentUser

        user?.let {
            val db = FirebaseDatabase.getInstance()

            val url1 = db.getReference("users/${user.uid}/channels/channel${item.channel}/timers/timer${item.eventNum}/state")

            url1.setValue(checked)
        }

    }

    private fun generateDaysOfTheWeekString(listDays: List<Boolean>): String{
        val daysWeek = listOf("D", "L", "M", "W", "J", "V", "S")

        if (listDays.all { it }) { // Verifica si todos los días están marcados
            return "Todos los días"
        }

        val daysWeekStr = daysWeek.filterIndexed { index, _ -> listDays[index] }
        return daysWeekStr.joinToString(",")
    }

}