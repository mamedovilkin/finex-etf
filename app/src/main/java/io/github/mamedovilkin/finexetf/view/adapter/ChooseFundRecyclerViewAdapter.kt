package io.github.mamedovilkin.finexetf.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.svg.SvgDecoder
import com.bumptech.glide.Glide
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.ChooseFundRecyclerViewItemBinding
import io.github.mamedovilkin.finexetf.model.network.ListFund

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

                nameTextView.text = fund.originalName.trim()
                tickerTextView.text = fund.ticker
            }
        }
    }
}