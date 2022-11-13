package ru.geekbrains.mysecondkotlinapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.mysecondkotlinapp.databinding.MainFragmentBinding
import ru.geekbrains.mysecondkotlinapp.ui.viewmodel.AppState
import ru.geekbrains.mysecondkotlinapp.ui.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding: MainFragmentBinding? = null

    //Обратите внимание, что эта переменная существует только между методами onCreateView и onDestroyView. Можете объяснить почему?
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        //val observer = Observer<Any> { renderData(it) }
        //viewModel.getData().observe(viewLifecycleOwner, observer)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getWeather()

        // TODO: Use the ViewModel
    }

    /*private fun renderData(data: Any?) {
        Toast.makeText(context, "data", Toast.LENGTH_LONG).show()
    }*/
    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                val weatherData = appState.weatherData
                binding.loadingLayout.visibility = View.GONE
                Snackbar.make(binding.mainView, "Success", Snackbar.LENGTH_LONG).show()
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar
                    .make(binding.mainView, "Error", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { viewModel.getWeather() }
                    .show()
            }
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}