package com.irjarqui.peleadoresapiaryp2.ui.fragments

import android.graphics.Bitmap
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.irjarqui.peleadoresapiaryp2.R
import com.irjarqui.peleadoresapiaryp2.application.PeleadoresRFApp
import com.irjarqui.peleadoresapiaryp2.data.PeleadorRepository
import com.irjarqui.peleadoresapiaryp2.data.remote.model.PeleadorDto
import com.irjarqui.peleadoresapiaryp2.databinding.FragmentPeleadorDetailBinding
import com.irjarqui.peleadoresapiaryp2.utils.Constants
import kotlinx.coroutines.launch

import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.MapStyleOptions
import androidx.core.graphics.scale
import androidx.core.graphics.createBitmap

class PeleadorDetailFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentPeleadorDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var fighterRepository: PeleadorRepository
    private var fighterId: Int = -1
    private lateinit var fighter: PeleadorDto

    private var mapFragment: SupportMapFragment? = null


    companion object {

        fun newInstance(id: Int): PeleadorDetailFragment {
            val fragment = PeleadorDetailFragment()
            val args = Bundle()
            args.putInt(Constants.ARG_PELEADOR_ID, id)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPeleadorDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fighterRepository = (requireActivity().application as PeleadoresRFApp).repository
        fighterId = arguments?.getInt(Constants.ARG_PELEADOR_ID) ?: return
        binding.pbLoading.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                fighter = fighterRepository.getPeleadorDetail(fighterId)

                // Setear datos de texto
                binding.tvNombre.text = fighter.nombre
                binding.tvDisciplina.text = getString(R.string.disciplina_label, fighter.disciplina)
                binding.tvRecord.text = getString(R.string.disciplina_record, fighter.record)
                binding.tvTitulos.text = getString(R.string.disciplina_titulos, fighter.titulos)
                binding.tvBiografia.text = fighter.biografia

                val videoUrl = fighter.video
                val videoId = videoUrl.substringAfterLast("/")
                val embedUrl = "https://www.youtube.com/embed/$videoId?controls=1"
                val webView = binding.youtubeWebView
                webView.settings.javaScriptEnabled = true
                webView.webChromeClient = android.webkit.WebChromeClient()
                webView.loadUrl(embedUrl)

                mapFragment = childFragmentManager
                    .findFragmentById(R.id.mapNacimiento) as? SupportMapFragment
                mapFragment?.getMapAsync(this@PeleadorDetailFragment)

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(),
                    getString(R.string.error_al_cargar), Toast.LENGTH_SHORT).show()
            } finally {
                binding.pbLoading.visibility = View.GONE
            }
        }

        binding.btnStart.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val response = fighterRepository.deletePeleador(fighterId)
                    if (response.isSuccessful) {
                        val mediaPlayer = MediaPlayer.create(requireContext(), R.raw.sound_delete)
                        mediaPlayer.start()
                        mediaPlayer.setOnCompletionListener { it.release() }

                        Toast.makeText(requireContext(),
                            getString(R.string.peleador_eliminado), Toast.LENGTH_SHORT).show()
                        requireActivity().supportFragmentManager.popBackStack()
                    } else {
                        Toast.makeText(requireContext(),
                            getString(R.string.error_al_eliminar), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(),
                        getString(R.string.error_conex), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        try {
            val success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style_dark)
            )
            if (!success) {
                Toast.makeText(requireContext(),
                    getString(R.string.error_estilo_del_mapa), Toast.LENGTH_SHORT).show()
            }

            val (lat, lng) = fighter.loc.split(",")
            val location = LatLng(lat.toDouble(), lng.toDouble())

            createMarker(
                map = googleMap,
                latLng = location,
                nombre = fighter.nombre,
                lugar = fighter.nacimiento,
                imagenUrl = fighter.imagen
            )

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), getString(R.string.error_mapa), Toast.LENGTH_SHORT).show()
        }
    }


    private fun createMarker(
        map: GoogleMap,
        latLng: LatLng,
        nombre: String,
        lugar: String,
        imagenUrl: String
    ) {
        Glide.with(requireContext())
            .asBitmap()
            .load(imagenUrl)
            .into(object : com.bumptech.glide.request.target.CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    val finalSize = 200
                    val scaledBitmap = resource.scale(finalSize, finalSize)
                    val output = createBitmap(finalSize + 20, finalSize + 20)
                    val canvas = android.graphics.Canvas(output)
                    val left = 10f
                    val top = 10f
                    canvas.drawBitmap(scaledBitmap, left, top, null)

                    val icon = BitmapDescriptorFactory.fromBitmap(output)

                    map.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(nombre)
                            .snippet(getString(R.string.lugar_de_nacimiento, lugar))
                            .icon(icon)
                    )
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f), 500, null)
                }

                override fun onLoadCleared(placeholder: android.graphics.drawable.Drawable?) {}
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
