package com.vandina.lungsbotkt.utils

import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat


//Project by Irvan Syachrialdi / vandina project copyright 2020
//https://github.com/irvansychrldi

object Time {

    fun timeStamp(): String {

        val timeStamp = Timestamp(System.currentTimeMillis())
        val sdf = SimpleDateFormat("HH:mm")
        val time = sdf.format(Date(timeStamp.time))

        return time.toString()
    }
}