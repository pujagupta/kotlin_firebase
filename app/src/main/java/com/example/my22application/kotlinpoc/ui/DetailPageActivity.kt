package com.example.my22application.kotlinpoc.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebBackForwardList
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.my22application.kotlinpoc.R

class DetailPageActivity : AppCompatActivity() {
    private lateinit var movieImageView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_page)
        movieImageView = findViewById<View>(R.id.movie_image_view) as WebView

        movieImageView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        movieImageView.loadUrl("https://i.pinimg.com/236x/20/84/82/208482771be1a6243d6efe8493047c42--foreign-movies-indian-movies.jpg")
    }
}
