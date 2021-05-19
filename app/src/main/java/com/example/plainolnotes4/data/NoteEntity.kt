package com.example.plainolnotes4.data

import android.content.Context
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.plainolnotes4.R
import kotlinx.android.parcel.Parcelize
import java.util.*

const val NEW_NOTE_ID: Int = 0

@Parcelize
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var date: Date,
    var text: String
) : Parcelable {
    constructor() : this(NEW_NOTE_ID, Date(), "")
    constructor(date: Date, text: String) : this(NEW_NOTE_ID, date, text)
}

fun getSampleNoteList(context: Context): List<NoteEntity> {
    fun getDate(diff: Long = 0): Date = Date(Date().time + diff)

    return arrayListOf(
        NoteEntity(getDate(), context.getString(R.string.sample_note_1)),
        NoteEntity(getDate(1), context.getString(R.string.sample_note_2)),
        NoteEntity(getDate(2), context.getString(R.string.sample_note_3))
    )
}