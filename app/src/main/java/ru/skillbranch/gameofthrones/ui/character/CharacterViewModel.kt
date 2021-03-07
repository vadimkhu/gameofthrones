package ru.skillbranch.gameofthrones.ui.character

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import ru.skillbranch.gameofthrones.repositories.RootRepository

class CharacterViewModel(private val characterId : String) : ViewModel() {
    private val repository = RootRepository

    fun getCharacter() : LiveData<Character> {
        return liveData {  }
    }

}

class CharacterViewModelFactory(private val characterId : String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CharacterViewModel::class.java)){
            return CharacterViewModel(characterId) as T
        }
        throw  IllegalArgumentException("unknown ViewModel class")
    }
}
