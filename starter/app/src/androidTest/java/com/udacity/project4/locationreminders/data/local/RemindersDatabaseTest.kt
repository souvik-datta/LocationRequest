package com.udacity.project4.locationreminders.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RemindersDatabaseTest : TestCase(){
    private lateinit var db: RemindersDatabase
    private lateinit var dao: RemindersDao

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.reminderDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadNote() = runBlocking {
        val note = ReminderDTO(
            "12gduncjd",
            "Test",
            "Testing checking using espresso",
            22.874548,
            88.65741289,
            "2021-05-29, 17:48:55"
        )
        dao.saveReminder(note)
        val notes=dao.getReminders()
        assertTrue(notes.contains(note))
    }

}