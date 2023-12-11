package com.example.android_news_pui

import android.os.AsyncTask
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.android_news_pui.ModelManager

class MainActivity : ComponentActivity() {

    private var recyclerView: RecyclerView? = null
    private var articleAdapter: ArticleAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerview)
        articleAdapter = ArticleAdapter(ArrayList())
        recyclerView?.adapter = articleAdapter

        // Fetch articles from the server
        FetchArticlesTask().execute()
    }

    private inner class FetchArticlesTask : AsyncTask<Void, Void, List<Article>>() {
        override fun doInBackground(vararg params: Void): List<Article> {
            // Adjust the buffer and offset values as needed
            val buffer = 0
            val offset = 0
            return ModelManager.getArticles(buffer, offset)
        }
    }
}
