package io.github.mamedovilkin.finexetf.view.adapter.fund

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.svg.SvgDecoder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.ChooseFundRecyclerViewItemBinding
import io.github.mamedovilkin.finexetf.di.GlideApp
import io.github.mamedovilkin.finexetf.util.hide
import io.github.mamedovilkin.finexetf.util.show
import io.github.mamedovilkin.network.model.finex.ListFund
import java.util.Locale

class ChooseFundRecyclerViewAdapter(var funds: List<ListFund>) : RecyclerView.Adapter<ChooseFundRecyclerViewAdapter.FundRecyclerViewViewHolder>() {

    var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FundRecyclerViewViewHolder {
        val binding: ChooseFundRecyclerViewItemBinding = DataBindingUtil
            .inflate(
                LayoutInflater.from(parent.context),
                R.layout.choose_fund_recycler_view_item,
                parent,
                false
            )

        return FundRecyclerViewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FundRecyclerViewViewHolder, position: Int) {
        holder.bind(funds[position])
        holder.itemView.setOnClickListener {
            onClickListener?.onFundClickListener(funds[position].ticker)
        }
    }

    override fun getItemCount(): Int {
        return funds.size
    }

    inner class FundRecyclerViewViewHolder(private val binding: ChooseFundRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(fund: ListFund) {
            binding.apply {
                when (fund.ticker) {
                    "FXTP" -> {
                        GlideApp.with(root.context).load(fund.icon)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(imageView)
                    }
                    "FXRE" -> {
                        imageView.setImageDrawable(ResourcesCompat.getDrawable(root.context.resources, R.drawable.fxre, null))
                    }
                    else -> {
                        imageView.load(fund.icon) {
                            decoderFactory { result, options, _ -> SvgDecoder(result.source, options) }
                        }
                    }
                }

                if (Locale.getDefault().language == "ru") {
                    nameTextView.text = fund.name.trim()
                } else {
                    nameTextView.text = fund.originalName.trim()
                }
                tickerTextView.text = fund.ticker

                if (fund.isActive) {
                    closedTextView.hide()
                } else {
                    closedTextView.show()
                }
            }
        }
    }
}