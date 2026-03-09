package com.showedup.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a Subject/Course that a user can define once and reuse
 * when adding classes to their timetable.
 */
@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val code: String = "",
    val instructor: String = "",
    val defaultRoom: String = "",
    val colorIndex: Int = 0 // Used to keep a consistent color in the UI
)
