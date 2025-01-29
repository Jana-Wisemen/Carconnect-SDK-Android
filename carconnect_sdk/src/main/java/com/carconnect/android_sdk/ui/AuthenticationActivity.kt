package com.carconnect.android_sdk.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import com.carconnect.android_sdk.R
import com.carconnect.android_sdk.helpers.JsMessageHandlerInterface
import com.carconnect.android_sdk.models.authentication.Authentication
import com.carconnect.android_sdk.models.authentication.buildRedirect
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Shape
import nl.dionsegijn.konfetti.core.models.Size
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit

internal class AuthenticationActivity : AppCompatActivity(), JsMessageHandlerInterface.Handler {

    companion object{
        const val EXTRA_USERNAME = "EXTRA_USERNAME"
        const val EXTRA_BRAND = "EXTRA_BRAND"
        const val RESULT_TOKENS = "RESULT_TOKENS"
        const val TAG = "CARCONNECT_SDK"
    }

    private val username: String? by lazy { intent.getStringExtra(EXTRA_USERNAME) }
    private val brand: String? by lazy { intent.getStringExtra(EXTRA_BRAND) }
    lateinit var mViewModel: AuthenticationViewModel

    private val webView: WebView by lazy {
        findViewById(R.id.webView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        Log.d(TAG, "onCreate")

        mViewModel = ViewModelProvider(this, AuthenticationViewModel.factory(username, brand))
            .get(AuthenticationViewModel::class.java)

        initWebView()
        initCloseButton()
    }

    private fun initCloseButton() {
        findViewById<ImageView>(R.id.btnClose).setOnClickListener {
            /*try {
                deleteDatabase("webview.db");
                deleteDatabase("webviewCache.db");
            } catch (ex: Exception){

            }*/

            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        Log.d(TAG, "initwebview")

        with(webView){
            clearCache(true)
            clearHistory()
            clearFormData()
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()

            with(settings){
                javaScriptEnabled = true
                allowContentAccess = true
                loadWithOverviewMode = true
                useWideViewPort = true
                domStorageEnabled = true
                cacheMode = WebSettings.LOAD_NO_CACHE
            }

            addJavascriptInterface(JsMessageHandlerInterface(this@AuthenticationActivity), "Android")

            webViewClient = object: WebViewClient(){
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {

                    Log.d(TAG, "mViewModel.authentication?.redirect ${mViewModel.authentication?.redirect}")
                    Log.d(TAG, "mViewModel.authentication?.redirect?.capture ${mViewModel.authentication?.redirect?.capture}")
                    val capture = mViewModel.authentication?.redirect?.capture

                    if(capture != null && request?.url.toString().startsWith(capture)){
                        Log.d("TAG", "request: " + request?.url.toString())
                        val uri = mViewModel.authentication?.buildRedirect(request?.url ?: return super.shouldOverrideUrlLoading(view, request))
                        Log.d("TAG", "redirect: " + uri.toString())
                        loadUrl(uri.toString())
                    }

                    return super.shouldOverrideUrlLoading(view, request)
                }

                override fun onReceivedSslError(
                    view: WebView?,
                    handler: SslErrorHandler?,
                    error: SslError?
                ) {
                    //super.onReceivedSslError(view, handler, error)
                    Log.d(TAG, "onReceivedSslError ${error}")
                    Log.d(TAG, "onReceivedSslError Brand ${brand}")


                    handler?.proceed()

                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                    Log.d(TAG, "onReceivedError ${error}")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Log.d(TAG, "onReceivedError ${error?.errorCode}")
                        Log.d(TAG, "onReceivedError ${error?.description}")
                    }
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    Log.d(TAG, "onpage started $url")
                    loading(true)
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    Log.d(TAG, "onpage Finished")
                    loading(false)
                    super.onPageFinished(view, url)
                }
            }

            Log.d(TAG, "load url ${mViewModel.getAuthenticationUrl().toString()}")
            loadUrl(mViewModel.getAuthenticationUrl().toString())
        }
    }

    fun loading(isLoading: Boolean){
        findViewById<ProgressBar>(R.id.progressBar).visibility = if(isLoading) View.VISIBLE else View.GONE
    }

    override fun onAuthentication(authentication: Authentication) {
        runOnUiThread {
            mViewModel.authentication = authentication
            Log.d(TAG, "onAuthentication ${authentication.url}")
            webView.loadUrl(authentication.url)
        }
    }

    override fun onStatus(status: String) {
        if(status == "success")
            dropConfetti()
    }

    override fun onTokens(json: String) {
        val resultIntent = Intent().apply {
            putExtra(RESULT_TOKENS, json)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun dropConfetti(){
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        findViewById<KonfettiView>(R.id.kvSuccess).start(
            Party(
                emitter = Emitter(duration = 3, TimeUnit.SECONDS).perSecond(200) ,
                position = Position.Absolute(displayMetrics.widthPixels.div(2f), -300f),
                spread = 360,
                fadeOutEnabled = true,
                timeToLive = 2000,
                shapes = listOf(Shape.Square, Shape.Circle),
                size = listOf(Size(12))
            )
        )
    }
}