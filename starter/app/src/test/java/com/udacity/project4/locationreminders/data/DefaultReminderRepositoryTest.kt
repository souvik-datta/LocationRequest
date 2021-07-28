package com.udacity.project4.locationreminders.data

import android.app.Application
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.data.local.RemindersDao
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsEqual
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi

class DefaultReminderRepositoryTest {
    private val task1 =
        ReminderDTO("Title1", "Description1", "location1", 22.578, 88.6048, "ID1")
    private val task2 =
        ReminderDTO("Title2", "Description2", "location2", 22.59878, 88.7848, "ID2")
    private val task3 =
        ReminderDTO("Title3", "Description3", "location3", 22.58587, 88.65848, "ID3")
    private val remoteReminder = listOf(task1, task2).sortedBy { it.id }
    private val localReminder = listOf(task3).sortedBy { it.id }
    private lateinit var tasksRemoteDataSource: FakeDataSource
    private lateinit var tasksLocalDataSource: FakeDataSource
    private lateinit var viewModel: SaveReminderViewModel

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun createRepository() {
        tasksRemoteDataSource = FakeDataSource(remoteReminder.toMutableList())
        tasksLocalDataSource = FakeDataSource(localReminder.toMutableList())
        viewModel = SaveReminderViewModel(Application(), tasksRemoteDataSource)
    }
    @Test
    fun getReminder_requestsAllRemindersFromDataSource() = mainCoroutineRule.runBlockingTest {
        // When tasks are requested from the tasks repository
        val tasks = tasksRemoteDataSource.getReminders() as Result.Success
        // Then tasks are loaded from the remote data source
        Assert.assertThat(tasks.data, IsEqual(remoteReminder))
    }

}