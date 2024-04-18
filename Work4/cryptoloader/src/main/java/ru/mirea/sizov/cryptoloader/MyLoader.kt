package ru.mirea.sizov.cryptoloader

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import androidx.loader.content.AsyncTaskLoader
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


class MyLoader(context: Context, args: Bundle?) :
    AsyncTaskLoader<String?>(context) {
    private var firstName: String? = null

    init {
        if (args != null) {
            val cryptText = args.getByteArray(ARG_WORD)
            val key = args.getByteArray("key")
            val originalKey = SecretKeySpec(key, 0, key!!.size, "AES")
            firstName = decryptMsg(cryptText, originalKey)
        }
    }

    override fun onStartLoading() {
        super.onStartLoading()
        forceLoad()
    }

    override fun loadInBackground(): String? {
        //	emulate	long-running	operation
        SystemClock.sleep(5000)
        return firstName
    }

    fun decryptMsg(cipherText: ByteArray?, secret: SecretKey?): String {
        /*	Decrypt	the	message	*/
        return try {
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.DECRYPT_MODE, secret)
            String(cipher.doFinal(cipherText))
        } catch (e: NoSuchAlgorithmException) {
            throw java.lang.RuntimeException(e)
        } catch (e: NoSuchPaddingException) {
            throw java.lang.RuntimeException(e)
        } catch (e: IllegalBlockSizeException) {
            throw java.lang.RuntimeException(e)
        } catch (e: BadPaddingException) {
            throw java.lang.RuntimeException(e)
        } catch (e: InvalidKeyException) {
            throw java.lang.RuntimeException(e)
        }
    }

    companion object {
        const val ARG_WORD = "word"
    }
}