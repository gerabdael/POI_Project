package com.fcfm.poi.chat.adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.fcfm.poi.chat.R
import com.fcfm.poi.chat.modelos.Groups
import com.fcfm.poi.chat.modelos.MensajeP

    class GroupsAdapter(private val listaGroupsAdapter: MutableList<Groups>):
RecyclerView.Adapter<GroupsAdapter.GroupViewHolder>(){

    class GroupViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
            return  GroupViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.custom_item_groups,parent,false)
            )

        }

        override fun getItemCount(): Int {
            TODO("Not yet implemented")
        }

        override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
            TODO("Not yet implemented")
        }
    }
  //  class ChatAdapterP(private val listaGruposP: MutableList<MensajeP>) :
    //RecyclerView.Adapter<ChatAdapterP.ChatViewHolder>() {
