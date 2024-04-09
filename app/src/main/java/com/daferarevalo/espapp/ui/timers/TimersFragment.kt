package com.daferarevalo.espapp.ui.timers

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.daferarevalo.espapp.R
import com.daferarevalo.espapp.data.model.DatetimeServer
import com.daferarevalo.espapp.data.remote.home.HomeDataSource
import com.daferarevalo.espapp.databinding.FragmentTimersBinding
import com.daferarevalo.espapp.domain.home.HomeRepoImpl
import com.daferarevalo.espapp.presentation.home.HomeViewModel
import com.daferarevalo.espapp.presentation.home.HomeViewModelFactory
import com.daferarevalo.espapp.core.Result
import com.daferarevalo.espapp.core.hide
import com.daferarevalo.espapp.core.show
import com.daferarevalo.espapp.ui.channels.EditChannelFragmentArgs


class TimersFragment : Fragment(R.layout.fragment_timers), TimersAdapter.OnTimersClickListener {
    private var definedTimers = 0
    var timerData = DatetimeServer()
    val args: TimersFragmentArgs by navArgs()
    lateinit var binding: FragmentTimersBinding
    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory(
            HomeRepoImpl(
                HomeDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTimersBinding.bind(view)

        // Agrega un callback para manejar el evento de retroceso
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner , object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_timersFragment_to_channelsFragment)
            }
        })

        binding.toolbar.inflateMenu(R.menu.menu_toolbar_channels) // Añadir un menú (si lo necesitas)
        val menuItem = binding.toolbar.menu.findItem(R.id.action_button)
        val iconDrawable = menuItem.icon
        val iconColor = ContextCompat.getColor(requireContext(), R.color.md_grey_100)

        DrawableCompat.setTint(iconDrawable, iconColor)

        timerData.channel = args.channel //Se define a que canal se le va a modificar los timers


        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_button -> {

                    if(definedTimers >= 6){
                        Toast.makeText(context, "No se puede agregar mas timers", Toast.LENGTH_SHORT).show()
                    }else{
                        timerData.eventNum = definedTimers
                        val action =
                            TimersFragmentDirections.actionTimersFragmentToEditChannelFragment(timerData,false)
                        findNavController().navigate(action)
                    }

                    true
                }
                else -> false
            }
        }


        viewModel.getDefinedTimersModel(args.channel).observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Result.Loading -> {
                    binding.linearLayoutPrincipal.hide()
                    binding.linearLayoutSecundario.show()
                }
                is Result.Success -> {
                    binding.linearLayoutPrincipal.show()
                    binding.linearLayoutSecundario.hide()
                    definedTimers = result.data.size
                    binding.timersRecyclerView.adapter =
                        TimersAdapter(result.data, this@TimersFragment)
                }
                is Result.Failure -> {
                    binding.progressBar.hide()
                    //binding.addChanelButton.isEnabled = true
                    Log.d("ERROR", "Error: ${result.exception}")
                    Toast.makeText(
                        context,
                        "Error: ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })


    }

    override fun onTimersClick(timer: DatetimeServer) {
        val action =TimersFragmentDirections.actionTimersFragmentToEditChannelFragment(timer,true)
        findNavController().navigate(action)
    }
}