package com.example.myapplication.tasksAdapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.myapplication.TaskDb
import com.example.myapplication.databinding.ItemTaskBinding

class TasksAdapter : ListAdapter<TaskDb, TasksViewHolder>(TasksDiffCallback) {

    var onTaskItemClickListener: ((TaskDb) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val taskItem = getItem(position)
        holder.binding.root.setOnClickListener {
            onTaskItemClickListener?.invoke(taskItem)
        }
        with(holder.binding){
            with(taskItem){
                tvTime.text = this.time
                tvName.text = this.name
            }
        }
    }

}