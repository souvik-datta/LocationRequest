package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class RemindersDaoTest {

    //    TODO: Add testing implementation to the RemindersDao.kt
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    private lateinit var database: RemindersDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            RemindersDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertReminder() = runBlockingTest {
        val reminder =
            ReminderDTO("title", "description", "22.87412,88.974512", 22.87412, 88.974512)
        database.reminderDao().saveReminder(reminder)
        val loaded = database.reminderDao().getReminderById(reminder.id)
        assertThat<ReminderDTO>(loaded as ReminderDTO, notNullValue())
        assertThat(loaded.id, `is`(reminder.id))
        assertThat(loaded.title, `is`(reminder.title))
        assertThat(loaded.description, `is`(reminder.description))
    }

    @Test
    fun updateTaskAndGetById() = runBlockingTest {
        val reminder =
            ReminderDTO("title", "description", "22.87412,88.974512", 22.87412, 88.974512)
        database.reminderDao().saveReminder(reminder)
        val reminderUpdate =
            ReminderDTO("new title", "new descriptions", "22.87412,88.974512", 22.87412, 88.974512)
        database.reminderDao().updateTask(reminderUpdate)
        val loaded = database.reminderDao().getReminderById(reminder.id)
        assertThat(loaded?.id, `is`(reminder.id))
        assertThat(loaded?.title, `is`("title"))
        assertThat(loaded?.description, `is`("description"))
    }

}