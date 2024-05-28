package com.example.myapplication

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.ActivityEditTaskBinding
import com.example.myapplication.db.LocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class EditTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditTaskBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var selectedDate: String? = null

        val task = intent.getParcelableExtra<TaskDb>("task")
        task?.let {
            binding.etName.setText(it.name)
            binding.etDescription.setText(it.description)
            binding.tvDate.text = it.time

            binding.buttonSelectDate.setOnClickListener {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog =
                    DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                        selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                        binding.tvDate.text = selectedDate
                    }, year, month, day)

                datePickerDialog.show()
            }
        }
        binding.buttonSaveChanges.setOnClickListener {
            val taskDb = TaskDb(
                task?.id, binding.tvDate.text.toString(),
                binding.etName.text.toString(), binding.etDescription.text.toString()
            )
            lifecycleScope.launch(Dispatchers.IO) {
                LocalDatabase.getInstance(applicationContext).taskDao().insertTask(
                    taskDb
                )
                withContext(Dispatchers.Main){
                    finish()
                }
            }
        }


    }
}
