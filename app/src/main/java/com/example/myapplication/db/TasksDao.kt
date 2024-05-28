package com.example.myapplication.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete

import androidx.room.Query
import androidx.room.Upsert
import com.example.myapplication.TaskDb

@Dao
interface TasksDao {

    @Upsert
    fun insertTask(taskDb: TaskDb)

    @Query("SELECT * FROM tasks")
    fun getTasks():LiveData<List<TaskDb>>

    @Delete
    fun deleteTask(taskDb: TaskDb)
}