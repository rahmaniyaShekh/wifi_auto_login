import android.content.Context
import android.webkit.JavascriptInterface

class MyJavaScriptInterface(private val context: Context) {

    // Define an interface for the listener
    interface OnCredentialsReceivedListener {
        fun onCredentialsReceived(username: String?, password: String?)
    }

    interface OnLoginBtnClickedListener {
        fun onLoginBtnClicked()
    }

    // Listener instance
    private var credentialsReceivedListener: OnCredentialsReceivedListener? = null

    // Method to set the listener
    fun setOnCredentialsReceivedListener(listener: OnCredentialsReceivedListener) {
        credentialsReceivedListener = listener
    }

    // Listener instance
    private var loginBtnClickedListener: OnLoginBtnClickedListener? = null

    // Method to set the listener
    fun setOnLoginBtnClickedListener(listener: OnLoginBtnClickedListener) {
        loginBtnClickedListener = listener
    }

    @JavascriptInterface
    fun onReceiveValue(username: String, password: String) {
        // Check if listener is set
        if (credentialsReceivedListener != null) {
            // Invoke the callback method of the listener with the received credentials
            credentialsReceivedListener?.onCredentialsReceived(username, password)
        }
    }

    @JavascriptInterface
    fun onLoginClicked() {
        // Check if listener is set
        if (loginBtnClickedListener != null) {
            // Invoke the callback method of the listener with the received credentials
            loginBtnClickedListener?.onLoginBtnClicked()
        }
    }
}
