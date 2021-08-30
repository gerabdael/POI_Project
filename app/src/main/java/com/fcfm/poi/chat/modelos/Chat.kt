package com.fcfm.poi.chat.modelos

import android.media.Image

class Chat (
    val id: String = "",
    var ultimoMensaje: String = "",
    val loggedUserUID: String = "",
    val otherUserUID: String = "",
    val imagenPerfil : Image){

}