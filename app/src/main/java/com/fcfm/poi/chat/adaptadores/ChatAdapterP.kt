package com.fcfm.poi.chat.adaptadores

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.fcfm.poi.chat.R
import com.fcfm.poi.chat.modelos.MensajeP
import kotlinx.android.synthetic.main.custom_item_mensaje.view.*
import kotlinx.android.synthetic.main.custom_item_mensaje2.view.*
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapterP(private val listaMensajesP: MutableList<MensajeP>) :
    RecyclerView.Adapter<ChatAdapterP.ChatViewHolder>() {

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun asignarInformacion(mensajep: MensajeP) {

          //  itemView.tvUsuario2.text=mensajep.de
            itemView.tvMensaje2.text = mensajep.contenido

            val dateFormater = SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.getDefault())
            val fecha = dateFormater.format(Date(mensajep.timeStamp as Long))

            itemView.tvFecha2.text = fecha

            val params = itemView.contenedorMensaje2.layoutParams

            if (mensajep.esMio) {

                val newParams = FrameLayout.LayoutParams(
                    params.width,
                    params.height,
                    Gravity.END
                )
                itemView.contenedorMensaje2.layoutParams = newParams

            } else {

                val newParams = FrameLayout.LayoutParams(
                    params.width,
                    params.height,
                    Gravity.START
                )
                itemView.contenedorMensaje2.layoutParams = newParams
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.custom_item_mensaje2,parent,false)
           // LayoutInflater.from(parent.context).inflate(R.layout.custom_item_mensaje, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

        holder.asignarInformacion(listaMensajesP[position])
    }

    override fun getItemCount(): Int = listaMensajesP.size
}