package com.example.plainolnotes4.data

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.plainolnotes4.R
import java.util.*

const val NEW_NOTE_ID: Int = 0

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var date: Date,
    var text: String
) {
    constructor() : this(NEW_NOTE_ID, Date(), "")
    constructor(date: Date, text: String) : this(NEW_NOTE_ID, date, text)

    companion object {
        fun getSampleNoteList(context: Context): List<NoteEntity> {
            fun getDate(diff: Long = 0): Date = Date(Date().time + diff)

            return arrayListOf(
                NoteEntity(1, getDate(), context.getString(R.string.sample_note_1)),
                NoteEntity(2, getDate(1), context.getString(R.string.sample_note_2)),
                NoteEntity(3, getDate(2), context.getString(R.string.sample_note_3))
            )
        }
    }
}