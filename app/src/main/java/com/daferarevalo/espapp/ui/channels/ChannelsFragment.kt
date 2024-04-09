package com.daferarevalo.espapp.ui.channels

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.daferarevalo.espapp.R
import com.daferarevalo.espapp.core.hide
import com.daferarevalo.espapp.core.show
import com.daferarevalo.espapp.data.model.ChannelServer
import com.daferarevalo.espapp.data.remote.home.HomeDataSource
import com.daferarevalo.espapp.databinding.FragmentChannelsBinding
import com.daferarevalo.espapp.domain.home.HomeRepoImpl
import com.daferarevalo.espapp.presentation.home.HomeViewModel
import com.daferarevalo.espapp.presentation.home.HomeViewModelFactory
import com.daferarevalo.espapp.core.Result

class ChannelsFragment : Fragment(R.layout.fragment_channels),ChannelAdapter.OnChannelsClickListener {
    private var definedChannels = 0
    var channelData = ChannelServer()
    private lateinit var binding: FragmentChannelsBinding
    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory(HomeRepoImpl(
            HomeDataSource()
        )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentChannelsBinding.bind(view)

        // Agrega un callback para manejar el evento de retroceso
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner , object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_channelsFragment_to_navigation_home)
            }
        })

        binding.toolbar.inflateMenu(R.menu.menu_toolbar_channels) // Añadir un menú (si lo necesitas)
        val menuItem = binding.toolbar.menu.findItem(R.id.action_button)
        val iconDrawable = menuItem.icon
        val iconColor = ContextCompat.getColor(requireContext(), R.color.md_grey_100)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_button -> {

                    if(definedChannels > 6){
                        Toast.makeText(context, "No se puede agregar mas timers", Toast.LENGTH_SHORT).show()
                    }else{
                        val action = ChannelsFragmentDirections.actionChannelsFragmentToAddChannelFragment(definedChannels)
                        findNavController().navigate(action)
                    }

                    true
                }
                else -> false
            }
        }

        viewModel.getDefinedChannelsModel().observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Result.Loading -> {
                    binding.linearLayoutPrincipal.hide()
                    binding.linearLayoutSecundario.show()
                }
                is Result.Success -> {
                    binding.linearLayoutPrincipal.show()
                    binding.linearLayoutSecundario.hide()
                    definedChannels = result.data.size
                    binding.channelsRecyclerView.adapter =
                        ChannelAdapter(result.data, this@ChannelsFragment)
                }
                is Result.Failure -> {
                    binding.progressBar.hide()
                    //binding.addChanelButton.isEnabled = true
                    //Log.d("ERROR", "Error: ${result.exception}")
                    Toast.makeText(
                        context,
                        "Error: ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

    }

    override fun onTimersClick(channel: ChannelServer) {
        val action = ChannelsFragmentDirections.actionChannelsFragmentToChannelDetailsFragment(channel,true)
        findNavController().navigate(action)
    }


}