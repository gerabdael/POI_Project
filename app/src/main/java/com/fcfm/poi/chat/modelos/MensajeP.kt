package com.fcfm.poi.chat.modelos

import com.google.firebase.database.Exclude

class MensajeP(
        var id: String = "",
        var id2: String = "",
        var contenido: String = "",
        var de: String = "",
        var para: String = "",
        val timeStamp: Any? = null
) {
    @Exclude
    var esMio: Boolean = false
}
