package com.kiselikovgame.radiokp.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kiselikovgame.radiokp.R
import com.kiselikovgame.radiokp.data.FeedItemDB
import io.realm.kotlin.types.RealmList

class RecAdapter(private val items: RealmList<FeedItemDB>) : RecyclerView.Adapter<RecHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_items, parent, false)
        return RecHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }
}