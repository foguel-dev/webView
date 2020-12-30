package com.example.webview

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.*
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    // private... crear constante

    private val BASE_URL = "https://google.com"
    private val SEARCH_PATH = "/search?q="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Refresh

        swipeRefresh.setOnRefreshListener {
            webViewL.reload()
        }

        // Search
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(p0: String?): Boolean { // Permite delegar en ese componente es decir que nos sugiera busquedas historicas
                return false
            }

            override fun onQueryTextSubmit(p0: String?): Boolean { // Accede al texto ingresado en la barra de busqueda

                p0?.let {                             // Desempaquetamos con Let si de verdad es distinto de null
                    if (URLUtil.isValidUrl(it)) {    // Class del sdk de android
                        //En este bloque es una URL
                        webViewL.loadUrl(it)
                    } else {
                        // No es una URL
                        webViewL.loadUrl("$BASE_URL$SEARCH_PATH$it")
                    }
                }
                return false
            }
        })

        //Configurar WebView
        // Hace interacciones con el webview

        webViewL.webChromeClient = object : WebChromeClient() {

        }

        webViewL.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading( // Darnos el control sobre el manejo de nuevas cargas url
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return super.shouldOverrideUrlLoading(view, request)

                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                searchView.setQuery(url, false) // Ver url base
                swipeRefresh.isRefreshing = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                swipeRefresh.isRefreshing = false
            }
        }
        val settings = webViewL.settings  //En esta parte activamos JavaScript, ya que los webview los traen desaviltado
        settings.javaScriptEnabled = true
        webViewL.loadUrl(BASE_URL)
    }

    // Volver hacia atras
    override fun onBackPressed() {
        if (webViewL.canGoBack()) {
            webViewL.goBack()
        } else {
            super.onBackPressed()
        }
    }
}