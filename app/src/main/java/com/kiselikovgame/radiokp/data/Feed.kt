package com.kiselikovgame.radiokp.data

class Feed(
    val items: ArrayList<FeedItem>
)

class FeedItem(
    val title: String,
    val link: String,
    val description: String,
    val thumbnail : String,
    val enclosure: Enclosure,
)

class Enclosure(
    var link: String,
    var type: String
)