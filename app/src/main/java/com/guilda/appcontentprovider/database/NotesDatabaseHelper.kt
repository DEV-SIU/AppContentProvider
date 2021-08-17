package com.guilda.appcontentprovider.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID

class NotesDatabaseHelper(context : Context):SQLiteOpenHelper(context, "databaseNotas",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABELA_NOTAS (" +
                "$_ID INTEGER NOT NULL PRIMARY KEY, " +
                "$TITULO_NOTAS TEXT NOT NULL, " +
                "$DESCRICAO TEXT NOT NULL)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
    companion object{
        const val TABELA_NOTAS:String = "Notas"
        const val TITULO_NOTAS:String = "Titulo"
        const val  DESCRICAO:String = "descrição"
    }
}