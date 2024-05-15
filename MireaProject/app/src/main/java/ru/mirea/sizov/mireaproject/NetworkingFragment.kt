package ru.mirea.sizov.mireaproject

import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NetworkingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NetworkingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mainTextView : TextView
    private lateinit var descTextView : TextView
    private lateinit var tempTextView : TextView
    private lateinit var feelsTextView : TextView
    private lateinit var windSpeedTextView : TextView
    private lateinit var updateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_networking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainTextView = view.findViewById(R.id.mainTextView)
        descTextView = view.findViewById(R.id.descTextView)
        tempTextView = view.findViewById(R.id.tempTextView)
        feelsTextView = view.findViewById(R.id.feelsTextView)
        windSpeedTextView = view.findViewById(R.id.windSpeedTextView)
        updateButton = view.findViewById(R.id.updateButton)

        updateButton.setOnClickListener {
            val connectivityManager =
                requireActivity().getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
            var networkinfo: NetworkInfo? = null
            networkinfo = connectivityManager.activeNetworkInfo
            if (networkinfo != null && networkinfo.isConnected) {
                DownloadWeatherTask().execute("https://api.openweathermap.org/data/2.5/weather?lat=55.755826&lon=37.6173&units=metric&appid=e6111b82c644442c5f67cb1364abe16b") // запуск нового потока
            } else {
                Toast.makeText(requireContext(), "Нет интернета", Toast.LENGTH_SHORT).show()
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NetworkingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NetworkingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private inner class DownloadWeatherTask : AsyncTask<String?, Void?, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): String {
            try {
                return downloadIpInfo(params[0]!!)
            } catch (e: IOException) {
                e.printStackTrace()
                return "error"
            }
        }

        override fun onPostExecute(result: String) {
            Log.d(MainActivity::class.java.simpleName, result)
            try {
                val responseJson = JSONObject(result)
                val weather = responseJson.getJSONArray("weather").getJSONObject(0)
                val main = responseJson.getJSONObject("main")
                val wind = responseJson.getJSONObject("wind")

                mainTextView.text = "Main: ${weather.getString("main")}"
                descTextView.text = "Description: ${weather.getString("description")}"
                tempTextView.text = "Temp: ${main.getString("temp")} °C"
                feelsTextView.text = "Feels like: ${main.getString("feels_like")} °C"
                windSpeedTextView.text = "Wind speed: ${wind.getString("speed")} meter/sec"

                Log.d(
                    MainActivity::class.java.simpleName,
                    "Response: $responseJson"
                )
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            super.onPostExecute(result)
        }

        @Throws(IOException::class)
        private fun downloadIpInfo(address: String): String {
            var inputStream: InputStream? = null
            var data = ""
            try {
                val url = URL(address)
                val connection = url.openConnection() as HttpURLConnection
                connection.readTimeout = 100000
                connection.connectTimeout = 100000
                connection.requestMethod = "GET"
                connection.instanceFollowRedirects = true
                connection.useCaches = false
                connection.doInput = true
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                    inputStream = connection.inputStream
                    val bos = ByteArrayOutputStream()
                    var read = 0
                    while ((inputStream.read().also { read = it }) != -1) {
                        bos.write(read)
                    }
                    bos.close()
                    data = bos.toString()
                } else {
                    data = connection.responseMessage + ". Error Code: " + responseCode
                }
                connection.disconnect()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                inputStream?.close()
            }
            return data
        }
    }
}