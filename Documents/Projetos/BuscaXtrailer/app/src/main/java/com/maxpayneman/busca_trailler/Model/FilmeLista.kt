package com.maxpayneman.busca_trailler.Model

class FilmeLista(var idFilme: String,var data : String,var imgUrl : String, var nome:String, var sinopse: String) {


    override fun toString(): String {
        return "$nome || $data')"
    }
}