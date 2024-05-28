package com.example.myapplication.tasksAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.myapplication.TaskDb


object TasksDiffCallback : DiffUtil.ItemCallback<TaskDb>() {

    override fun areItemsTheSame(oldItem: TaskDb, newItem: TaskDb): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: TaskDb, newItem: TaskDb): Boolean {
        return oldItem == newItem
    }
}