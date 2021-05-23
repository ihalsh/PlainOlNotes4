package com.example.plainolnotes4

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.plainolnotes4.data.AppDatabase
import com.example.plainolnotes4.data.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditorViewModel(application: Application) : AndroidViewModel(application) {
    private val noteDao = AppDatabase.getInstance(application)?.noteDao()
    val currentNote = MutableStateFlow(NoteEntity())

    fun getNoteById(noteId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val note =
                    if (noteId != 0) {
                        noteDao?.getNoteById(noteId)
                    } else {
                        NoteEntity()
                    }
                currentNote.emit(note ?: NoteEntity())
            }
        }
    }

    fun updateCurrentNote() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                currentNote.value.let {
                    if (it.id != 0) {
                        if (it.text.isEmpty()) {
                            noteDao?.deleteSingleNote(it)
                        } else {
                            noteDao?.insertNote(it)
                        }
                    } else if (it.text.isNotEmpty()) {
                        noteDao?.insertNote(it)
                    }
                }
            }
        }
    }
}