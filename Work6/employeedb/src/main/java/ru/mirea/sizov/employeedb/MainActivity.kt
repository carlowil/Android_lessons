package ru.mirea.sizov.employeedb

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {

    private val TAG = "SUPERHEROTAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val db = App.getInstance().database
        val superheroDao = db.superheroDao()

        var superhero:Superhero = Superhero()

        superhero.id = 1
        superhero.name = "Venom"
        superhero.universe = "Marvel"

        superheroDao?.insert(superhero)

        val superheroes = superheroDao?.all

        superhero = superheroDao?.getById(1)!!

        superhero.name = "Deadpool"
        superheroDao.update(superhero)
        Log.d(TAG, "${superhero.name} ${superhero.universe}")


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}