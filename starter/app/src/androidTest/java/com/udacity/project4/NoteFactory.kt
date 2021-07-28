package com.udacity.project4

import com.udacity.project4.locationreminders.data.dto.ReminderDTO

class NoteFactory {
    companion object Factory{
        fun makeNoteEntity(): ReminderDTO {
            return ReminderDTO(
                DataFactory.randomUuid(),
                DataFactory.randomUuid(),
                DataFactory.randomUuid(),
                DataFactory.randomDouble(),
                DataFactory.randomDouble(),
                DataFactory.randomUuid()
            )
        }
        fun makeNote(): ReminderDTO {
            return ReminderDTO(
                DataFactory.randomUuid(),
                DataFactory.randomUuid(),
                DataFactory.randomUuid(),
                DataFactory.randomDouble(),
                DataFactory.randomDouble(),
                DataFactory.randomUuid()
            )
        }

        fun makeNoteList(count: Int): List<ReminderDTO> {
            val notes = mutableListOf<ReminderDTO>()
            repeat(count) {
                notes.add(makeNote())
            }
            return notes
        }

    }
}