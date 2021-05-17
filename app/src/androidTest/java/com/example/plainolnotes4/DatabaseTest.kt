package com.example.plainolnotes4

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.plainolnotes4.data.AppDatabase
import com.example.plainolnotes4.data.NoteDao
import com.example.plainolnotes4.data.NoteEntity
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var dao: NoteDao
    private lateinit var database: AppDatabase
    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.noteDao()!!
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun shouldInsertListOfNotes() {
        val list = NoteEntity.getSampleNoteList(appContext)
        dao.insertAll(list)
        val count = dao.getCount()
        assertEquals(count, list.size)
    }

    @Test
    fun shouldInsertSingleNote() {
        val id = 1
        val noteEntity = NoteEntity().apply { text = "Test note" }
        dao.insertNote(noteEntity)
        val noteFromDb = dao.getNoteById(id)
        assertEquals(id, noteFromDb?.id ?: 0)
    }
}