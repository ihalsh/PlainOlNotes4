package com.example.plainolnotes4

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.plainolnotes4.data.NoteEntity

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _notesList = MutableLiveData<List<NoteEntity>>()
    val notesList: LiveData<List<NoteEntity>> = _notesList

    init {
        _notesList.value = NoteEntity.getSampleNoteList(application)
    }
}