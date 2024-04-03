package com.example.songslist

data class Song(
    val id: Int,
    val title: String,
    val tj: String,
    val ky: String,
    val info:String,
    val category:Int
//    val alarmTen: Boolean,
//    val alarmThirty: Boolean
) {
    override fun toString() = "$title $tj $ky $info $category"
}
