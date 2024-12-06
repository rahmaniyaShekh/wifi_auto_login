import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLDecoder

class CaptivePortalInterceptor(private val context: Context, private val textView: TextView) {

    private val connectivityManager: ConnectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    init {
        registerNetworkCallback()
    }

    private fun registerNetworkCallback() {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.d("CaptivePortalInterceptor", "Network available")

                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                val isCaptivePortal = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_CAPTIVE_PORTAL) ?: false

                if (isCaptivePortal) {
                    val url = getCaptivePortalUrl(network)
                    Log.d("CaptivePortalInterceptor", "Captive Portal URL: $url")
                    textView.post {
                        textView.text = "Captive Portal URL: $url"
                    }
                } else {
                    Log.d("CaptivePortalInterceptor", "Not a captive portal")
                }
            }
        }
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        Log.d("CaptivePortalInterceptor", "Network callback registered")
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getCaptivePortalUrl(network: Network): String? {
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        val isCaptivePortal = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_CAPTIVE_PORTAL) ?: false

        return if (isCaptivePortal) {
            fetchCaptivePortalUrl()
        } else {
            Log.d("CaptivePortalInterceptor", "Not a captive portal")
            null
        }
    }

    private fun fetchCaptivePortalUrl(): String? {
        val testUrl = "http://clients3.google.com/generate_204"

        try {
            val connection = URL(testUrl).openConnection() as HttpURLConnection
            connection.instanceFollowRedirects = false
            connection.connect()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP || responseCode == HttpURLConnection.HTTP_MOVED_PERM) {
                val redirectUrl = connection.getHeaderField("Location")
                if (redirectUrl != null) {
                    return URL(redirectUrl).toString()
                } else {
                    Log.e("CaptivePortalInterceptor", "No redirect location found")
                }
            } else if (responseCode == HttpURLConnection.HTTP_OK) {
                return testUrl // No redirect, return original URL
            } else {
                Log.e("CaptivePortalInterceptor", "Invalid response code: $responseCode")
            }
        } catch (e: IOException) {
            Log.e("CaptivePortalInterceptor", "Error fetching captive portal URL: ${e.message}")
        }

        return null
    }


}
