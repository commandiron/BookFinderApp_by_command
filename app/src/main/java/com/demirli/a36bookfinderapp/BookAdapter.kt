package com.demirli.a36bookfinderapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class BookAdapter(var bookList: List<Book>): RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = bookList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.author.text = bookList[position].author
        holder.title.text = bookList[position].title

        if(bookList[position].imageLink != ""){
            println(bookList[position].imageLink)
            Picasso.get().load(bookList[position].imageLink).into(holder.book_imageView, object: Callback{
                override fun onSuccess() {
                }

                override fun onError(e: Exception?) {
                    e!!.printStackTrace()
                }
            })
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val author = view.findViewById<TextView>(R.id.author_tv)
        val title = view.findViewById<TextView>(R.id.title_tv)
        val book_imageView = view.findViewById<ImageView>(R.id.book_imageView)
    }
}