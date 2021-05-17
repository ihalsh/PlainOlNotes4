package com.example.plainolnotes4

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.plainolnotes4.data.AppDatabase
import com.example.plainolnotes4.data.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val noteDao = AppDatabase.getInstance(application)?.noteDao()
    val notesList: LiveData<List<NoteEntity>>? = noteDao?.getAll()

    fun addSampleData() {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao?.insertAll(NoteEntity.getSampleNoteList(getApplication()))
        }
    }
}