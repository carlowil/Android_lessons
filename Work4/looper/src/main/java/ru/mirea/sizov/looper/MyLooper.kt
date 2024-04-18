package ru.mirea.sizov.looper

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log

class MyLooper(private val mainHandler: Handler) : Thread() {
    var mHandler: Handler? = null
    override fun run() {
        Log.d("MyLooper", "run")
        Looper.prepare()
        mHandler = object : Handler(Looper.myLooper()!!) {
            override fun handleMessage(msg: Message) {
                val data = msg.getData().getString("KEY")
                val age = msg.data.getInt("TIME")
                sleep((age * 1000).toLong())
                Log.d("MyLooper	get	message: ", "${data!!} $age")
                val count = data.length
                val message = Message()
                val bundle = Bundle()
                bundle.putString(
                    "result",
                    String.format("The number of letters in the word %s is %d, age = %d", data, count, age)
                )
                message.data = bundle

                mainHandler.sendMessage(message)
            }
        }
        Looper.loop()
    }
}