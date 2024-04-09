package com.daferarevalo.espapp.ui.channels

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.daferarevalo.espapp.R
import com.daferarevalo.espapp.data.remote.home.HomeDataSource
import com.daferarevalo.espapp.databinding.FragmentAddChannelBinding
import com.daferarevalo.espapp.domain.home.HomeRepoImpl
import com.daferarevalo.espapp.presentation.home.HomeViewModel
import com.daferarevalo.espapp.presentation.home.HomeViewModelFactory
import com.daferarevalo.espapp.core.Result
import com.daferarevalo.espapp.data.model.ChannelServer

class AddChannelFragment : DialogFragment() {

    lateinit var binding: FragmentAddChannelBinding
    val args: AddChannelFragmentArgs by navArgs()
    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory(
            HomeRepoImpl(
                HomeDataSource()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_channel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddChannelBinding.bind(view)
        val channel = ChannelServer()

        binding.acceptButton.setOnClickListener {

            channel.number=args.enabledChannels
            channel.name = binding.nameChannelEditText.text.toString().trim()
            if(channel.name.isEmpty()){
                binding.nameChannelEditText.error = "Ingrese el nombre del canal"
            }else{
                val alertDialog = AlertDialog.Builder(requireContext()).setTitle("Guardando nuevo canal...").create()
                viewModel.addChannelModel(channel).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Result.Loading -> {
                            alertDialog.show()
                        }
                        is Result.Success -> {
                            alertDialog.dismiss()
                            //dismiss()
                            findNavController().navigate(R.id.action_addChannelFragment_to_channelsFragment)
                            Toast.makeText(context, "Nuevo canal guardado", Toast.LENGTH_SHORT).show()
                        }
                        is Result.Failure -> {
                            alertDialog.dismiss()
                        }
                    }
                }
            }
        }

    }


}