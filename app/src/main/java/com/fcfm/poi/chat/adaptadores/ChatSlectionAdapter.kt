package com.fcfm.poi.chat.adaptadores

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.fcfm.poi.chat.R
import com.fcfm.poi.chat.modelos.Chat
import com.fcfm.poi.chat.modelos.Mensaje
import kotlinx.android.synthetic.main.custom_item_groups.view.*
import kotlinx.android.synthetic.main.custom_item_mensaje.view.*
import java.text.SimpleDateFormat
import java.util.*

class ChatSelectionAdapter(private val listaChats: MutableList<Chat>) :
RecyclerView.Adapter<ChatSelectionAdapter.ChatViewHolder>(){

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun asignarInformación(chat: Chat)
        {
            itemView.group_name.text = chat.otherUserUID
            //group image = chat.imagenPerfil
            //Tambien se puede asignar el último mensaje, pero creo que no es necesario
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatSelectionAdapter.ChatViewHolder { //Genera la vista
        return ChatSelectionAdapter.ChatViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.custom_item_groups, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatSelectionAdapter.ChatViewHolder, position: Int) { //Asigna la info
        holder.asignarInformación(listaChats[position])
    }

    override fun getItemCount(): Int = listaChats.size //Retorna el tamaño de la lista
}