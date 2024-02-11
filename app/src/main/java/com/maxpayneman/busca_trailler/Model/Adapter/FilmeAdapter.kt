package com.maxpayneman.busca_trailler.Model.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.maxpayneman.busca_trailler.Model.FilmeLista
import com.maxpayneman.busca_trailler.Model.Interface.ItemClickListener
import com.maxpayneman.busca_trailler.R

class FilmeAdapter(
        private val context: Context,
        private var listaDeFilmes: List<FilmeLista>,
        private val itemClickListener: ItemClickListener? = null) : BaseAdapter() {


    override fun getCount(): Int {
        return listaDeFilmes.size
    }

    override fun getItem(position: Int): Any {
        return listaDeFilmes[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewHolder: ViewHolder


        if (view == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.item_film, null)

            viewHolder = ViewHolder()
            viewHolder.nomeFilmeTextView = view.findViewById(R.id.nome_filme)
            viewHolder.imageView = view.findViewById(R.id.img_filmes1)
              // Adicionado para o botão de remover

            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val filme = listaDeFilmes[position]
        viewHolder.nomeFilmeTextView?.text = filme.nome
        Glide.with(context).load(filme.imgUrl).into(viewHolder.imageView!!)

        viewHolder.imageView?.setOnClickListener {
            val filmeSelecionado = listaDeFilmes[position]
            itemClickListener?.onItemClicked(filmeSelecionado)

        }
        return view!!
    }

    private class ViewHolder {
        var nomeFilmeTextView: TextView? = null
        var imageView: ImageView? = null
        var id: TextView? = null
    }

    fun updateLista(novaLista: List<FilmeLista>) {
        listaDeFilmes = novaLista
        notifyDataSetChanged()
    }
}


