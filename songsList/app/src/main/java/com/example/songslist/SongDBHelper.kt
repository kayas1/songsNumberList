package com.example.songslist

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SongDBHelper(
    context: Context, name: String,
    factory: SQLiteDatabase.CursorFactory?, version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    companion object {
        const val TABLE = "song"
    }

    override fun onCreate(db: SQLiteDatabase) {
        "CREATE TABLE if not exists $TABLE (_id integer primary key autoincrement, title text Not Null, tj integer, ky number, info text, category integer, UNIQUE(title));"
            .apply { db.execSQL(this) }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        "DROP TABLE if exists $TABLE".apply { db.execSQL(this) }
        onCreate(db)
    }

    fun selectSong(id: String): Song? {
        var songItem: Song? = null
        val columns = arrayOf("_id", "title", "tj", "ky", "info", "category")
        readableDatabase.query(TABLE, columns, "_id=?", arrayOf(id), null, null, null).use {
            if (it.moveToNext()) {
                val idx = it.getInt(0)
                val title = it.getString(1)
                val tj = it.getString(2)
                val ky = it.getString(3)
                val info = it.getString(4)
                val category = it.getInt(5)
                songItem = Song(idx, title, tj, ky, info, category)
            }
        }
        return songItem
    }

    fun findSong(id: String): Song? {
        var songItem: Song? = null
        val columns = arrayOf("_id", "title", "tj", "ky", "info", "category")
        readableDatabase.query(TABLE, columns, "title=?", arrayOf(id), null, null, null).use {
            if (it.moveToNext()) {
                val idx = it.getInt(0)
                val title = it.getString(1)
                val tj = it.getString(2)
                val ky = it.getString(3)
                val info = it.getString(4)
                val category = it.getInt(5)
                songItem = Song(idx, title, tj, ky, info, category)
            }
        }
        return songItem
    }

    fun insertSong(item: Song) {
        val value = ContentValues().apply {
            put("title", item.title)
            put("tj", item.tj)
            put("ky", item.ky)
            put("info", item.info)
            put("category", item.category)
        }

        with(writableDatabase) {
            beginTransaction()
            val k = insert(TABLE, null, value)
            if (k > 0) setTransactionSuccessful()
            endTransaction()
        }
    }

    fun deleteSong(id: String) {
        with(writableDatabase) {
            beginTransaction()
            val k = delete(TABLE, "_id=?", arrayOf(id))
            if (k > 0) setTransactionSuccessful()
            endTransaction()
        }
    }

    fun updateSong(item: Song) {
        val value = ContentValues().apply {
            put("title", item.title)
            put("tj", item.tj)
            put("ky", item.ky)
            put("info", item.info)
            put("category", item.category)
        }

        with(writableDatabase) {
            beginTransaction()
            val k = update(TABLE, value, "_id=?", arrayOf(item.id.toString()))
            if (k > 0) setTransactionSuccessful()
            endTransaction()
        }
    }

    fun selectAllSongs(): MutableList<Song> {
        val list = mutableListOf<Song>()
        readableDatabase.rawQuery("SELECT * FROM $TABLE ORDER BY title", null).use {
            while (it.moveToNext()) {
                val idx = it.getInt(0)
                val title = it.getString(1)
                val tj = it.getString(2)
                val ky = it.getString(3)
                val info = it.getString(4)
                val category = it.getInt(5)
                list.add(Song(idx, title, tj, ky, info, category))
            }
        }
        return list
    }

    fun selectSongsByCategory(idx:Int): MutableList<Song> {
        val list = mutableListOf<Song>()
        readableDatabase.rawQuery("SELECT * FROM $TABLE WHERE category = $idx ORDER BY title", null).use {
            while (it.moveToNext()) {
                val idx = it.getInt(0)
                val title = it.getString(1)
                val tj = it.getString(2)
                val ky = it.getString(3)
                val info = it.getString(4)
                val category = it.getInt(5)
                list.add(Song(idx, title, tj, ky, info, category))
            }
        }
        return list
    }

    fun getCount() = selectAllSongs().size
}