package ru.mirea.sizov.securesharedpreferences

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val maikovsieTextView = findViewById<TextView>(R.id.maikovsieTextView)

        try {
            val keyGenParametresSpec = MasterKeys.AES256_GCM_SPEC
            val mainKeyAlias = MasterKeys.getOrCreate(keyGenParametresSpec)

            val secureSharedPreferences = 	EncryptedSharedPreferences.create(
                "secret_shared_prefs",
                mainKeyAlias,
                baseContext,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            secureSharedPreferences.edit().putString("secure", "Maikovsie").apply()

            maikovsieTextView.text = secureSharedPreferences.getString("secure", "Нету данных :( (Как 0_o????)")

        } catch (e:Exception) {
            throw RuntimeException(e)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}