package com.udacity.project4.locationreminders.reminderslist

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.`is`
import com.google.common.truth.ExpectFailure.assertThat
import com.udacity.project4.MyApp
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.FakeReminderTestRepository
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.RemindersDao
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.utils.EspressoIdlingResource
import junit.framework.Assert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    // provide testing to the RemindersListViewModel and its live data objects
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Subject under test
    private lateinit var reminderListViewModel: RemindersListViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var tasksRepository: FakeReminderTestRepository

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    @Before
    fun setupReminderListViewModel() {
        tasksRepository = FakeReminderTestRepository()
        reminderListViewModel = RemindersListViewModel(Application(),tasksRepository)
    }

    @Test
    fun loadReminders_loading() {
        mainCoroutineRule.pauseDispatcher()
        reminderListViewModel.refresh()
        Assert.assertTrue(reminderListViewModel.showLoading.getOrAwaitValue())
        mainCoroutineRule.resumeDispatcher()
        Assert.assertTrue(reminderListViewModel.showLoading.getOrAwaitValue() == false)
    }
    @Test
    fun loadReminderssWhenUnavailable_callErrorToDisplay() {
        tasksRepository.setReturnError(true)
        reminderListViewModel.refresh()
        MatcherAssert.assertThat(reminderListViewModel.empty.getOrAwaitValue(), `is`(true))
        MatcherAssert.assertThat(reminderListViewModel.error.getOrAwaitValue(), `is`(true))
    }

}