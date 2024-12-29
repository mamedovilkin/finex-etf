package io.github.mamedovilkin.finexetf.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.svg.SvgDecoder
import com.bumptech.glide.Glide
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.FondRecyclerViewItemBinding
import io.github.mamedovilkin.finexetf.model.Fond
import io.github.mamedovilkin.finexetf.model.Fonds

class FondRecyclerViewAdapter(private val fonds: Fonds) : RecyclerView.Adapter<FondRecyclerViewAdapter.FondRecyclerViewViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FondRecyclerViewViewHolder {
        val binding: FondRecyclerViewItemBinding = DataBindingUtil
            .inflate(
                LayoutInflater.from(parent.context),
                R.layout.fond_recycler_view_item,
                parent,
                false
            )

        return FondRecyclerViewViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return fonds.size
    }

    override fun onBindViewHolder(holder: FondRecyclerViewViewHolder, position: Int) {
        holder.setFond(fonds[position])
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClickListener(position)
        }
    }

    inner class FondRecyclerViewViewHolder(private val binding: FondRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setFond(fond: Fond) {
            binding.apply {
                when (fond.ticker) {
                    "FXTP" -> {
                        Glide
                            .with(root.context)
                            .load(fond.icon)
                            .fitCenter()
                            .into(imageView)
                    }
                    "FXRE" -> {
                        imageView.setImageDrawable(root.context.resources.getDrawable(R.drawable.fxre, null))
                    }
                    else -> {
                        imageView.load(fond.icon) {
                            decoderFactory { result, options, _ -> SvgDecoder(result.source, options) }
                        }
                    }
                }

                nameTextView.text = fond.originalName.trim()
                tickerTextView.text = fond.ticker
            }
        }
    }
}