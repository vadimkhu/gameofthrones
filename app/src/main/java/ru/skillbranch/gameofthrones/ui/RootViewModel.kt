package ru.skillbranch.gameofthrones.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.skillbranch.gameofthrones.extensions.isNetworkAvailable
import ru.skillbranch.gameofthrones.repositories.RootRepository

class RootViewModel(val app : Application) : AndroidViewModel(app){
    private val repository = RootRepository

    fun syncDataIfNeed(): LiveData<LoadResult<Boolean>> {
        val result : MutableLiveData<LoadResult<Boolean>> = MutableLiveData(LoadResult.Loading<Boolean>(false))
        viewModelScope.launch (Dispatchers.IO) {
            repository.isNeedUpdate {isNeedUpdate ->
                if (isNeedUpdate) {
                    if (!app.applicationContext.isNetworkAvailable) {
                        result.postValue(LoadResult.Error("Интернет недоступен - приложение не может быть запущенно. Подключитесь к интернету и перезапустите приложение"))
                    }
                    repository.sync() {
                        result.postValue(LoadResult.Success(true))
                    }
                }
                else {
                    Log.d("RootViewModel", "in not need update")
                    result.postValue(LoadResult.Success(true))
                }
            }
        }
        return result
    }
}

sealed class LoadResult<T> (
    val data : T?,
    val errorMessage : String? = null
) {

    class Success<T>(data : T) : LoadResult<T>(data)
    class Loading<T>(data : T? = null) : LoadResult<T>(data)
    class Error<T>(message : String?, data : T? = null) : LoadResult<T>(data, message)

}