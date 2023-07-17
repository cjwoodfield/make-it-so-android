package com.example.makeitso.model

import com.google.firebase.firestore.DocumentId


data class player(
    @DocumentId val id: String = "",
    val name: String = ""
){
    override fun toString(): String {
        return name
    }
}
