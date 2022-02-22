package com.vandina.lungsbotkt.utils

import android.util.Log


//Project by Irvan Syachrialdi / vandina project copyright 2020
//https://github.com/irvansychrldi

object SolveMath {

    fun solveMath(equation: String) : Int{

        val newEquation = equation.replace(" ", "")
        Log.d("Math", newEquation)

        return when {
            newEquation.contains("+") -> {
                val split = newEquation.split("+")
                val result = split[0].toInt() + split[1].toInt()
                result
            }
            newEquation.contains("-") -> {
                val split = newEquation.split("-")
                val result = split[0].toInt() - split[1].toInt()
                result
            }
            newEquation.contains("*") -> {
                val split = newEquation.split("*")
                val result = split[0].toInt() * split[1].toInt()
                result
            }
            newEquation.contains("/") -> {
                val split = newEquation.split("/")
                val result = split[0].toInt() / split[1].toInt()
                result
            }
            else -> {
                0
            }
        }
    }
}