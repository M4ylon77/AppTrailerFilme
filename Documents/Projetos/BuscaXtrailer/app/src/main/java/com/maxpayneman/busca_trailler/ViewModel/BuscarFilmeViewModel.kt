package com.maxpayneman.busca_trailler.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maxpayneman.busca_trailler.Model.FilmeLista

class BuscarFilmeViewModel : ViewModel() {
    private val _minhaLista = MutableLiveData<List<FilmeLista>>()
    val minhaLista: LiveData<List<FilmeLista>> get() = _minhaLista

    fun carregarMinhaLista(idFilme: String, data: String, imgUrl: String, nome: String, sinopse: String) {
        val listaFilmes = mutableListOf<FilmeLista>()
        listaFilmes.add(FilmeLista(idFilme, data, imgUrl, nome, sinopse))
        _minhaLista.postValue(listaFilmes)
    }
}