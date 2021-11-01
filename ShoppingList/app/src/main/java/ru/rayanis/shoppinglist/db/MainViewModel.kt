package ru.rayanis.shoppinglist.db

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.rayanis.shoppinglist.entities.NoteItem
import java.lang.IllegalArgumentException

class MainViewModel(database: MainDataBase): ViewModel() {
    val dao = database.getDao()
    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()

    fun insertNote(note: NoteItem) = viewModelScope.launch {
        dao.insertNote(note)
    }

    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNote(id)
    }

    class MainViewModelFactory(val database: MainDataBase): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(database) as T
            }
            throw  IllegalArgumentException("Unknown ViewModelClass")
        }
    }
}