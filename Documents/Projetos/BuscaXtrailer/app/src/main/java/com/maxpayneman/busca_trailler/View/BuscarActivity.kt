package com.maxpayneman.busca_trailler.View

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.maxpayneman.busca_trailler.Model.Adapter.FilmeAdapter
import com.maxpayneman.busca_trailler.Model.FilmeLista
import com.maxpayneman.busca_trailler.Model.Interface.ItemClickListener
import com.maxpayneman.busca_trailler.R
import com.maxpayneman.busca_trailler.View.Fragment.TrailerFragment
import com.maxpayneman.busca_trailler.databinding.ActivityBuscarBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class BuscarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuscarBinding
    private var Api_Key =
        "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5ZmM1YzgyYjMxNGMwNjNmNDdmZDkyOGU1NzE1NzkxMiIsInN1YiI6IjY1MWNiNDQ1OTY3Y2M3MzQyNWYxZjYxMiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.WAXOM3qxERSbv6SCsygStOwygDjtI4G-WLsOfbUciYI"
    private var filmeList = ArrayList<FilmeLista>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuscarBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val meuAdaptador = FilmeAdapter(
            applicationContext,
            mutableListOf(),
            object : ItemClickListener {
                override fun onItemClicked(filme: FilmeLista) {
                    // Lógica para lidar com o clique em um item da lista
                    val idFilme = filme.idFilme
                    val nome = filme.nome
                    val img = filme.imgUrl
                    val data = filme.data
                    val descricao = filme.sinopse


                    val budle = Bundle()


                    budle.putString("filmeId", idFilme)
                    budle.putString("filmename", nome)
                    budle.putString("filmeimg", img)
                    budle.putString("filmedesc", descricao)
                    budle.getString("filmedata", data)

                    val trailer = TrailerFragment()
                    trailer.arguments = budle
                    replaceFragment(trailer)

                }
            }
        )

        // Associe o Adapter à ListView ou RecyclerView
        binding.minhaListFilme.adapter = meuAdaptador

        binding.buscarFilme.setOnClickListener {
            filmeList.clear()
            CoroutineScope(Dispatchers.IO).launch {
                val nameFilme = binding.editQuery.text.toString()

                val client = OkHttpClient()

                val request = Request.Builder()
                    .url("https://api.themoviedb.org/3/search/movie?query=${nameFilme}&language=pt-BR&page=1&include_adult=false")
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader(
                        "Authorization",
                        "Bearer $Api_Key"
                    )
                    .build()

                val response = client.newCall(request).execute()

                val responseData = response.body?.string()

                responseData?.let {
                    val jsonObject = JSONObject(it)
                    val resultsArray = jsonObject.getJSONArray("results")

                    for (i in 0 until resultsArray.length()) {
                        val filme = resultsArray.getJSONObject(i)
                        val titulo = filme.getString("title")
                        val ano = filme.getString("release_date")
                        val descricao = filme.getString("overview")
                        val id = filme.getString("id")
                        val backGround = filme.getString("backdrop_path")

                        val detalhesRequest = Request.Builder()
                            .url("https://api.themoviedb.org/3/movie/$id?language=pt-BR")
                            .get()
                            .addHeader("accept", "application/json")
                            .addHeader(
                                "Authorization",
                                "Bearer $Api_Key"
                            )
                            .build()

                        val detalhesResponse = client.newCall(detalhesRequest).execute()
                        val detalhesData = detalhesResponse.body?.string()

                        detalhesData?.let {
                            val detalhesJson = JSONObject(it)
                            val imagemPath = detalhesJson.getString("poster_path")
                            val imagemUrl = "https://image.tmdb.org/t/p/w500$imagemPath"
                            val idtent = detalhesJson.getString("id")

                            // Adiciona o filme à lista com o URL da imagem
                            filmeList.add(FilmeLista(idtent, ano, imagemUrl, titulo, descricao))
                        }
                    }

                    // Atualiza o Adapter com a nova lista de filmes na thread principal
                    runOnUiThread {
                        meuAdaptador.updateLista(filmeList)
                    }
                }
            }
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        Log.d("HomeFragment", "Replacing fragment with ${fragment.javaClass.simpleName}")
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentTrailer, fragment)
        fragmentTransaction.commit()
    }

}