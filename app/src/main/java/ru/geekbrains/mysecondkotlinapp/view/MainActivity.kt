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
        binding.root.apply {
            setContentView(this)
        }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commit()
        }
    }
}