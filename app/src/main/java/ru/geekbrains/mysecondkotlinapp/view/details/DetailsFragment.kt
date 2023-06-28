package ru.geekbrains.mysecondkotlinapp.view

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import ru.geekbrains.mysecondkotlinapp.R
import ru.geekbrains.mysecondkotlinapp.databinding.FragmentDetailsBinding
import ru.geekbrains.mysecondkotlinapp.model.Weather
import ru.geekbrains.mysecondkotlinapp.model.WeatherDTO
import ru.geekbrains.mysecondkotlinapp.ui.viewmodel.AppState
import java.io.BufferedReader
import java.io.IOException
import java.util.stream.Collectors

private const val YOUR_API_KEY = "266af688-30ba-419b-8e29-46cb06ba895d"
private const val MY_API_KEY = "8df85a2d-de57-4e99-be0f-4d7cb50a67ef"

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weather = arguments?.getParcelable<Weather>(BUNDLE_EXTRA)
        if (weather != null) {
            val city = weather.city
            binding.cityName.text = city.city
            binding.cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                city.lat.toString(),
                city.lon.toString()
            )
            binding.temperatureValue.text = weather.temperature.toString()
            binding.feelsLikeValue.text = weather.feelsLike.toString()
        }
    }*/

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
/*        arguments?.getParcelable<Weather>(BUNDLE_EXTRA)?.let { weather ->
            weather.city.also { city ->
                binding.cityName.text = city.city
                binding.cityCoordinates.text = String.format(
                    getString(R.string.city_coordinates),
                    city.lat.toString(),
                    city.lon.toString()
                )
                binding.temperatureValue.text = weather.temperature.toString()
                binding.feelsLikeValue.text = weather.feelsLike.toString()
            }
        }*/
        weatherBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: Weather()
        binding.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE
        loadWeather()
    }

    private fun displayWeather(weatherDTO: WeatherDTO) {
        with(binding) {
            mainView.visibility = View.VISIBLE
            loadingLayout.visibility = View.GONE
            val city = weatherBundle.city
            cityName.text = city.city
            cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                city.lat.toString(),
                city.lon.toString()
            )
            weatherCondition.text = weatherDTO.fact?.condition
            temperatureValue.text = weatherDTO.fact?.temp.toString()
            feelsLikeValue.text = weatherDTO.fact?.feels_like.toString()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadWeather() {
/*        try {
            val uri =
                URL("https://api.weather.yandex.ru/v2/informers?lat=${weatherBundle.city.lat}&lon=${weatherBundle.city.lon}")
            val handler = Handler()
            Thread(Runnable {
                lateinit var urlConnection: HttpsURLConnection
                try {
                    urlConnection = uri.openConnection() as HttpsURLConnection
                    urlConnection.requestMethod = "GET"
                    urlConnection.addRequestProperty(
                        "X-Yandex-API-Key",
                        YOUR_API_KEY
                    )
                    urlConnection.readTimeout = 10000
                    val bufferedReader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream))

                    // преобразование ответа от сервера (JSON) в модель данных (WeatherDTO)
                    val weatherDTO: WeatherDTO =
                        Gson().fromJson(getLines(bufferedReader), WeatherDTO::class.java)
                    handler.post { displayWeather(weatherDTO) }
                } catch (e: Exception) {
                    Log.e("", "Fail connection", e)
                    e.printStackTrace()
                    //Обработка ошибки
                } finally {
                    urlConnection.disconnect()
                }
            }).start()
        } catch (e: MalformedURLException) {
            Log.e("", "Fail URI", e)
            e.printStackTrace()
            //Обработка ошибки
        }
    }*/
        val client = OkHttpClient() // Клиент
        val builder: Request.Builder = Request.Builder() // Создаём строителя запроса
        builder.header("X-Yandex-API-Key", MY_API_KEY) // Создаём заголовок запроса
        builder.url("https://api.weather.yandex.ru/v2/informers?lat=${weatherBundle.city.lat}&lon=${weatherBundle.city.lon}") // Формируем URL
        val request: Request = builder.build() // Создаём запрос
        val call: Call = client.newCall(request) // Ставим запрос в очередь и отправляем
        call.enqueue(object : Callback {

            val handler: Handler = Handler()

            // Вызывается, если ответ от сервера пришёл
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val serverResponse: String? = response.body()?.string()
                // Синхронизируем поток с потоком UI
                if (response.isSuccessful && serverResponse != null) {
                    handler.post {
                        val weather = Gson().fromJson(serverResponse, WeatherDTO::class.java)
                        val weather2 = Gson().fromJson(serverResponse, WeatherDTO::class.java)
                        //renderData(Gson().fromJson(serverResponse, WeatherDTO::class.java))
                    }
                } else {
                    TODO()
                }
            }

            // Вызывается при сбое в процессе запроса на сервер
            override fun onFailure(call: Call?, e: IOException?) {
                TODO()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.mainView.visibility = View.VISIBLE
                /*binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                setWeather(appState.weatherData[0])*/
            }

            is AppState.Loading -> {
                binding.mainView.visibility = View.GONE
                //binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
            }

            is AppState.Error -> {
                binding.mainView.visibility = View.VISIBLE
                /*binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                binding.mainView.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    {
                        viewModel.getWeatherFromRemoteSource(
                            weatherBundle.city.lat,
                            weatherBundle.city.lon
                        )
                    })*/
            }
        }
    }

    companion object {

        const val BUNDLE_EXTRA = "weather"

        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}

/*
   *//* private lateinit var viewModel: MainViewModel
    private var _binding: MainFragmentBinding? = null

    //Обратите внимание, что эта переменная существует только между методами onCreateView и onDestroyView. Можете объяснить почему?
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        //val observer = Observer<Any> { renderData(it) }
        //viewModel.getData().observe(viewLifecycleOwner, observer)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getWeatherFromLocalSource()*//*

        // TODO: Use the ViewModel
    }

    *//*private fun renderData(data: Any?) {
        Toast.makeText(context, "data", Toast.LENGTH_LONG).show()
    }*//*
    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                val weatherData = appState.weatherData
                binding.loadingLayout.visibility = View.GONE
                //Snackbar.make(binding.mainView, "Success", Snackbar.LENGTH_LONG).show()
                setData(weatherData)
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar
                    .make(binding.mainView, "Error", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { viewModel.getWeatherFromLocalSource() }
                    .show()
            }
        }
    }

    private fun setData(weatherData: Weather) {
        binding.cityName.text = weatherData.city.city
        binding.cityCoordinates.text = String.format(
            getString(R.string.city_coordinates),
            weatherData.city.lat.toString(),
            weatherData.city.lon.toString()
        )
        binding.temperatureValue.text = weatherData.temperature.toString()
        binding.feelsLikeValue.text = weatherData.feelsLike.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = DetailsFragment()
    }
}*/
