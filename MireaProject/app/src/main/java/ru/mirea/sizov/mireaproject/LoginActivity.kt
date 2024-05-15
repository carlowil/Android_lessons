package ru.mirea.sizov.mireaproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.mirea.sizov.mireaproject.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authHandler = AuthHandler()

        binding.regButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val email = binding.emailEditText.text.toString()
            val pass = binding.passwordEditText.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                if (!authHandler.createAccount(email, pass)) {
                    Toast.makeText(this, "Bad registration! Check your login or password!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Empty email or password!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val email = binding.emailEditText.text.toString()
            val pass = binding.passwordEditText.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                if (!authHandler.signIn(email, pass)) {
                    Toast.makeText(this, "Bad auth! Check your login or password!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Empty email or password!", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}