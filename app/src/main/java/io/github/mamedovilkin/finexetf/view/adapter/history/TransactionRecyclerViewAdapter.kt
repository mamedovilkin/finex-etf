package io.github.mamedovilkin.finexetf.view.adapter.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.svg.SvgDecoder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.TransactionRecyclerViewItemBinding
import io.github.mamedovilkin.database.util.Converter
import io.github.mamedovilkin.finexetf.di.GlideApp
import io.github.mamedovilkin.database.entity.Asset
import io.github.mamedovilkin.database.util.Type
import io.github.mamedovilkin.finexetf.util.hide
import io.github.mamedovilkin.finexetf.util.show
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionRecyclerViewAdapter(var assets: List<Asset>) : RecyclerView.Adapter<TransactionRecyclerViewAdapter.TransactionRecyclerViewViewHolder>() {

    var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionRecyclerViewViewHolder {
        val binding: TransactionRecyclerViewItemBinding = DataBindingUtil
            .inflate(
                LayoutInflater.from(parent.context),
                R.layout.transaction_recycler_view_item,
                parent,
                false
            )

        return TransactionRecyclerViewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionRecyclerViewViewHolder, position: Int) {
        val asset = assets[position]
        if (position == 0 || asset.datetime != assets[position - 1].datetime) {
            holder.binding.dateTimeTextView.show()
        } else {
            holder.binding.dateTimeTextView.hide()
        }
        holder.bind(asset)
        holder.itemView.setOnLongClickListener {
            onClickListener?.onTransactionClickListener(asset, position)
            true
        }
    }

    override fun getItemCount(): Int = assets.size

    inner class TransactionRecyclerViewViewHolder(val binding: TransactionRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(asset: Asset) {
            binding.apply {
                when (asset.ticker) {
                    "FXTP" -> {
                        GlideApp.with(root.context).load(asset.icon)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(imageView)
                    }
                    "FXRE" -> {
                        imageView.setImageDrawable(ResourcesCompat.getDrawable(root.context.resources, R.drawable.fxre, null))
                    }
                    else -> {
                        imageView.load(asset.icon) {
                            decoderFactory { result, options, _ -> SvgDecoder(result.source, options) }
                        }
                    }
                }

                dateTimeTextView.text = SimpleDateFormat(root.context.resources.getString(R.string.date_time_format), Locale.getDefault()).format(Date(asset.datetime))

                if (Locale.getDefault().language == "ru") {
                    nameTextView.text = asset.name.trim()
                } else {
                    nameTextView.text = asset.originalName.trim()
                }

                tickerTextView.text = asset.ticker
                priceTextView.text = root.context.resources.getString(R.string.price_rub, asset.price)

                val total = asset.price * asset.quantity

                if (asset.type == Converter.fromType(Type.PURCHASE)) {
                    quantityTextView.setTextColor(root.context.resources.getColor(R.color.colorGreen, null))
                    quantityTextView.text = root.context.resources.getString(R.string.positive_quantity_pcs, total, asset.quantity)
                }

                if (asset.type == Converter.fromType(Type.SELL)) {
                    quantityTextView.setTextColor(root.context.resources.getColor(R.color.colorRed, null))
                    quantityTextView.text = root.context.resources.getString(R.string.negative_quantity_pcs, total, asset.quantity)
                }
            }
        }
    }
}