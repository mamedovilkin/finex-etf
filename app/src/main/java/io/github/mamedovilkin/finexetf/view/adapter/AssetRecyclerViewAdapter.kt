package io.github.mamedovilkin.finexetf.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.svg.SvgDecoder
import com.bumptech.glide.Glide
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.AssetRecyclerViewItemBinding
import io.github.mamedovilkin.finexetf.databinding.NetWorthRecyclerViewHeaderBinding
import io.github.mamedovilkin.finexetf.model.view.Asset
import io.github.mamedovilkin.finexetf.view.fragment.viewpager.NetWorthRUBFragment
import io.github.mamedovilkin.finexetf.view.fragment.viewpager.NetWorthUSDFragment
import io.github.mamedovilkin.finexetf.viewmodel.MyAssetsViewModel

class AssetRecyclerViewAdapter(
    private val assets: List<Asset>,
    private val viewModel: MyAssetsViewModel,
    private val fragmentManager: FragmentManager,
    private val lifecycle: Lifecycle,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val HEADER: Int = 0
    private val LIST: Int = 1
    var rate: String = ""
    var dateFrom: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == HEADER) {
            val binding: NetWorthRecyclerViewHeaderBinding = DataBindingUtil
                .inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.net_worth_recycler_view_header,
                    parent,
                    false
                )

            return NetWorthRecyclerViewViewHolder(binding)
        } else {
            val binding: AssetRecyclerViewItemBinding = DataBindingUtil
                .inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.asset_recycler_view_item,
                    parent,
                    false
                )

            return AssetRecyclerViewViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            HEADER
        } else {
            LIST
        }
    }

    override fun getItemCount(): Int = assets.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AssetRecyclerViewViewHolder) {
            holder.bind(assets[position - 1])
        }
    }

    inner class NetWorthRecyclerViewViewHolder(binding: NetWorthRecyclerViewHeaderBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                exchangeRateTextView.text = "1$ = $rate₽ (Bank of Russia from $dateFrom)"
                viewPager2.adapter = NetWorthFragmentStateAdapter(
                    listOf(NetWorthRUBFragment(viewModel), NetWorthUSDFragment(viewModel)),
                    fragmentManager,
                    lifecycle
                )
                circleIndicator3.setViewPager(viewPager2)
            }
        }
    }

    inner class AssetRecyclerViewViewHolder(private val binding: AssetRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root) {

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

                nameTextView.text = asset.originalName.trim()
                tickerTextView.text = asset.ticker
                totalPriceTextView.text = "${String.format("%.2f", asset.totalNavPrice)}₽"
                totalQuantityTextView.text = "${asset.totalQuantity} pcs."
            }
        }
    }
}