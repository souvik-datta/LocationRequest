package com.udacity.project4.locationreminders.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.utils.EspressoIdlingResource.wrapEspressoIdlingResource
import kotlinx.coroutines.*

/**
 * Concrete implementation of a data source as a db.
 *
 * The repository is implemented so that you can focus on only testing it.
 *
 * @param remindersDao the dao that does the Room db operations
 * @param ioDispatcher a coroutine dispatcher to offload the blocking IO tasks
 */
class RemindersLocalRepository(
    private val remindersDao: RemindersDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ReminderDataSource {


    private val observableReminders = MutableLiveData<Result<List<ReminderDTO>>>()

    /**
     * Get the reminders list from the local db
     * @return Result the holds a Success with all the reminders or an Error object with the error message
     */
    override suspend fun getReminders(): Result<List<ReminderDTO>> = wrapEspressoIdlingResource {
        withContext(ioDispatcher) {
            return@withContext try {
                Result.Success(remindersDao.getReminders())
            } catch (ex: Exception) {
                Result.Error(ex.localizedMessage)
            }
        }
    }

    /**
     * Insert a reminder in the db.
     * @param reminder the reminder to be inserted
     */
    override suspend fun saveReminder(reminder: ReminderDTO) =
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                remindersDao.saveReminder(reminder)
            }
        }

    /**
     * Get a reminder by its id
     * @param id to be used to get the reminder
     * @return Result the holds a Success object with the Reminder or an Error object with the error message
     */
    override suspend fun getReminder(id: String): Result<ReminderDTO> = wrapEspressoIdlingResource {
        withContext(ioDispatcher) {
            try {
                val reminder = remindersDao.getReminderById(id)
                if (reminder != null) {
                    return@withContext Result.Success(reminder)
                } else {
                    return@withContext Result.Error("data not found!")
                }
            } catch (e: Exception) {
                return@withContext Result.Error(e.localizedMessage)
            }
        }
    }

    /**
     * Deletes all the reminders in the db
     */
    override suspend fun deleteAllReminders() {
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                remindersDao.deleteAllReminders()
            }
        }
    }

    override suspend fun refreshReminders() {
        observableReminders.value = getReminders()
    }

    override suspend fun refreshReminder(id: String) {
        refreshReminders()
    }

    override suspend fun observeReminders(): LiveData<Result<List<ReminderDTO>>> {
        return observableReminders
    }

    override suspend fun observeTask(reminderId: String): LiveData<Result<ReminderDTO>> {
        return observableReminders.map { result ->
            when (result) {
                is Result.Success -> {
                    Result.Success(result.data.first { it.id == reminderId })
                }
                is Result.Error -> {
                    Result.Error(result.message)
                }
                else -> Result.Error("")
            }
        }

    }
}
