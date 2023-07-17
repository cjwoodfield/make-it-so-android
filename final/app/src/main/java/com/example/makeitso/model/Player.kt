package com.example.makeitso.model

import com.google.firebase.firestore.DocumentId


data class Player(
    @DocumentId val id: String = "",
    val name: String = "",
    val selected: Boolean = false,
    val userId: String = ""
){
    override fun toString(): String {
        return name
    }
}
