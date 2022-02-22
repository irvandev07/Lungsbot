package com.vandina.lungsbotkt.utils

//Project by Irvan Syachrialdi / vandina project copyright 2020
//https://github.com/irvansychrldi

object BotResponse {

    fun basicResponses(_message: String): String {

        val random = (0..2).random()
        val message =_message.toLowerCase()

        return when {

            message.contains("berjalan")->{"Bagaimana cara berbicara anda?"}

            message.contains("berbicara")->{"Bagaimana cara berbicara anda?"}

            message.contains("istirahat")->{"Bagaimana cara berbicara anda?"}

            message.contains("ya") ->{"Kapan anda merasakan sesak nafas?"}

            message.contains("satu kalimat") ->{"Bagaimana kondisi anda saat ini?"}

            message.contains("beberapa kata") ->{"Bagaimana kondisi anda saat ini?"}

            message.contains("kata demi kata") ->{"Bagaimana kondisi anda saat ini?"}

            message.contains("mungkin gelisah") ->{"Berapakah Frekuensi Nafas anda?"}

            message.contains("gelisah") ->{"Berapakah Frekuensi Nafas anda?"}

            message.contains("<20/menit") ->{"Berapakah banyak denyut nadi anda permenit?"}

            message.contains("20-30 menit") ->{"Berapakah banyak denyut nadi anda permenit?"}

            message.contains(">30/menit") ->{"Berapakah banyak denyut nadi anda permenit?"}

            message.contains("<100") ->{"Apakah Nafas anda terdengar suara melengking?"}

            message.contains("100-200") ->{"Apakah Nafas anda terdengar suara melengking?"}

            message.contains(">120") ->{"Apakah Nafas anda terdengar suara melengking?"}


            //When the programme doesn't understand...
            else -> {
                when (random) {
                    0 -> "Lubo tidak mengerti..."
                    1 -> "Silahkan mencoba pertanyaan lain"
                    2 -> "Maaf, Lubo tidak paham..."
                    else -> "error"
                }
            }
        }
    }
}