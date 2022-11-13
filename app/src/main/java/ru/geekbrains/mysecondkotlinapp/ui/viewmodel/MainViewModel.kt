package ru.geekbrains.mysecondkotlinapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Thread.sleep

class MainViewModel(private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()) :
    ViewModel() {

       /* fun getData(): LiveData<Any> {
            getDataFromLocalSource()
            return liveDataToObserve
        }*/

    fun getLiveData() = liveDataToObserve

    fun getWeather() = getDataFromLocalSource()


    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(1000)
            liveDataToObserve.postValue(AppState.Success(Any()))
        }.start()

    }
    // TODO: Implement the ViewModel
}