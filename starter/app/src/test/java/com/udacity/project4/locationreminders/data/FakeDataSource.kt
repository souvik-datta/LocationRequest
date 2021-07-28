package com.udacity.project4.locationreminders.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import java.lang.Exception

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(var remindertask: MutableList<ReminderDTO>? = mutableListOf()) :
    ReminderDataSource {

    //  Create a fake data source to act as a double to the real data source
    private val observableReminders = MutableLiveData<Result<List<ReminderDTO>>>()

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        remindertask?.let { return Result.Success(ArrayList(it)) }
        return Result.Error(Exception("Reminder not found").toString())
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        remindertask?.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        remindertask?.let { return Result.Success(it.filter { it.id == id }[0]) }
        return Result.Error(Exception("data not found").toString())
    }

    override suspend fun deleteAllReminders() {
        remindertask?.clear()
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