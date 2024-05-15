package ru.mirea.sizov.mireaproject

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class AuthHandler() {
    private val TAG = "AuthHandler"
    private var mAuth: FirebaseAuth? = null

    init {
        mAuth = FirebaseAuth.getInstance()
    }

    fun createAccount(email: String, password: String) : Boolean {
        var flag = true
        Log.d(TAG, "CreateAccount:$email")
        try {
            mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", it.exception)
                        flag = false
                    }
                }
        } catch (e:NullPointerException) {
            Log.w(TAG, "Please init before use auth methods")
            flag = false
        }
        return flag
    }

    fun signIn(email: String, password: String) : Boolean {
        var flag = true
        Log.d(TAG, "signIn:$email")
        try {
            mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", it.exception)
                        flag = false
                    }
                }
        } catch (e:NullPointerException) {
            Log.w(TAG, "Please init before use auth methods")
            flag = false
        }
        return flag
    }

    fun signOut() : Boolean {
        var flag = true
        try {
            mAuth!!.signOut()
        } catch (e:NullPointerException) {
            Log.w(TAG, "Please use init before use auth methods")
            flag = false
        }
        return flag
    }

    fun sendEmailVerification() : Boolean {
        var flag = true
        val user = mAuth!!.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener {
            if (it.isSuccessful) {
                Log.e(TAG, "sendEmailVerification:success")
            } else {
                Log.e(TAG, "sendEmailVerification", it.exception)
                flag = false
            }
        }
        return flag
    }
}