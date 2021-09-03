package com.demirli.a36bookfinderapp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var userList: ArrayList<Book>
    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userList = arrayListOf()

        recyclerView.layoutManager = GridLayoutManager(this,2)
        adapter = BookAdapter(userList)
        recyclerView.adapter = adapter


            search_btn.setOnClickListener {

                if(search_key_et.text.toString() != ""){
                    progressbar.visibility = View.VISIBLE
                    userList.clear()
                    val searchKey = search_key_et.text.toString()
                    val url = "https://www.googleapis.com/books/v1/volumes?q=${searchKey}&key=${Constants.API_KEY}"

                    getBooksFromGoogleApi().execute(url)
                }else{
                    Toast.makeText(this,"Search box is empty", Toast.LENGTH_SHORT).show()
                }
            }
    }

    inner class getBooksFromGoogleApi(): AsyncTask<String, Void, String>(){
        override fun doInBackground(vararg params: String?): String {
            var result = ""
            var url: URL
            var httpURLConnection: HttpURLConnection
            url = URL(params[0])
            httpURLConnection = url.openConnection() as HttpURLConnection
            val inputStream = httpURLConnection.inputStream
            val inputStreamReader = InputStreamReader(inputStream)
            var data = inputStreamReader.read()

            while (data > 0) {
                val character = data.toChar()
                result += character

                data = inputStreamReader.read()
            }
            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val jsonObject = JSONObject(result)
            val jsonArray = JSONArray(jsonObject.getString("items"))

            for (i in 0 until jsonArray.length()){
                val jsonObjectInFor = JSONObject(jsonArray.getString(i))
                val volumeInfoJsonObject = jsonObjectInFor.getJSONObject("volumeInfo")


                //Title
                val title = volumeInfoJsonObject.getString("title")
                //

                //Author
                var author = ""
                try {
                    val authorArray = volumeInfoJsonObject.getJSONArray("authors")
                    if (authorArray.length() == 1){
                        author = authorArray.getString(0)
                    }else if(authorArray.length() >= 1){
                        for (i in 0 until authorArray.length()){
                            author += authorArray.getString(i) + ", "
                        }
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
                //

                //SmallThumbNail
                var imageLink = ""
                try {
                    val imageLinksObject = volumeInfoJsonObject.getJSONObject("imageLinks")
                    imageLink = imageLinksObject.getString("smallThumbnail")
                }catch (e:Exception){
                }

                userList.add(Book(title,author,imageLink))
            }

            progressbar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            adapter.notifyDataSetChanged()
        }
    }
}
