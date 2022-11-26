package ru.geekbrains.mysecondkotlinapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.mysecondkotlinapp.model.Repository
import ru.geekbrains.mysecondkotlinapp.model.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()
) :
    ViewModel() {

    /* fun getData(): LiveData<Any> {
         getDataFromLocalSource()
         return liveDataToObserve
     }*/

    fun getLiveData() = liveDataToObserve

    //fun getWeather() = getDataFromLocalSource()
    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(isRussian = true)

    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(isRussian = false)

    fun getWeatherFromRemoteSource() = getDataFromLocalSource(isRussian = true)

    private fun getDataFromLocalSource(isRussian: Boolean) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(1000)
            liveDataToObserve.postValue(AppState.Success(if (isRussian)
                repositoryImpl.getWeatherFromLocalStorageRus() else
                    repositoryImpl.getWeatherFromLocalStorageWorld()))
        }.start()

    }
    // TODO: Implement the ViewModel
}