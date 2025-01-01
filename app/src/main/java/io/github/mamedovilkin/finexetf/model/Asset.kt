package io.github.mamedovilkin.finexetf.model

data class Asset(
    val ticker: String,
    val icon: String,
    val name: String,
    val originalName: String,
    val totalQuantity: Int,
    val totalPrice: Double,
)
