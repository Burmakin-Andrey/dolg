package com.example.myapplication

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.DialogAddTaskBinding
import com.example.myapplication.db.LocalDatabase
import com.example.myapplication.tasksAdapter.TasksAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar


class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var tasksAdapter: TasksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tasksAdapter = TasksAdapter()
        binding.rvTasks.adapter = tasksAdapter
        setupSwipeListener(binding.rvTasks)
        LocalDatabase.getInstance(applicationContext).taskDao().getTasks().observe(this){
            tasksAdapter.submitList(it)
        }
        setOnClickListener()
    }


    private fun setOnClickListener(){

        tasksAdapter.onTaskItemClickListener = {
            val intent = Intent(this, EditTaskActivity::class.java).apply {
                putExtra("task", it)
            }
            startActivity(intent)
        }

        binding.buttonAddItem.setOnClickListener{
            val dialogBinding = DialogAddTaskBinding.inflate(layoutInflater)
            var selectedDate: String? = null
            dialogBinding.buttonSelectDate.setOnClickListener{
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                    dialogBinding.textViewSelectedDate.text = selectedDate
                }, year, month, day)

                datePickerDialog.show()
            }
            val dialog = AlertDialog.Builder(this)
                .setTitle("Добавить задачу")
                .setView(dialogBinding.root)
                .setPositiveButton("Добавить"){ dialog, which ->
                    lifecycleScope.launch(Dispatchers.IO) {
                        val name = dialogBinding.editTextAddName.text.toString()
                        val description = dialogBinding.editTextAddDescription.text.toString()
                        val taskDb = selectedDate?.let { time -> TaskDb(id = null, time = time, name = name, description = description) }
                        taskDb?.let { task ->
                            LocalDatabase.getInstance(applicationContext).taskDao().insertTask(
                                task
                            )
                        }
                    }

                }

            dialog.show()
        }
    }


    private fun setupSwipeListener(tasksList: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = tasksAdapter.currentList[viewHolder.adapterPosition]
                lifecycleScope.launch(Dispatchers.IO) {
                    LocalDatabase.getInstance(applicationContext).taskDao().deleteTask(
                        item
                    )
                }

            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(tasksList)
    }
}

