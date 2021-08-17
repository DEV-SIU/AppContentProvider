package com.guilda.appcontentprovider.database

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.media.UnsupportedSchemeException
import android.net.Uri
import android.os.Build
import android.provider.BaseColumns._ID
import androidx.annotation.RequiresApi
import com.guilda.appcontentprovider.database.NotesDatabaseHelper.Companion.TABELA_NOTAS

class NotasProvider : ContentProvider() {

    private lateinit var  mUriMatcher: UriMatcher
    private lateinit var  dbHelper : NotesDatabaseHelper
    override fun onCreate(): Boolean {
     mUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        mUriMatcher.addURI(AUTHORITY, "notas", NOTAS)
        mUriMatcher.addURI(AUTHORITY,"notas/#", NOTAS_BY_ID)
        if (context != null){dbHelper = NotesDatabaseHelper(context as Context) }
        return true
    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        if (mUriMatcher.match(uri) == NOTAS_BY_ID){
        val  db: SQLiteDatabase = dbHelper.writableDatabase
        val linesAffect : Int = db.delete(TABELA_NOTAS,"$_ID =?", arrayOf(uri.lastPathSegment))
        db.close()
        context?.contentResolver?.notifyChange(uri,null)
            return linesAffect
        }else{
            throw UnsupportedSchemeException(" Uri inválida para exclusão!")
        }

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun getType(uri: Uri): String = throw UnsupportedSchemeException("Não Implementado")

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if(mUriMatcher.match(uri) == NOTAS){
            val db:SQLiteDatabase = dbHelper.writableDatabase
            val id = db.insert(TABELA_NOTAS,null,values)
            val insertUri = Uri.withAppendedPath(BASE_URI, id.toString())
            db.close()
            context?.contentResolver?.notifyChange(uri,null)
            return insertUri
        }else{
            throw  UnsupportedSchemeException("Uri inválida para inserção!")
        }
    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return  when {
            mUriMatcher.match(uri) == NOTAS -> {
                val db:SQLiteDatabase = dbHelper.writableDatabase
                val cursor = db.query(TABELA_NOTAS,projection,selection,selectionArgs,null,null,sortOrder)
                cursor.setNotificationUri(context?.contentResolver,uri)
                cursor
            }
            mUriMatcher.match(uri) == NOTAS_BY_ID ->{
                val db:SQLiteDatabase = dbHelper.writableDatabase
                val cursor = db.query(TABELA_NOTAS,projection,"$_ID = ?", arrayOf(uri.lastPathSegment),null,null,sortOrder)
                cursor.setNotificationUri(context?.contentResolver,uri)
                cursor
            }
            else ->{
                throw UnsupportedSchemeException("Uri não implementada")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        if (mUriMatcher.match(uri) == NOTAS_BY_ID){
            val db: SQLiteDatabase = dbHelper.writableDatabase
            val  linesAffect = db.update(TABELA_NOTAS,values,"$_ID = ?", arrayOf(uri.lastPathSegment))
            db.close()
            context?.contentResolver?.notifyChange(uri, null)
            return  linesAffect
        }else{
            throw UnsupportedSchemeException("Uri não implementada")
        }
    }
    companion object{
        const val  AUTHORITY = "com.guilda.appcontentprovider.provider"
        val BASE_URI = Uri.parse("content://$AUTHORITY")
        val URI_NOTAS = Uri.withAppendedPath(BASE_URI,"notas")


        const val  NOTAS = 1
        const val  NOTAS_BY_ID = 2
    }
}