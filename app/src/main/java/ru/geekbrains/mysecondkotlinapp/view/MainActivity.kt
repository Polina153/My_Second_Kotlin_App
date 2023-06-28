package ru.geekbrains.mysecondkotlinapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.geekbrains.mysecondkotlinapp.R
import ru.geekbrains.mysecondkotlinapp.databinding.MainActivityBinding
import ru.geekbrains.mysecondkotlinapp.view.main.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //binding.ok.setOnClickListener(clickListener)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitAllowingStateLoss()
        }
    }

    /*var clickListener: View.OnClickListener = object : View.OnClickListener {

        @RequiresApi(Build.VERSION_CODES.N)
        override fun onClick(v: View?) {
            try {
                val uri = URL(binding.url.text.toString())
                val handler = Handler() //Запоминаем основной поток
                Thread {
                    var urlConnection: HttpsURLConnection? = null
                    try {
                        urlConnection = uri.openConnection() as HttpsURLConnection
                        urlConnection.requestMethod = "GET" //установка метода получения данных -- GET
                        urlConnection.readTimeout = 10000 //установка таймаута -- 10 000 миллисекунд
                        val reader =
                            BufferedReader(InputStreamReader(urlConnection.inputStream)) //читаем данные в поток
                        val result = getLines(reader)

                        // Возвращаемся к основному потоку
                        handler.post {
                            binding.webview.loadData(result, "text/html; charset=utf-8", "utf-8")
                        }
                    } catch (e: Exception) {
                        Log.e("", "Fail connection", e)
                        e.printStackTrace()
                    } finally {
                        urlConnection?.disconnect()
                    }
                }.start()
            } catch (e: MalformedURLException) {
                Log.e("", "Fail URI", e)
                e.printStackTrace()
            }
        }

        @RequiresApi(Build.VERSION_CODES.N)
        private fun getLines(reader: BufferedReader): String {
            return reader.lines().collect(Collectors.joining("\n"))
        }
    }*/
}
