package com.example.plainolnotes4

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.plainolnotes4.data.AppDatabase
import com.example.plainolnotes4.data.NoteEntity
import com.example.plainolnotes4.data.getSampleNoteList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val noteDao = AppDatabase.getInstance(application)?.noteDao()
    val notesList: Flow<List<NoteEntity>>? = noteDao?.getAll()

    fun addSampleData() {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao?.insertAll(getSampleNoteList(getApplication()))
        }
    }

    fun deleteMultipleNotes(list: List<NoteEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao?.deleteMultipleNotes(list)
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao?.deleteAllNotes()
        }
    }
}
