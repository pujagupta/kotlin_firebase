package com.example.my22application.kotlinpoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.my22application.kotlinpoc.adapter.MoviesAdapter
import com.example.my22application.kotlinpoc.model.Movies
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var addButton: ImageButton
    private lateinit var movieNameText: EditText
    private lateinit var genreText: EditText
    lateinit var mDatabase: DatabaseReference
    var moviesList: MutableList<Movies>? = null
    lateinit var movieAdapter: MoviesAdapter
    private var listViewItems: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addButton = findViewById<View>(R.id.add_button) as ImageButton
        listViewItems = findViewById<View>(R.id.movies_list) as ListView
        addButton.setOnClickListener { view ->
            //Show Dialog here to add new Item
            addNewItemDialog()
        }

        mDatabase = FirebaseDatabase.getInstance().reference
        moviesList = mutableListOf<Movies>()
        movieAdapter = MoviesAdapter(this, moviesList!!)
        listViewItems!!.setAdapter(movieAdapter)
        mDatabase.orderByKey().addListenerForSingleValueEvent(itemListener)

    }

    private fun addNewItemDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("With EditText")
        val dialogLayout = inflater.inflate(R.layout.add_movie_dialog, null)
        movieNameText = dialogLayout.findViewById<EditText>(R.id.movie_name)
        genreText = dialogLayout.findViewById<EditText>(R.id.genre)
        builder.setView(dialogLayout)
        builder.setPositiveButton("OK") { dialogInterface, i ->
            addMovies();
        }
        builder.show()
    }

    private fun addMovies() {
        val movies = Movies.create()
        movies.movieName = movieNameText.text.toString()
        movies.movieGenre = genreText.text.toString()
        //We first make a push so that a new item is made with a unique ID
        val newItem = mDatabase.child(Constants.FIREBASE_ITEM).push()
        movies.movieId = newItem.key
        //then, we used the reference to set the value on that ID
        newItem.setValue(movies)
        Toast.makeText(this, "Movie Saved", Toast.LENGTH_SHORT).show()
        moviesList?.clear();
        mDatabase.orderByKey().addListenerForSingleValueEvent(itemListener)
    }

    var itemListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            addDataToList(dataSnapshot)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Item failed, log a message
            Log.w("MainActivity", "loadItem:onCancelled", databaseError.toException())
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

