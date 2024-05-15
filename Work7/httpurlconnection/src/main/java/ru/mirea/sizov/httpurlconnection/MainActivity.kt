package ru.mirea.sizov.httpurlconnection

import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.transition.Visibility
import org.json.JSONException
import org.json.JSONObject
import ru.mirea.sizov.httpurlconnection.databinding.ActivityMainBinding
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.sendButton.setOnClickListener {
            val connectivityManager =
                getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            var networkinfo: NetworkInfo? = null
            networkinfo = connectivityManager.activeNetworkInfo
            if (networkinfo != null && networkinfo.isConnected) {
                DownloadPageTask().execute("https://ipinfo.io/json") // запуск нового потока
            } else {
                Toast.makeText(this, "Нет интернета", Toast.LENGTH_SHORT).show()
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private inner class DownloadPageTask : AsyncTask<String?, Void?, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            binding.textView.text = "Загружаем..."
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
            binding.textView.text = ""
            binding.textView.visibility = View.INVISIBLE
            Log.d(MainActivity::class.java.simpleName, result)
            try {
                val responseJson = JSONObject(result)
                Log.d(
                    MainActivity::class.java.simpleName,
                    "Response: $responseJson"
                )
                val ip = responseJson.getString("ip")

                binding.ipTextView.text = "IP: $ip"
                binding.cityTextView.text = "City: ${responseJson.getString("city")}"
                binding.hostnameTextView.text = "Hostname: ${responseJson.getString("hostname")}"
                binding.regionTextView.text = "Region: ${responseJson.getString("region")}"
                binding.countryTextView.text = "Country: ${responseJson.getString("country")}"
                binding.locTextView.text = "Country: ${responseJson.getString("loc")}"
                binding.orgTextView.text = "Organization: ${responseJson.getString("org")}"
                binding.postalTextView.text = "Postal: ${responseJson.getString("postal")}"
                binding.timezoneTextView.text = "Timezone: ${responseJson.getString("timezone")}"

                val loc = responseJson.getString("loc").split(",")

                DownloadWeatherTask().execute("https://api.openweathermap.org/data/2.5/weather?lat=${loc[0]}&lon=${loc[1]}&units=metric&appid=e6111b82c644442c5f67cb1364abe16b")


                Log.d(
                    MainActivity::class.java.simpleName,
                    "IP: $ip"
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

    private inner class DownloadWeatherTask : AsyncTask<String?, Void?, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            binding.weatherTextView.text = "Загружаем..."
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
            binding.weatherTextView.text = ""
            binding.weatherTextView.visibility = View.INVISIBLE
            Log.d(MainActivity::class.java.simpleName, result)
            try {
                val responseJson = JSONObject(result)
                val weather = responseJson.getJSONArray("weather").getJSONObject(0)
                val main = responseJson.getJSONObject("main")
                val wind = responseJson.getJSONObject("wind")

                binding.mainTextView.text = "Main: ${weather.getString("main")}"
                binding.descTextView.text = "Description: ${weather.getString("description")}"
                binding.tempTextView.text = "Temp: ${main.getString("temp")} °C"
                binding.feelsTextView.text = "Feels like: ${main.getString("feels_like")} °C"
                binding.windSpeedTextView.text = "Wind speed: ${wind.getString("speed")} meter/sec"
                binding.weatherLayout.visibility = View.VISIBLE
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