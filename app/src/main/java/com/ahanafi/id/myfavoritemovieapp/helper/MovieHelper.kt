package com.ahanafi.id.myfavoritemovieapp.helper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyMovieColumns.Companion.TABLE_NAME
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyMovieColumns.Companion._ID
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseHelper
import java.sql.SQLException

class MovieHelper(context: Context) {
    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper : DatabaseHelper
        private var INSTANCE : MovieHelper? = null
        private lateinit var database : SQLiteDatabase

        fun getInstance(context: Context) : MovieHelper {
            if(INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if(INSTANCE == null) {
                        INSTANCE = MovieHelper(context)
                    }
                }
            }

            return INSTANCE as MovieHelper
        }
    }

    init {
        databaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun queryAll() : Cursor {
        return database.query(DATABASE_TABLE, null, null, null, null, null, "$_ID ASC")
    }

    fun insert(values: ContentValues?) : Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteById(id: String) : Int {
        return database.delete(DATABASE_TABLE, "$_ID = $id", null)
    }

    @SuppressLint("Recycle")
    fun checkIfExists(id: Int?) : Boolean {
        val query = database.rawQuery("SELECT * FROM $DATABASE_TABLE WHERE $_ID = $id", null)
        return query.count > 0
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null)
    }

    fun update(id: String, values: ContentValues?) : Int {
        return database.update(DATABASE_TABLE, values, "${_ID} = ?", arrayOf(id))
    }
}