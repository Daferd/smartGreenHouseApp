package com.daferarevalo.espapp.ui.channels

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.daferarevalo.espapp.R
import com.daferarevalo.espapp.core.show
import com.daferarevalo.espapp.data.model.ChannelServer
import com.daferarevalo.espapp.data.model.DatetimeServer
import com.daferarevalo.espapp.data.remote.home.HomeDataSource
import com.daferarevalo.espapp.databinding.FragmentEditChannelBinding
import com.daferarevalo.espapp.domain.home.HomeRepoImpl
import com.daferarevalo.espapp.presentation.home.HomeViewModel
import com.daferarevalo.espapp.presentation.home.HomeViewModelFactory
import com.daferarevalo.espapp.core.Result
import com.daferarevalo.espapp.core.hide

class EditChannelFragment : Fragment() {

    private lateinit var binding:FragmentEditChannelBinding

    private var enableTimers = 0
    private var timerData = DatetimeServer()
    private var time = ""
    private var daysActived = false
    private var action = ""

    val args: EditChannelFragmentArgs by navArgs()
    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory(
            HomeRepoImpl(
            HomeDataSource()
        )
        )
    }

    private var channelStatus=ChannelServer()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_channel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentEditChannelBinding.bind(view)
        timerData = args.datetimer!!
        enableTimers = args.datetimer!!.eventNum

        if(args.track){
            binding.actionSwitch.apply {
                isChecked = timerData.action
            }
            //binding.hourEditText.setText("${timerData.hour}:${timerData.min}")
            binding.hourTimePicker.hour = timerData.hour
            binding.hourTimePicker.minute = timerData.min
            binding.mondayCheck.apply { isChecked = timerData.mon }
            binding.thursdayCheck.apply { isChecked = timerData.thurs }
            binding.wednesdayCheck.apply { isChecked = timerData.wend}
            binding.tuesdayCheck.apply { isChecked = timerData.tues }
            binding.fridayCheck.apply { isChecked = timerData.fri }
            binding.saturdayCheck.apply { isChecked = timerData.satu }
            binding.sundayCheck.apply { isChecked = timerData.sun }
        }else{

        }

        binding.hourTimePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            timerData=DatetimeServer(timerData.channel,true,enableTimers,hourOfDay,minute,timerData.action,timerData.sun,timerData.mon,timerData.thurs,timerData.wend,timerData.tues,timerData.fri,timerData.satu)
        }

        binding.actionSwitch.setOnCheckedChangeListener { _,isChecked->
            timerData.action = isChecked
        }

        listDayActions()

        binding.saveButton.setOnClickListener {

            if(validateTimerData(checkAtLeastOneActiveDay(timerData)))return@setOnClickListener
            binding.errorDaysTextView.show()
            if(!args.track) enableTimers += 1
            timerData.eventNum = enableTimers
            viewModel.setTimerModel(timerData).observe(viewLifecycleOwner, Observer { result ->
                when(result){
                    is Result.Loading -> {
                        binding.progressBar.show()
                        binding.saveButton.isEnabled = false
                    }
                    is Result.Success -> {
                        binding.progressBar.hide()
                        binding.saveButton.isEnabled = true
                        val action = EditChannelFragmentDirections.actionEditChannelFragmentToTimersFragment(timerData.channel)
                        //EditTimerFragmentDirections.actionEditTimerFragmentToTimerFragment(timerData)
                        findNavController().navigate(action)
                        Toast.makeText(context,"Guardado!", Toast.LENGTH_SHORT).show()
                        //findNavController().navigate(R.id.action_editTimerFragment_to_timerFragment)
                        /*
                        viewModel.addTimerModel(enableTimers).observe(viewLifecycleOwner, Observer { result ->
                            when(result){
                                is Result.Loading -> {
                                    //binding.saveButton.isEnabled
                                }
                                is Result.Success -> {

                                    val action = EditChannelFragmentDirections.actionEditChannelFragmentToTimersFragment()
                                        //EditTimerFragmentDirections.actionEditTimerFragmentToTimerFragment(timerData)
                                    findNavController().navigate(action)
                                    Toast.makeText(context,"Guardado!", Toast.LENGTH_SHORT).show()
                                    //findNavController().navigate(R.id.action_editTimerFragment_to_timerFragment)
                                }
                                is Result.Failure -> {
                                    Toast.makeText(context,"ERROR: ${result.exception}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        })
                        */
                        //findNavController().navigate(R.id.action_editTimerFragment_to_timerFragment)

                    }
                    is Result.Failure -> {
                        binding.progressBar.hide()
                        binding.saveButton.isEnabled = true
                        Toast.makeText(
                            context,
                            "Error: ${result.exception}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        }

    }

    fun checkAtLeastOneActiveDay(datetime: DatetimeServer): Boolean {
        return datetime.sun || datetime.mon || datetime.tues ||
                datetime.wend || datetime.thurs || datetime.fri ||
                datetime.satu
    }

    private fun listDayActions() {

        binding.sundayCheck.setOnCheckedChangeListener { _, isChecked ->
            timerData.sun = isChecked
        }

        binding.mondayCheck.setOnCheckedChangeListener { _, isChecked ->
            timerData.mon = isChecked
        }
        binding.tuesdayCheck.setOnCheckedChangeListener { _, isChecked ->
            timerData.tues = isChecked
        }
        binding.wednesdayCheck.setOnCheckedChangeListener { _, isChecked ->
            timerData.wend = isChecked
        }
        binding.thursdayCheck.setOnCheckedChangeListener { _, isChecked ->
            timerData.thurs = isChecked
        }
        binding.fridayCheck.setOnCheckedChangeListener { _, isChecked ->
            timerData.fri = isChecked
        }
        binding.saturdayCheck.setOnCheckedChangeListener { _, isChecked ->
            timerData.satu = isChecked
        }

    }

    @SuppressLint("SetTextI18n")
    private fun onTimeSelectedHora(hour: Int, minute: Int) {
        time = "$hour:$minute"
        //binding.hourEditText.setText(time)
        timerData=DatetimeServer(timerData.channel,true,enableTimers,hour,minute,timerData.action,timerData.sun,timerData.mon,timerData.thurs,timerData.wend,timerData.tues,timerData.fri,timerData.satu)
        //horaConfigFirebase(hour, minute)
    }

    private fun validateTimerData(
        daysAct: Boolean,
    ): Boolean {
        if (!daysAct) {
            binding.errorDaysTextView.show()
            return true
        }
        return false
    }
}

