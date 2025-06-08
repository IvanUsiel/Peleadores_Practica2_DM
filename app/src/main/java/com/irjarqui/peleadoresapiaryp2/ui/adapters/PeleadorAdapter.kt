package com.irjarqui.peleadoresapiaryp2.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.irjarqui.peleadoresapiaryp2.R
import com.irjarqui.peleadoresapiaryp2.data.remote.model.PeleadorDto
import com.irjarqui.peleadoresapiaryp2.databinding.PeleadorItemBinding

class PeleadorAdapter(
    private val peleadores: List<PeleadorDto>,
    private val onItemClick: (PeleadorDto) -> Unit,
    private val onFavoriteClick: (PeleadorDto) -> Unit
) : RecyclerView.Adapter<PeleadorAdapter.PeleadorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeleadorViewHolder {
        val binding = PeleadorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PeleadorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PeleadorViewHolder, position: Int) {
        val peleador = peleadores[position]
        holder.bind(peleador)

        holder.itemView.setOnClickListener {
            onItemClick(peleador)
        }

        holder.binding.ivLike.setOnClickListener {
            onFavoriteClick(peleador)
        }
    }

    override fun getItemCount(): Int = peleadores.size

    inner class PeleadorViewHolder(val binding: PeleadorItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(peleador: PeleadorDto) {
            binding.tvNombre.text = peleador.nombre
            binding.tvDisciplina.text = peleador.disciplina
            binding.tvRecord.text = peleador.record

            if (peleador.isFavorite) {
                binding.ivLike.setImageResource(R.drawable.ic_favorite_filled)
            } else {
                binding.ivLike.setImageResource(R.drawable.ic_favorite_empty)
            }

            Glide.with(binding.root.context)
                .load(peleador.imagen)
                .into(binding.ivImagen)
        }
    }
}
