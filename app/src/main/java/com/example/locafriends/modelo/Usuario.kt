package com.example.locafriends.modelo

//MVP Modelo Vista Presentacion
// Esta clase tambien es clase modelo de tipo entidad: solo contiene atributos
class Usuario {
    var nombre:String? = null
    var nickname:String? = null
    var email:String? = null
    var localizacion:Localizacion? = null

    override fun toString(): String {
        return nickname.toString()
    }
}