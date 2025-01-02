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
import io.github.mamedovilkin.finexetf.room.Fund
import io.github.mamedovilkin.finexetf.room.Type
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionRecyclerViewAdapter(var funds: List<Fund>) : RecyclerView.Adapter<TransactionRecyclerViewAdapter.TransactionRecyclerViewViewHolder>() {

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
        val fund = funds[position]
        if (position == 0 || fund.datetime != funds[position - 1].datetime) {
            holder.binding.dateTimeTextView.visibility = View.VISIBLE
        } else {
            holder.binding.dateTimeTextView.visibility = View.GONE
        }
        holder.setFund(fund)
    }

    override fun getItemCount(): Int {
        return funds.size
    }

    inner class TransactionRecyclerViewViewHolder(val binding: TransactionRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setFund(fund: Fund) {
            binding.apply {
                when (fund.ticker) {
                    "FXTP" -> {
                        Glide
                            .with(root.context)
                            .load(fund.icon)
                            .fitCenter()
                            .into(imageView)
                    }
                    "FXRE" -> {
                        imageView.setImageDrawable(root.context.resources.getDrawable(R.drawable.fxre, null))
                    }
                    else -> {
                        imageView.load(fund.icon) {
                            decoderFactory { result, options, _ -> SvgDecoder(result.source, options) }
                        }
                    }
                }

                dateTimeTextView.text = SimpleDateFormat("MMMM dd, yyyy 'at' HH:mm", Locale.US).format(Date(fund.datetime))
                nameTextView.text = fund.originalName.trim()
                tickerTextView.text = fund.ticker
                priceTextView.text = "${String.format("%.2f", fund.price)}₽"

                if (fund.type == Converter.fromType(Type.PURCHASE)) {
                    quantityTextView.setTextColor(root.context.resources.getColor(R.color.colorPurchase, null))
                    quantityTextView.text = "+${fund.quantity} pcs."
                }

                if (fund.type == Converter.fromType(Type.SELL)) {
                    quantityTextView.setTextColor(root.context.resources.getColor(R.color.colorSell, null))
                    quantityTextView.text = "-${fund.quantity} pcs."
                }
            }
        }
    }
}