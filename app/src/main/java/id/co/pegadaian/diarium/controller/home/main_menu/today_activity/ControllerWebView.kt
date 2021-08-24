package id.co.pegadaian.diarium.controller.home.main_menu.today_activity

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import id.co.pegadaian.diarium.R

class ControllerWebView : AppCompatActivity() {

    lateinit var webView : WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controller_web_view)

        webView = findViewById(R.id.service_webview)
        print("url webview : " + intent.getStringExtra("url"))
        webView.loadUrl(intent.getStringExtra("url"))

        var webSettings : WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true

        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                if (url.endsWith(".jpg") || url.endsWith(".jpeg")
                        || url.endsWith(".xlsx") || url.endsWith(".xls")
                        || url.endsWith(".docx") || url.endsWith(".doc")){
                    var source : Uri = Uri.parse(url)
                    var request : DownloadManager.Request = DownloadManager.Request(source)
                    request.setDescription("Downloading file")
                    val title = url.split("/".toRegex()).toTypedArray()[url.split("/".toRegex()).toTypedArray().size - 1]
                    request.setTitle(title)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                        request.allowScanningByMediaScanner()
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    }
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title)
                    var manager : DownloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    manager.enqueue(request)

                } else if (url.endsWith(".mp3")){

                } else view?.loadUrl(url)

//                return super.shouldOverrideUrlLoading(view, url)
                return true
            }
        }

//        webView.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
//
//            val request = DownloadManager.Request(Uri.parse(url))
//            request.setMimeType(mimetype)
//            request.addRequestHeader("cookie", CookieManager.getInstance().getCookie(url))
//            request.addRequestHeader("User-Agent", userAgent)
//            request.setDescription("Downloading file...")
//            request.setTitle(URLUtil.guessFileName(url.trim(), contentDisposition, mimetype))
//            request.allowScanningByMediaScanner()
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//            request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, ".png")
//            val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//            dm.enqueue(request)
//        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if (webView.canGoBack()){
            webView.goBack()
        } else {
            finish()
        }
    }
}