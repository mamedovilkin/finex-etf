package io.github.mamedovilkin.finexetf.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.svg.SvgDecoder
import com.bumptech.glide.Glide
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.TransactionRecyclerViewItemBinding
import io.github.mamedovilkin.finexetf.room.Converter
import io.github.mamedovilkin.finexetf.room.Fond
import io.github.mamedovilkin.finexetf.room.Type
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionRecyclerViewAdapter(var fonds: List<Fond>) : RecyclerView.Adapter<TransactionRecyclerViewAdapter.TransactionRecyclerViewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionRecyclerViewAdapter.TransactionRecyclerViewViewHolder {
        val binding: TransactionRecyclerViewItemBinding = DataBindingUtil
            .inflate(
                LayoutInflater.from(parent.context),
                R.layout.transaction_recycler_view_item,
                parent,
                false
            )

        return TransactionRecyclerViewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionRecyclerViewAdapter.TransactionRecyclerViewViewHolder, position: Int) {
        val fond = fonds[position]
        if (position == 0 || fond.datetimePurchase != fonds[position - 1].datetimePurchase) {
            holder.binding.datetimeTextView.visibility = View.VISIBLE
        } else {
            holder.binding.datetimeTextView.visibility = View.GONE
        }
        holder.setFond(fond)
    }

    override fun getItemCount(): Int {
        return fonds.size
    }

    inner class TransactionRecyclerViewViewHolder(val binding: TransactionRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root) {

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

                datetimeTextView.text = SimpleDateFormat("MMMM dd, yyyy 'at' HH:mm", Locale.US).format(Date(fond.datetimePurchase))
                nameTextView.text = fond.originalName.trim()
                tickerTextView.text = fond.ticker
                pricePurchaseTextView.text = "${fond.pricePurchase}₽"

                if (fond.type == Converter.fromType(Type.PURCHASE)) {
                    quantityTextView.setTextColor(root.context.resources.getColor(R.color.colorPurchase, null))
                    quantityTextView.text = "+${fond.quantity} pcs."
                }

                if (fond.type == Converter.fromType(Type.SELL)) {
                    quantityTextView.setTextColor(root.context.resources.getColor(R.color.colorSell, null))
                    quantityTextView.text = "-${fond.quantity} pcs."
                }
            }
        }
    }
}