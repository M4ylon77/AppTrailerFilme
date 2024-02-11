package com.maxpayneman.busca_trailler.View.Fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.maxpayneman.busca_trailler.R
import com.maxpayneman.busca_trailler.databinding.FragmentTrailerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var _binding: FragmentTrailerBinding? = null
private val binding get() = _binding!!

/**
 * A simple [Fragment] subclass.
 * Use the [TrailerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TrailerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {  _binding = FragmentTrailerBinding.inflate(inflater, container, false)
        val view = binding.root


        val args = arguments
        val idMovie = args?.getString("filmeId").toString()
        val Movie_Name = args?.getString("filmename").toString()

        binding.closed.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item -> {
                    val fragmentManager = requireActivity().supportFragmentManager

                    // Remove o fragment atual
                    fragmentManager.beginTransaction().remove(this@TrailerFragment).commit()

                    binding.trailerFilm.visibility = View.GONE
                    true
                }

                else -> false
            }
        }

        fetchVideo(idMovie, requireContext(), Movie_Name)

        return view
    }

    private fun fetchVideo(idMovie: String, context: Context, nameMovie : String) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/movie/$idMovie/videos?language=pt-BR")
            .get()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5ZmM1YzgyYjMxNGMwNjNmNDdmZDkyOGU1NzE1NzkxMiIsInN1YiI6IjY1MWNiNDQ1OTY3Y2M3MzQyNWYxZjYxMiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.WAXOM3qxERSbv6SCsygStOwygDjtI4G-WLsOfbUciYI")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val jsonObject = JSONObject(responseBody)
                    val resultsArray = jsonObject.getJSONArray("results")

                    if (resultsArray.length() > 0) {
                        val firstResult = resultsArray.getJSONObject(0)
                        val videoKey = firstResult.getString("key")
                        setupWebView(videoKey)
                        showToast("$nameMovie", context)
                    } else {

                        showToast("Trailer indisponivel!", context)
                    }
                } else {
                    showToast("Erro ao obter vídeos", context)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                showToast("Falha na requisição", context)
            }
        })
    }

    private fun setupWebView(videoId: String) {
        val youTubePlayerView = binding.trailerFilm
        requireActivity().runOnUiThread {
            lifecycle.addObserver(youTubePlayerView)
        }

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {
                youTubePlayer.loadVideo(videoId, 0f)
            }

            override fun onStateChange(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer, state: PlayerConstants.PlayerState) {
                // Your state change logic here
            }
        })
    }

    private fun showToast(message: String, context: Context) {
        requireActivity().runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TrailerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TrailerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}