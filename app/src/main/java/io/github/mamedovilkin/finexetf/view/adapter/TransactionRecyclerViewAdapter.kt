package io.github.mamedovilkin.finexetf.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.svg.SvgDecoder
import com.bumptech.glide.Glide
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.TransactionRecyclerViewItemBinding
import io.github.mamedovilkin.finexetf.database.Converter
import io.github.mamedovilkin.finexetf.model.database.Asset
import io.github.mamedovilkin.finexetf.model.database.Type
import io.github.mamedovilkin.finexetf.util.hide
import io.github.mamedovilkin.finexetf.util.show
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionRecyclerViewAdapter(var assets: List<Asset>) : RecyclerView.Adapter<TransactionRecyclerViewAdapter.TransactionRecyclerViewViewHolder>() {

    var onClickListener: OnClickListener? = null

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
        val asset = assets[position]
        if (position == 0 || asset.datetime != assets[position - 1].datetime) {
            holder.binding.dateTimeTextView.show()
        } else {
            holder.binding.dateTimeTextView.hide()
        }
        holder.bind(asset)
        holder.itemView.setOnLongClickListener {
            onClickListener?.onTransactionClickListener(asset)
            true
        }
    }

    override fun getItemCount(): Int = assets.size

    inner class TransactionRecyclerViewViewHolder(val binding: TransactionRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(asset: Asset) {
            binding.apply {
                when (asset.ticker) {
                    "FXTP" -> {
                        Glide
                            .with(root.context)
                            .load(asset.icon)
                            .fitCenter()
                            .into(imageView)
                    }
                    "FXRE" -> {
                        imageView.setImageDrawable(root.context.resources.getDrawable(R.drawable.fxre, null))
                    }
                    else -> {
                        imageView.load(asset.icon) {
                            decoderFactory { result, options, _ -> SvgDecoder(result.source, options) }
                        }
                    }
                }

                dateTimeTextView.text = SimpleDateFormat("MMMM dd, yyyy 'at' HH:mm", Locale.US).format(Date(asset.datetime))
                nameTextView.text = asset.originalName.trim()
                tickerTextView.text = asset.ticker
                priceTextView.text = "${String.format("%.2f", asset.price)}₽"

                if (asset.type == Converter.fromType(Type.PURCHASE)) {
                    quantityTextView.setTextColor(root.context.resources.getColor(R.color.colorPurchase, null))
                    quantityTextView.text = "+${asset.quantity} pcs."
                }

                if (asset.type == Converter.fromType(Type.SELL)) {
                    quantityTextView.setTextColor(root.context.resources.getColor(R.color.colorSell, null))
                    quantityTextView.text = "-${asset.quantity} pcs."
                }
            }
        }
    }
}