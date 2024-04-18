package ru.mirea.sizov.thread

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.mirea.sizov.thread.databinding.ActivityMainBinding
import java.lang.Thread.sleep

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var counter : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mainThread = Thread.currentThread()
        binding.potok.text = "Имя текущего потока: ${mainThread.name}"

        var count = 0.0

        binding.countButton.setOnClickListener {
            var numberThread = counter++
            val days = binding.countDaysEditText.text.toString().toDouble()
            val lessons = binding.countLessonsEditText.text.toString().toDouble()
            val newThread = Thread {
                synchronized(this) {
                    count = avgLessons(days, lessons)
                    Log.d("ThreadProject", "Среднее количество пар: ${count}")
                }
            }

            newThread.name = "№$numberThread МОЙ НОМЕР ГРУППЫ: БСБО-10-21, НОМЕР ПО СПИСКУ: 23. Я АНИМЕШНИК"

            binding.potok.text = "${binding.potok.text}\nНовое имя потока: ${newThread.name}"

            Log.d("ThreadProject", "Запущен поток №$numberThread студентом группы № БСБО-10-21 номер по списку 23")
            newThread.start()
            // Кривовато вышло :D
            sleep(10)

            binding.avgTextView.text = "Среднее количество пар: ${count}"
            Log.d("ThreadProject", "Выполнен поток №$numberThread")
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun avgLessons(days : Double, lessons: Double) : Double {
        return lessons / days
    }

}