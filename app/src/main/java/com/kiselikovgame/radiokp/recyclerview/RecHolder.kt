package com.kiselikovgame.radiokp.recyclerview

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kiselikovgame.radiokp.MainActivity
import com.kiselikovgame.radiokp.R
import com.kiselikovgame.radiokp.data.FeedItemDB
import com.squareup.picasso.Picasso

class RecHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: FeedItemDB) {

        val title = itemView.findViewById<TextView>(R.id.item_title)
        val description = itemView.findViewById<TextView>(R.id.item_desc)
        val thumbnail = itemView.findViewById<ImageView>(R.id.item_thumb)

        title.text = item.title
        description.text = item.description

        Picasso.get()
            .load(item.thumbnail)
            .placeholder(R.drawable.thumbnail)
            .error(R.drawable.thumbnail)
            .resize(240,240)
            .into(thumbnail)

        //set click on recyclerView's item
        itemView.setOnClickListener {
            (it.context as MainActivity).playMusic(
                url = item.enclosure!!.link,
                title = item.title)
        }

        //set long click on recyclerView's item
        itemView.setOnLongClickListener {
            (it.context as MainActivity).showArticle(item.link)
            return@setOnLongClickListener true
        }
    }
}