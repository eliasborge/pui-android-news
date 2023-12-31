package com.example.android_news_pui

import android.os.AsyncTask
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.RecyclerView
import java.util.Properties

class MainActivity : ComponentActivity() {

    var mm: ModelManager? = null

    val prop = Properties()
    private var recyclerView: RecyclerView? = null
    private var articleAdapter: ArticleAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        prop.setProperty(ModelManager.ATTR_LOGIN_USER, "DEV_TEAM_03")
        prop.setProperty(ModelManager.ATTR_LOGIN_PASS, "123703@3")
        prop.setProperty(
            ModelManager.ATTR_SERVICE_URL,
            "https://sanger.dia.fi.upm.es/pui-rest-news/"
        )
        //prop.setProperty(ModelManager.ATTR_REQUIRE_SELF_CERT, "TRUE")



        recyclerView = findViewById(R.id.recyclerview)
        articleAdapter = ArticleAdapter(ArrayList(),this)
        recyclerView?.adapter = articleAdapter

        // Fetch articles from the server
        FetchArticlesTask().execute()
    }

    private inner class FetchArticlesTask : AsyncTask<Void, Void, List<Article>>() {
        override fun doInBackground(vararg params: Void): List<Article> {
            mm = ModelManager(prop)
            // Adjust the buffer and offset values as needed
            val buffer = 10
            val offset = 0
            return mm?.getArticles(buffer, offset) ?: emptyList()
        }

        override fun onPostExecute(articles: List<Article>) {
            super.onPostExecute(articles)
            // Update the adapter with the new data
            articleAdapter?.setArticles(articles)
            articleAdapter?.notifyDataSetChanged()
        }
    }
}
