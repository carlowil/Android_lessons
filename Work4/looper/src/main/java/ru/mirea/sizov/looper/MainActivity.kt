package ru.mirea.sizov.looper

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.mirea.sizov.looper.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mainThreadHandler: Handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                Log.d(MainActivity::class.java.getSimpleName(),
                    "Task execute.	This is result: " + msg.getData()
                        .getString("result")
                )
            }
        }

        val myLooper = MyLooper(mainThreadHandler)
        myLooper.start()

        binding.buttonMirea.setOnClickListener {
            val age = binding.editTextMirea2.text.toString().toInt()
            val job = binding.editTextMirea1.text.toString()
            val msg = Message.obtain()
            val bundle = Bundle()
            bundle.putString("KEY", job)
            bundle.putInt("TIME", age)
            msg.data = bundle
            myLooper.mHandler?.sendMessage(msg)
        }

    }
}