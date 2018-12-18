package com.example.alexj.appFinal02

import java.io.Serializable
import java.sql.Date


data class Medicion(var fecha: String, var glucosa: Int, var km: Int, var peso: Int, var carbohidratos: Int ) :
    Serializable