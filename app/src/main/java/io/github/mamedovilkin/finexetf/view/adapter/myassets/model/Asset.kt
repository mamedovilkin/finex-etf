package io.github.mamedovilkin.finexetf.view.adapter.myassets.model

data class Asset(
    val ticker: String,
    val icon: String,
    val name: String,
    val originalName: String,
    val isActive: Boolean,
    val navPrice: Double,
    val totalQuantity: Long,
    val totalPrice: Double,
    val totalNavPrice: Double,
)