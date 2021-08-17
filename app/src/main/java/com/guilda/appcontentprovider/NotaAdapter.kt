package com.guilda.appcontentprovider

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guilda.appcontentprovider.database.NotesDatabaseHelper.Companion.DESCRICAO
import com.guilda.appcontentprovider.database.NotesDatabaseHelper.Companion.TITULO_NOTAS

class NotaAdapter(private val listener: NotaCLickedListener): RecyclerView.Adapter<NotasViewHolder>(){
    private var  mCursor: Cursor? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotasViewHolder =
        NotasViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.nota_item,parent,false))

    override fun onBindViewHolder(holder: NotasViewHolder, position: Int) {
        mCursor?.moveToPosition(position)
        holder.nota_titulo.text = mCursor?.getString(mCursor?.getColumnIndex(TITULO_NOTAS)as Int)
        holder.nota_decricao.text = mCursor?.getString(mCursor?.getColumnIndex(DESCRICAO) as Int)
        holder.buttomremover.setOnClickListener{
            mCursor?.moveToPosition(position)
            listener.noteRemoveItem(mCursor)
            notifyDataSetChanged()
        }
        holder.itemView.setOnClickListener{listener.noteClickedItem(mCursor as Cursor)}

    }

    override fun getItemCount(): Int  = if (mCursor != null) mCursor?.count as Int else 0


    fun setCursor(newCursor: Cursor?){
        mCursor = newCursor
        notifyDataSetChanged()
    }
}

class NotasViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val nota_titulo = itemView.findViewById(R.id.nota_titulo)as TextView
    val nota_decricao = itemView.findViewById(R.id.nota_decricao) as TextView
    val buttomremover = itemView.findViewById(R.id.nota_buttom_remove) as Button

}