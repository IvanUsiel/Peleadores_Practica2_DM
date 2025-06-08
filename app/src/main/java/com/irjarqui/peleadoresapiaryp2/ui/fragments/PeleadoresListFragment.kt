package com.irjarqui.peleadoresapiaryp2.ui.fragments

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.irjarqui.peleadoresapiaryp2.R
import com.irjarqui.peleadoresapiaryp2.application.PeleadoresRFApp
import com.irjarqui.peleadoresapiaryp2.data.PeleadorRepository
import com.irjarqui.peleadoresapiaryp2.data.remote.model.FavoritoRequestDto
import com.irjarqui.peleadoresapiaryp2.data.remote.model.PeleadorDto
import com.irjarqui.peleadoresapiaryp2.databinding.FragmentPeleadoresListBinding
import com.irjarqui.peleadoresapiaryp2.ui.adapters.PeleadorAdapter
import kotlinx.coroutines.launch

class PeleadoresListFragment : Fragment() {

    private var _binding: FragmentPeleadoresListBinding? = null
    private val binding get() = _binding!!
    private lateinit var fightersAdapter: PeleadorAdapter
    private lateinit var repository: PeleadorRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPeleadoresListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pbLoading.visibility = View.VISIBLE
        repository = (requireActivity().application as PeleadoresRFApp).repository

        lifecycleScope.launch {

            try {
                val peleadores = repository.getPeleadores()

                fightersAdapter = PeleadorAdapter(peleadores, { selectedPeleador ->
                    val mediaPlayer = MediaPlayer.create(requireContext(), R.raw.sound_enter)
                    mediaPlayer.start()
                    mediaPlayer.setOnCompletionListener {
                        it.release()
                    }
                    selectedPeleador.id.let { id ->
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, PeleadorDetailFragment.newInstance(id))
                            .addToBackStack(null)
                            .commit()
                    }
                }, { selectedPeleador ->
                    toggleFavorite(selectedPeleador, peleadores)
                })

                binding.rvPeleadores.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = fightersAdapter
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    requireContext(),
                    R.string.error_conex,
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                binding.pbLoading.visibility = View.GONE
            }

        }
    }

    private fun toggleFavorite(selectedPeleador: PeleadorDto, peleadores: List<PeleadorDto>) {
        val wasFavorite = selectedPeleador.isFavorite
        selectedPeleador.isFavorite = !selectedPeleador.isFavorite

        if (selectedPeleador.isFavorite) {
            val mediaPlayer = MediaPlayer.create(requireContext(), R.raw.sounf_like)
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener {
                it.release() // libera recursos cuando termina
            }
        }

        fightersAdapter.notifyItemChanged(peleadores.indexOf(selectedPeleador))

        val favoritoRequest = FavoritoRequestDto(
            usuario = getString(R.string.user_example),
            peleadorId = selectedPeleador.id,
            mensaje = getString(R.string.peleador_favorito)
        )

        lifecycleScope.launch {
            try {
                val response = repository.addFavorito(favoritoRequest)
                val message = response.mensaje
                val snackBar = Snackbar.make(
                    binding.root,
                    message,
                    Snackbar.LENGTH_SHORT
                )

                snackBar.setAction(getString(R.string.deshacer)) {
                    selectedPeleador.isFavorite = wasFavorite
                    fightersAdapter.notifyItemChanged(peleadores.indexOf(selectedPeleador))
                }

                snackBar.show()

            } catch (e: Exception) {
                e.printStackTrace()
                Snackbar.make(
                    binding.root,
                    R.string.error_conex,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
