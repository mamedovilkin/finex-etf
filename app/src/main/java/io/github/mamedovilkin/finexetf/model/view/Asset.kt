package io.github.mamedovilkin.finexetf.model.view

data class Asset(
    val ticker: String,
    val icon: String,
    val name: String,
    val originalName: String,
    val navPrice: Double,
    val totalQuantity: Int,
    val totalPrice: Double,
    val totalNavPrice: Double,
)