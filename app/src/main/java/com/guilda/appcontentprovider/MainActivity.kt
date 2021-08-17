package com.guilda.appcontentprovider

import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns._ID
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.guilda.appcontentprovider.database.NotasProvider.Companion.URI_NOTAS
import com.guilda.appcontentprovider.database.NotesDatabaseHelper.Companion.TITULO_NOTAS

class MainActivity : AppCompatActivity(),LoaderManager.LoaderCallbacks<Cursor>{
    lateinit var  notaRecycleView: RecyclerView
    lateinit var  notaAdd: FloatingActionButton


    lateinit var adapter: NotaAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        notaAdd = findViewById(R.id.nota_add)
        notaAdd.setOnClickListener{
            NotaDetailFragment().show(supportFragmentManager,"Dialog")
        }
        adapter = NotaAdapter(object : NotaCLickedListener {
            override fun noteClickedItem(cursor: Cursor?) {
                val id = cursor?.getLong(cursor.getColumnIndex(_ID))
                val fragment = id?.let { NotaDetailFragment.newInstance(it) }
                fragment?.show(supportFragmentManager,"Dialog")
            }

            override fun noteRemoveItem(cursor: Cursor?) {
                val id = cursor?.getLong(cursor.getColumnIndex(_ID))
                contentResolver.delete(Uri.withAppendedPath(URI_NOTAS,id.toString()),null,null)
            }

        })
        adapter.setHasStableIds(true)

        notaRecycleView = findViewById(R.id.notas_recycler)
        notaRecycleView.layoutManager = LinearLayoutManager(this)
        notaRecycleView.adapter = adapter
        LoaderManager.getInstance(this).initLoader(0,null,this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> =
        CursorLoader(this,URI_NOTAS,null,null,null,TITULO_NOTAS)


    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
       if(data != null){adapter.setCursor(data)}
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
      adapter.setCursor(null)
    }
}