package com.example.my22application.kotlinpoc.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.example.my22application.kotlinpoc.R
import com.example.my22application.kotlinpoc.model.Movies

class MoviesAdapter(context: Context, toDoItemList: MutableList<Movies>) : BaseAdapter() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var itemList = toDoItemList
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val movieId: String = itemList.get(position).movieId as String
        val movieName: String = itemList.get(position).movieName as String
        val movieGenre: String = itemList.get(position).movieGenre as String
        val view: View
        val viewHolder: ListRowHolder
        if (convertView == null) {
            view = mInflater.inflate(R.layout.movie_list, parent, false)
            viewHolder = ListRowHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ListRowHolder
        }
        viewHolder.name.text = movieName
        viewHolder.genre.text = movieGenre
        return view
    }
    override fun getItem(index: Int): Any {
        return itemList.get(index)
    }
    override fun getItemId(index: Int): Long {
        return index.toLong()
    }
    override fun getCount(): Int {
        return itemList.size
    }
    private class ListRowHolder(row: View?) {
        val name: TextView = row!!.findViewById<TextView>(R.id.movie_name) as TextView
        val genre: TextView = row!!.findViewById<CheckBox>(R.id.movie_genre) as TextView
    }
}