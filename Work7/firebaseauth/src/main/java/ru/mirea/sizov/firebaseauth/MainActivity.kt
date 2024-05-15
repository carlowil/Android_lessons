package ru.mirea.sizov.firebaseauth

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ru.mirea.sizov.firebaseauth.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName;
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance();


        binding.regButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val pass = binding.passwordEditText.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                createAccount(email, pass)
            } else {
                Toast.makeText(this, "Empty email or password!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val pass = binding.passwordEditText.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                signIn(email, pass)
            } else {
                Toast.makeText(this, "Empty email or password!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.verifyEmailButton.setOnClickListener {
            sendEmailVerification()
        }

        binding.signOutButton.setOnClickListener {
            signOut()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user:FirebaseUser?){
        if (user != null) {
            binding.statusTextView.text = getString(R.string.emailpassword_status_fmt,
                user.email, user.isEmailVerified
            );
            binding.detailTextView.text = getString(R.string.firebase_status_fmt, user.uid);
            binding.emailFields.visibility = View.GONE;
            binding.passFields.visibility = View.GONE;
            binding.signOutButtons.visibility = View.VISIBLE;
            binding.verifyEmailButton.setEnabled(!user.isEmailVerified);
        } else {
            binding.statusTextView.setText(R.string.signed_out);
            binding.detailTextView.text = null;
            binding.emailFields.visibility = View.VISIBLE;
            binding.passFields.visibility = View.VISIBLE;
            binding.signOutButtons.visibility = View.GONE;
        }
    }

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "CreateAccount:$email")
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d(TAG, "createUserWithEmail:success")
                val user = mAuth.currentUser
                updateUI(user)
            } else {
                Log.w(TAG, "createUserWithEmail:failure", it.exception)
                Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                updateUI(null)
            }
        }
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = mAuth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", it.exception)
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
                if (!it.isSuccessful) {
                    binding.statusTextView.text = R.string.auth_failed.toString()
                }
            }
    }

    private fun signOut() {
        mAuth.signOut()
        updateUI(null)
    }

    private fun sendEmailVerification() {
        binding.verifyEmailButton.setEnabled(false)
        val user = mAuth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener {
            binding.verifyEmailButton.setEnabled(true)
            if (it.isSuccessful) {
                Toast.makeText(this,
                    "Verification email sent to ${user.email}",
                    Toast.LENGTH_SHORT).show()
            } else {
                Log.e(TAG, "sendEmailVerification", it.exception)
                Toast.makeText(this,
                    "Failed to send verification email.",
                    Toast.LENGTH_SHORT).show()
            }
        }

    }


}