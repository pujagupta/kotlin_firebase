package com.example.my22application.kotlinpoc.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.my22application.kotlinpoc.R
import com.example.my22application.kotlinpoc.adapter.MoviesAdapter
import com.example.my22application.kotlinpoc.model.Movies
import com.google.firebase.database.*

class ListActivity : AppCompatActivity() {
    lateinit var mDatabase: DatabaseReference
    var moviesList: MutableList<Movies>? = null
    lateinit var movieAdapter: MoviesAdapter
    private var listViewItems: ListView? = null
    private var logoutIcon: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        listViewItems = findViewById<View>(R.id.movies_list) as ListView
        logoutIcon = findViewById<View>(R.id.logout_icon) as ImageView

        mDatabase = FirebaseDatabase.getInstance().reference
        moviesList = mutableListOf<Movies>()
        movieAdapter = MoviesAdapter(this, moviesList!!)
        listViewItems!!.setAdapter(movieAdapter)
        mDatabase.orderByKey().addListenerForSingleValueEvent(itemListener)

        listViewItems!!.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                navigateToDetailActivity(parent, position)
            }

        logoutIcon!!.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(R.string.logoutMessage)

            //performing positive action
            builder.setPositiveButton("Yes"){dialogInterface, which ->
                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            //performing negative action
            builder.setNegativeButton("No"){dialogInterface, which ->
                //Toast.makeText(applicationContext,"clicked No",Toast.LENGTH_LONG).show()
            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }

    private fun navigateToDetailActivity(parent: AdapterView<*>?, position: Int) {
        var item = parent?.getItemAtPosition(position)
        var items = item as Movies
        val id = items.movieId;
        //pass this id to detail page through intent
        val intent = Intent(this , DetailPageActivity :: class.java)
        intent.putExtra("Id",id);
        startActivity(intent)
    }

    var itemListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            addDataToList(dataSnapshot)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Item failed, log a message
            Log.w("ListActivity", "loadItem:onCancelled", databaseError.toException())
        }
    }

    private fun addDataToList(dataSnapshot: DataSnapshot) {
        val items = dataSnapshot.children.iterator()
        //Check if current database contains any collection
        if (items.hasNext()) {
            val toDoListindex = items.next()
            val itemsIterator = toDoListindex.children.iterator()

            //check if the collection has any to do items or not
            while (itemsIterator.hasNext()) {
                //get current item
                val currentItem = itemsIterator.next()
                val movieItem = Movies.create()
                //get current data in a map
                val map = currentItem.getValue() as HashMap<String, Any>
                //key will return Firebase ID
                movieItem.movieId = currentItem.key
                movieItem.movieName = map.get("movieName") as String?
                movieItem.movieGenre = map.get("movieGenre") as String?
                moviesList!!.add(movieItem);
            }
        }
        //alert adapter that has changed
        movieAdapter.notifyDataSetChanged()

    }
}

