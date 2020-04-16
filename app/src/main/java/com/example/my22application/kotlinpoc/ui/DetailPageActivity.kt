package com.example.my22application.kotlinpoc.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ListView
import android.widget.TextView
import com.example.my22application.kotlinpoc.Constants
import com.example.my22application.kotlinpoc.R
import com.example.my22application.kotlinpoc.model.Movies
import com.google.firebase.database.*
import java.util.*

class DetailPageActivity : AppCompatActivity() {
    private lateinit var movieImageWebView: WebView
    private lateinit var movieNameTextView: TextView
    private lateinit var movieYearTextView: TextView
    private lateinit var movieGenreTextView: TextView
    private lateinit var movieDirectorTextView: TextView
    lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_page)

        movieImageWebView = findViewById<View>(R.id.movie_webview) as WebView
        movieNameTextView = findViewById<View>(R.id.movie_name) as TextView
        movieGenreTextView = findViewById<View>(R.id.genre_value) as TextView
        movieYearTextView = findViewById<View>(R.id.year_value) as TextView
        movieDirectorTextView = findViewById<View>(R.id.director_value) as TextView

        mDatabase = FirebaseDatabase.getInstance().reference
        val movieId: String = intent.getStringExtra("Id")
        loadData(movieId)
    }

    private fun loadData(movieId: String) {
        val itemReference = mDatabase.child(Constants.FIREBASE_ITEM).child(movieId)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue(Movies::class.java)
                if (data != null) {
                    movieNameTextView.text = data.movieName.toString()
                    movieYearTextView.text = data.year.toString()
                    movieGenreTextView.text = data.movieGenre.toString()
                    movieDirectorTextView.text = data.Director.toString()
                    movieImageWebView.webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            url: String?
                        ): Boolean {
                            view?.loadUrl(url)
                            return true
                        }
                    }
                    movieImageWebView.loadUrl(data.image.toString())
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                //Log.d(TAG, databaseError.getMessage()) //Don't ignore errors!
            }
        }
        itemReference.addListenerForSingleValueEvent(valueEventListener)
    }
}
