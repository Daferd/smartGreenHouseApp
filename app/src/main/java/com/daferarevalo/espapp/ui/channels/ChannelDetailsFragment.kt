package com.daferarevalo.espapp.ui.channels

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.daferarevalo.espapp.R
import com.daferarevalo.espapp.data.remote.home.HomeDataSource
import com.daferarevalo.espapp.databinding.FragmentChannelDetailsBinding
import com.daferarevalo.espapp.databinding.FragmentEditChannelBinding
import com.daferarevalo.espapp.domain.home.HomeRepoImpl
import com.daferarevalo.espapp.presentation.home.HomeViewModel
import com.daferarevalo.espapp.presentation.home.HomeViewModelFactory
import com.daferarevalo.espapp.core.Result
import com.daferarevalo.espapp.data.model.ChannelServer


class ChannelDetailsFragment : Fragment(R.layout.fragment_channel_details) {
    lateinit var binding: FragmentChannelDetailsBinding
    val args: ChannelDetailsFragmentArgs by navArgs()
    var channelData = ChannelServer()

    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory(
            HomeRepoImpl(
            HomeDataSource()
        )
        )
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentChannelDetailsBinding.bind(view)
        channelData = args.channelData!!

        binding.channelNameTextViwe.text = "Canal ${channelData?.number}"

        // Agrega un callback para manejar el evento de retroceso
        /*requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner , object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                /*when(args.track){
                    1->{ findNavController().navigate(TimerFragmentDirections.actionTimerFragmentToNavigationHome())}
                    2->{findNavController().navigate(TimerFragmentDirections.actionTimerFragmentToAnalogPortsFragment())}
                }*/
            }
        })*/

        viewModel.checkChannelModel(args.channelData!!.number).observe(viewLifecycleOwner, Observer{ result ->
            when(result){
                is Result.Loading -> {
                    //binding.progressBar.show()
                    //binding.addChanelButton.isEnabled = false
                }
                is Result.Success -> {
                    channelData = result.data
                }
                is Result.Failure -> {
                    //binding.progressBar.hide()
                    //binding.addChanelButton.isEnabled = true
                    Toast.makeText(
                        context,
                        "Error: ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        })

        binding.turnOnChannelButton.setOnClickListener {
            val state = true
            viewModel.turnChannelModel(channelData!!.number,state).observe(viewLifecycleOwner, Observer{ result ->
                when(result){
                    is Result.Loading -> {
                        //binding.progressBar.show()
                        //binding.addChanelButton.isEnabled = false
                    }
                    is Result.Success -> {
                        /*if (state){
                            binding.turnOnChannelButton.setBackgroundColor(Color.GREEN)
                        }else{
                            binding.turnOnChannelButton.setBackgroundColor(Color.RED)
                        }*/

                    }
                    is Result.Failure -> {
                        //binding.progressBar.hide()
                        //binding.addChanelButton.isEnabled = true
                        Toast.makeText(
                            context,
                            "Error: ${result.exception}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        }

        binding.turnOffChannelButton.setOnClickListener {
            val state = false
            viewModel.turnChannelModel(channelData!!.number,state).observe(viewLifecycleOwner, Observer{ result ->
                when(result){
                    is Result.Loading -> {
                        //binding.progressBar.show()
                        //binding.addChanelButton.isEnabled = false
                    }
                    is Result.Success -> {


                    }
                    is Result.Failure -> {
                        //binding.progressBar.hide()
                        //binding.addChanelButton.isEnabled = true
                        Toast.makeText(
                            context,
                            "Error: ${result.exception}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        }

        binding.addTimeButton.setOnClickListener {
            val action = ChannelDetailsFragmentDirections.actionChannelDetailsFragmentToTimersFragment(
                channelData?.number!!
            )
            findNavController().navigate(action)
        }
    }
}

