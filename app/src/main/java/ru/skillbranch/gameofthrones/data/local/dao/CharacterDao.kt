package ru.skillbranch.gameofthrones.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ru.skillbranch.gameofthrones.data.local.entities.Character
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem

@Dao
interface CharacterDao : BaseDao<Character> {
    @Query("SELECT * FROM CharacterItem WHERE house = :title")
    fun findCharacters(title: String) : LiveData<List<CharacterItem>>

    @Query("SELECT * FROM CharacterItem WHERE house = :title")
    fun findCharacterList(title: String) : List<CharacterItem>

    @Query("DELETE FROM characters")
    fun deleteTable()

    @Transaction
    fun upsert(objList : List<Character>) {
        insert(objList)
            .mapIndexed {index,l -> if(l == -1L) objList[index] else null}
            .filterNotNull()
            .also{ if(it.isNotEmpty()) update(it)}
    }
}