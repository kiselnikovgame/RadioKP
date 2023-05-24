package com.kiselikovgame.radiokp.data

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject

class FeedDB : RealmObject {
    var items: RealmList<FeedItemDB> =
        realmListOf()
}

class FeedItemDB : RealmObject {
    var title: String = ""
    var link: String = ""
    var description: String = ""
    var thumbnail : String = ""
    var enclosure: EnclosureDB? = null
}

class EnclosureDB : RealmObject {
    var link: String = ""
    var type: String = ""
}