package com.guilda.appcontentprovider

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.guilda.appcontentprovider.database.NotasProvider.Companion.URI_NOTAS
import com.guilda.appcontentprovider.database.NotesDatabaseHelper.Companion.DESCRICAO
import com.guilda.appcontentprovider.database.NotesDatabaseHelper.Companion.TITULO_NOTAS

class NotaDetailFragment: DialogFragment(),DialogInterface.OnClickListener {

    private lateinit var  notaEditTitulo : EditText
    private lateinit var  notaEditDecription: EditText
    private var id:Long = 0
    companion object{
        private  const val EXTRA_ID = "id"
        fun newInstance(id:Long): NotaDetailFragment{
            val bundle = Bundle()
            bundle.putLong(EXTRA_ID,id)
            val notasFragment = NotaDetailFragment()
            notasFragment.arguments = bundle
            return  notasFragment

        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity?.layoutInflater?.inflate(R.layout.nota_detail, null)
        notaEditTitulo = view?.findViewById(R.id.nota_edt_titulo) as EditText
        notaEditDecription = view.findViewById(R.id.nota_edt_descricao) as EditText

        var newNota = true
        if (arguments != null && arguments?.getLong(EXTRA_ID) != 0L) {
            id = arguments?.getLong(EXTRA_ID) as Long
            val uri = Uri.withAppendedPath(URI_NOTAS, id.toString())
            val cursor =
                activity?.contentResolver?.query(uri, null, null, null, null)

            if (cursor?.moveToNext() as Boolean) {
                newNota = false
                notaEditTitulo.setText(cursor.getString(cursor.getColumnIndex(TITULO_NOTAS)))
                notaEditDecription.setText(cursor.getString(cursor.getColumnIndex(DESCRICAO)))
            }
            cursor.close()
        }
        return AlertDialog.Builder(activity as Activity)
            .setTitle(if (newNota) " Nova Mensagem" else "Editar Mensagem")
            .setView(view)
            .setPositiveButton("Salvar", this)
            .setNegativeButton("Cancelar", this)
            .create()
    }
    override fun onClick(dialog: DialogInterface?, which: Int) {
       val values = ContentValues()
        values.put(TITULO_NOTAS, notaEditTitulo.text.toString())
        values.put(DESCRICAO,notaEditDecription.text.toString())

        if(id != 0L){
            val uri = Uri.withAppendedPath(URI_NOTAS,id.toString())
            context?.contentResolver?.update(uri,values,null,null)
        }else{
            context?.contentResolver?.insert(URI_NOTAS,values)
        }

    }

}