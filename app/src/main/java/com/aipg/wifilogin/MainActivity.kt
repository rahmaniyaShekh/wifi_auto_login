package com.aipg.wifilogin

import CaptivePortalInterceptor
import MyJavaScriptInterface
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.graphics.Color
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aipg.wifilogin.localData.dataClass
import com.aipg.wifilogin.localData.dataViewModel
import com.google.android.material.card.MaterialCardView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity(), MyJavaScriptInterface.OnCredentialsReceivedListener,
    MyJavaScriptInterface.OnLoginBtnClickedListener {
    lateinit var code: String

    var currentCostumerTodayDate: String = "12 Oct 2023"
    var dataRegistered: Boolean = false
    var attempted: Boolean = false
    var autoDie: Int = 0

    private val LOGIN_PAGE_URL = "https://192.168.1.250/connect"

    //database variables
    lateinit var dataItems :List<dataClass> //list which store all permanent stock
    lateinit var dataItemsViewModel: dataViewModel

    //views variables
    lateinit var floatingCard : MaterialCardView
    lateinit var exitText : TextView
    lateinit var autoDieLL : LinearLayout
    lateinit var autoDieCheckBox : CheckBox

    private lateinit var textView: TextView
    private lateinit var captivePortalInterceptor: CaptivePortalInterceptor

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setting theme
        setTheme(R.style.Theme_WifiLogin)

        setContentView(R.layout.activity_main)

        // Make the status bar transparent
        // Make the status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.WHITE

            // Set the status bar text color to black for white status bar
            val decor = getWindow().decorView
            if (isStatusBarLight()) {
                decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decor.systemUiVisibility = 0
            }
        }



        textView = findViewById(R.id.textView)
        captivePortalInterceptor = CaptivePortalInterceptor(this, textView)

        //getting button and checkboxes
        floatingCard = findViewById<MaterialCardView>(R.id.floatingCard)
        exitText = findViewById<TextView>(R.id.exitText)
        autoDieLL = findViewById<LinearLayout>(R.id.linearLayoutForAutoDie)
        autoDieCheckBox = findViewById<CheckBox>(R.id.checkAutoDie)

        //for initialisation of current data such as date
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentCostumerTodayDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm:ss"))
        }

        //dataItems view model
        dataItemsViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(dataViewModel::class.java)
        dataItemsViewModel.allData.observe(this, Observer { list ->
            list?.let {
                dataItems = it
                if (dataItems.isNotEmpty()) {
                    dataRegistered = true
                }
                dataLoaded()
            }
        })

        //check box listner
        autoDieCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            // on below line we are checking
            // if check box is checked.
            if (isChecked) {
                autoDie = 1
            } else {
                autoDie = 0
            }
        }

    }

    private fun isStatusBarLight(): Boolean {
        // Check if the status bar color is light (e.g., white)
        return ColorUtils.calculateLuminance(window.statusBarColor) > 0.5
    }
    // Callback method to receive the credentials
    override fun onCredentialsReceived(username: String?, password: String?) {
        // Handle the received credentials here
        //Toast.makeText(this, "credentials\nUsername: $username\nPassword: $password", Toast.LENGTH_SHORT).show()
        if (username != null && password != null) {
            if (username.isNotEmpty() && password.isNotEmpty()) {
                val newItem = dataClass(
                    username,
                    password,
                    autoDie,
                    currentCostumerTodayDate
                )
                dataItemsViewModel.insertStock(newItem)
            } else {
                Toast.makeText(this, "Please Enter valid username and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onLoginBtnClicked() {
        //Toast.makeText(this, "login btn clicked", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({
            if (dataRegistered && dataItems[0].autoDie == 1) {
                onBackPressed()
            }
        }, 800)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun dataLoaded() {
        if (!dataRegistered) {
            autoDieLL.visibility = View.VISIBLE
            exitText.visibility = View.INVISIBLE
            code = """
                const code = `
                document.getElementById('loginButton').innerHTML = '<a class="button" style="display: flex; flex-direction: column; justify-content: center; text-align: center;" onclick="handleButtonClick()">Save & Log In</a>';
                function handleButtonClick(){
                    var username = document.getElementById("LoginUserPassword_auth_username").value;
                    var password = document.getElementById("LoginUserPassword_auth_password").value;
                  
                    Android.onReceiveValue(username, password);
                }`;
                             
                const scriptElement = document.createElement('script');
                scriptElement.setAttribute('type', 'text/javascript');
                scriptElement.textContent = code;
                document.documentElement.appendChild(scriptElement);
                """
        } else {
            autoDieLL.visibility = View.INVISIBLE
            exitText.visibility = View.VISIBLE
            floatingCard.isClickable = true
            code = """
                
                const code = `
                function checkAvail() {
                    setTimeout(() => {
                        if (document.getElementById("LoginUserPassword_auth_username") == null) { 
                            checkAvail();
                        } else {
                            var u = document.getElementById("LoginUserPassword_auth_username");
                            var p = document.getElementById("LoginUserPassword_auth_password");
                            u.value = '${dataItems[0].usernameSt}';
                            u.dispatchEvent(new Event('change'));
                            p.value = '${dataItems[0].passwordSt}';
                            u.dispatchEvent(new Event('change'));
                            
                            checkAndSubmit();
                        }
                    }, 200);
                }
                
                function checkAndSubmit() {
                    setTimeout(() => {
                        if(!document.getElementById('UserCheck_Logoff_Button_span')) {
                            oAuthentication.submitActiveForm();
                            checkAndSubmit();
                        } else {
                            document.getElementById('UserCheck_Logoff_Button_span').innerText = "Success";
                            Android.onLoginClicked();
                        }                       
                    }, 100);
                }
                checkAvail();`;
                
                
                setTimeout(() => {
                    const scriptElement = document.createElement('script');
                    scriptElement.setAttribute('type', 'text/javascript');
                    scriptElement.textContent = code;
                    document.documentElement.appendChild(scriptElement);
                }, 100);
                
                """
        }

        val webView = findViewById<WebView>(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = MyWebViewClient()
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        val jsInterface = MyJavaScriptInterface(this)
        // Set the listener in the MyJavaScriptInterface
        jsInterface.setOnCredentialsReceivedListener(this)
        jsInterface.setOnLoginBtnClickedListener(this)
        // Add MyJavaScriptInterface to WebView
        webView.addJavascriptInterface(jsInterface, "Android")
        webView.loadUrl(LOGIN_PAGE_URL)
    }


    private inner class MyWebViewClient : WebViewClient() {
        @Suppress("OverridingDeprecatedMember")
        override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
            view?.loadUrl(url)
            return true
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            view?.loadUrl(request?.url.toString())
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            // Inject JavaScript code to fill in the login form
            Handler().postDelayed({
                //Toast.makeText(applicationContext, "Page finished loading", Toast.LENGTH_SHORT).show()
            view?.evaluateJavascript(
                code.trimIndent(), null
            )
                //Toast.makeText(applicationContext, "Login Successfuly", Toast.LENGTH_SHORT).show()
                
            }, 1300) // 2000 milliseconds = 2 seconds

        }

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            // Ignore SSL certificate errors
            handler?.proceed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    fun exitDueToClickOnExit(view: View) {
        onBackPressed()
    }
}