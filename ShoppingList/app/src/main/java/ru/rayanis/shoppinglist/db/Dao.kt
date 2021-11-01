package ru.rayanis.shoppinglist.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.rayanis.shoppinglist.entities.NoteItem

@Dao
interface Dao {
    @Query("SELECT * FROM note_list")
    fun getAllNotes(): Flow<List<NoteItem>>

    @Query("SELECT * FROM note_list WHERE id IS :id")
    fun deleteNote(id: Int)

    @Insert
    suspend fun insertNote(note: NoteItem)


}