package com.kiselikovgame.radiokp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kiselikovgame.radiokp.R
import com.kiselikovgame.radiokp.recyclerview.RecAdapter
import com.kiselikovgame.radiokp.App
import com.kiselikovgame.radiokp.data.EnclosureDB
import com.kiselikovgame.radiokp.data.Feed
import com.kiselikovgame.radiokp.data.FeedDB
import com.kiselikovgame.radiokp.data.FeedItemDB
import com.kiselikovgame.radiokp.utils.createRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf

class MainFragment : Fragment() {

    private lateinit var app : App
    private lateinit var realm : Realm
    private lateinit var recView: RecyclerView
    private var request: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = requireContext().applicationContext as App
        realm = Realm.open(app.realmConfig)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recView = view.findViewById(R.id.fragment_main_recview)

        val o = createRequest(getString(R.string.rss_url))
            .map { Gson().fromJson(it, Feed::class.java) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        request = o.subscribe({
            val feeds = it.items.mapTo(realmListOf()) { feed ->
                FeedItemDB().apply {
                    this.title = feed.title
                    this.link = feed.link
                    this.description = feed.description
                    this.thumbnail = feed.thumbnail
                    this.enclosure = EnclosureDB().apply {
                        this.link = feed.enclosure.link
                        this.type = feed.enclosure.type
                    }
                }
            }

            //write feeds in block transaction
            realm.writeBlocking {
                val old = query<FeedDB>().find()
                delete(old)

                copyToRealm(FeedDB().apply {
                    this.items = feeds
                })
            }

            showRecView()

        }, {
            Log.e(null, it.message, it)
        })
    }

    private fun showRecView() {

        val feeds = realm.query<FeedDB>().find()
        if (feeds.size > 0) {
            recView.adapter = RecAdapter(feeds.last().items)
            recView.layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        realm.close();
        request?.dispose()
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}