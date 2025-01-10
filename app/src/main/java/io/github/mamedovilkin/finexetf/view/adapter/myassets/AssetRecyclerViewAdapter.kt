package io.github.mamedovilkin.finexetf.view.adapter.myassets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.svg.SvgDecoder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.AssetRecyclerViewItemBinding
import io.github.mamedovilkin.finexetf.databinding.AssetsRecyclerViewHeaderBinding
import io.github.mamedovilkin.finexetf.di.GlideApp
import io.github.mamedovilkin.finexetf.model.Asset
import io.github.mamedovilkin.finexetf.util.hide
import io.github.mamedovilkin.finexetf.util.show
import io.github.mamedovilkin.finexetf.view.adapter.fund.OnClickListener
import io.github.mamedovilkin.finexetf.view.fragment.viewpager.NetWorthEURFragment
import io.github.mamedovilkin.finexetf.view.fragment.viewpager.NetWorthKZTFragment
import io.github.mamedovilkin.finexetf.view.fragment.viewpager.NetWorthRUBFragment
import io.github.mamedovilkin.finexetf.view.fragment.viewpager.NetWorthUSDFragment
import java.util.Locale

class AssetRecyclerViewAdapter(
    private val assets: List<Asset>,
    private val fragmentManager: FragmentManager,
    private val lifecycle: Lifecycle,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val HEADER: Int = 0
    private val LIST: Int = 1
    var onClickListener: OnClickListener? = null
    var rates: List<Double> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == HEADER) {
            val binding: AssetsRecyclerViewHeaderBinding = DataBindingUtil
                .inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.assets_recycler_view_header,
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
            val asset = assets[position - 1]
            holder.bind(asset)
            holder.itemView.setOnClickListener {
                onClickListener?.onFundClickListener(asset.ticker)
            }
        }
    }

    inner class NetWorthRecyclerViewViewHolder(binding: AssetsRecyclerViewHeaderBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                val showExchangeRates = PreferenceManager.getDefaultSharedPreferences(root.context).getBoolean("show_exchange_rates", false)

                if (showExchangeRates) {
                    val usdRate = String.format(Locale.ROOT, "%.2f", rates[0])
                    val eurRate = String.format(Locale.ROOT, "%.2f", rates[1])
                    val kztRate = String.format(Locale.ROOT, "%.2f", rates[2])
                    exchangeRateTextView.text = "1$ = $usdRate₽ / 1€ = $eurRate₽ / 1₸ = $kztRate₽"
                    exchangeRateTextView.show()
                } else {
                    exchangeRateTextView.hide()
                }

                viewPager2.adapter = NetWorthFragmentStateAdapter(
                    listOf(NetWorthRUBFragment(), NetWorthUSDFragment(), NetWorthEURFragment(), NetWorthKZTFragment()),
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
                        GlideApp.with(root.context).load(asset.icon)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(imageView)
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