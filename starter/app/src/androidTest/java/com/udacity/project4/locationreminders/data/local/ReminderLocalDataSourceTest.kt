package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.junit.*
import org.junit.runner.RunWith
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.core.IsEqual


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest

class ReminderLocalDataSourceTest {
    private lateinit var localDataSource: ReminderLocalDataSource
    private lateinit var database: RemindersDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        localDataSource =
            ReminderLocalDataSource(
                database.reminderDao(),
                Dispatchers.Main
            )
    }

    @Test
    fun saveTask_retrievesTask() = runBlocking {
        // GIVEN - a new task saved in the database
        val newTask = ReminderDTO("title", "description", "22.8745, 88.6971", 22.8745, 88.6971)
        localDataSource.saveReminder(newTask)

        // WHEN  - Task retrieved by ID
        val result = localDataSource.getReminder(newTask.id)

        // THEN - Same task is returned
        // assertThat(result.succeeded, CoreMatchers.`is`(true))
        result as Result.Success
        assertThat(result.data.title, CoreMatchers.`is`("title"))
        assertThat(result.data.description, CoreMatchers.`is`("description"))
    }

    @Test
    fun completeTask_retrievedTaskIsComplete() = runBlocking {
        // Given a new task in the persistent repository
        val newTask = ReminderDTO("title", "description", "22.8745, 88.6971", 22.8745, 88.6971)
        localDataSource.saveReminder(newTask)
        // When completed in the persistent repository
        val result = localDataSource.getReminder(newTask.id)
        // Then the task can be retrieved from the persistent repository and is complete
        //  assertThat(result.succeeded, `is`(true))
        result as Result.Success
        assertThat(result.data.title, `is`(newTask.title))
    }

    @After
    fun cleanUp() {
        database.close()
    }

}