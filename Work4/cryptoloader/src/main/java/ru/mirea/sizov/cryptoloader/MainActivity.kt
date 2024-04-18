package ru.mirea.sizov.cryptoloader

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import java.security.InvalidKeyException
import java.security.InvalidParameterException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<String> {
    val TAG = this.javaClass.getSimpleName()
    private val loaderID = 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val cryptoButton = findViewById<Button>(R.id.cryptoButton)
        val cryptoEditText = findViewById<EditText>(R.id.cryptoEditText)

        cryptoButton.setOnClickListener {
            val key = generateKey()
            val text = cryptoEditText.text.toString()
            val shiper = encryptMsg(text, key)
            val bundle = Bundle()
            bundle.putByteArray(MyLoader.ARG_WORD, shiper)
            bundle.putByteArray("key", key.encoded)
            LoaderManager.getInstance(this).initLoader(loaderID, bundle, this)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun onClickButton(view: View?) {
        val bundle = Bundle()
        bundle.putString(MyLoader.ARG_WORD, "mirea")
        LoaderManager.getInstance(this).initLoader(loaderID, bundle, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): MyLoader {
        return when(id) {
            loaderID -> {
                Toast.makeText(this, "onCreateLoader: $id", Toast.LENGTH_SHORT).show()
                MyLoader(this, args)
            }
            else -> {
                throw InvalidParameterException("Invalid loader id")
            }
        }
    }

    override fun onLoaderReset(loader: Loader<String>) {
        Log.d(TAG, "onLoaderReset")
    }

    override fun onLoadFinished(loader: Loader<String>, data: String?) {
        if (loader.id == loaderID) {
            Log.d(TAG, "onLoadFinished: $data")
            Toast.makeText(this, "onLoadFinished: $data", Toast.LENGTH_SHORT).show()
        }
    }

    fun generateKey(): SecretKey {
        return try {
            val sr = SecureRandom.getInstance("SHA1PRNG")
            sr.setSeed("any	data	used	as	random	seed".toByteArray())
            val kg = KeyGenerator.getInstance("AES")
            kg.init(256, sr)
            SecretKeySpec(kg.generateKey().encoded, "AES")
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }

    fun encryptMsg(message: String, secret: SecretKey?): ByteArray {
        var cipher: Cipher? = null
        return try {
            cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.ENCRYPT_MODE, secret)
            cipher.doFinal(message.toByteArray())
        } catch (e: NoSuchAlgorithmException) {
            throw java.lang.RuntimeException(e)
        } catch (e: NoSuchPaddingException) {
            throw java.lang.RuntimeException(e)
        } catch (e: InvalidKeyException) {
            throw java.lang.RuntimeException(e)
        } catch (e: BadPaddingException) {
            throw java.lang.RuntimeException(e)
        } catch (e: IllegalBlockSizeException) {
            throw java.lang.RuntimeException(e)
        }
    }

}


