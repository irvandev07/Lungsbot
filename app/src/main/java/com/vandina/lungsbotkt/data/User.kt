package com.vandina.lungsbotkt.data

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties


//Project by Irvan Syachrialdi / vandina project copyright 2020
//https://github.com/irvansychrldi

@IgnoreExtraProperties
data class User(
    var nama: String? = "",
    var email: String? = "",
    val profileUser : String? = ""
) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to nama,
            "email" to email,
            "profileUser" to profileUser
        )
    }
}
