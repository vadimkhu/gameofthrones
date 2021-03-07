package ru.skillbranch.gameofthrones.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

val Context.isNetworkAvailable: Boolean
    get() {
        var result = false
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }

fun List<String>.dropLastUntil(predicate: (String) -> Boolean): String {
    val array = this
    for ((index, indexOfWord) in array.withIndex()) {
        if (predicate(indexOfWord))
            return array[index - 1]
    }
    return ""
}

fun <T, A, B> LiveData<A>.combineAndCompute(other: LiveData<B>, onChange : (A, B) -> T) : MediatorLiveData<T> {
    var source1emitted = false
    var source2emitted = false

    val result = MediatorLiveData<T>()

    val mergeF = {
        val source1Value = this.value
        val source2Value = other.value

        if(source1emitted && source2emitted){
            result.value = onChange.invoke(source1Value!!, source2Value!!)
        }
    }
    result.addSource(this) {source1emitted = true ; mergeF.invoke()}
    result.addSource(other) {source2emitted = true ; mergeF.invoke()}

    return  result
}