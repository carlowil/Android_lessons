package ru.mirea.simplefragmentapp

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


class MainActivity : AppCompatActivity() {
    lateinit var fragment1: Fragment
    lateinit var fragment2: Fragment
    lateinit var fragmentManager: FragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        fragment1 = FirstFragment()
        fragment2 = SecondFragment()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun onClick(view: View) {
        fragmentManager = supportFragmentManager
        when (view.id) {
            R.id.btnFirstFragment -> fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment1).commit()

            R.id.btnSecondFragment -> fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment2).commit()

            else -> {}
        }
    }
}