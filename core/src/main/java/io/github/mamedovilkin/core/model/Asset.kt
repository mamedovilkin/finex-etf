package io.github.mamedovilkin.core.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Asset(
    val id: String? = null,
    val ticker: String? = null,
    val icon: String? = null,
    val name: String? = null,
    val originalName: String? = null,
    val isActive: Boolean? = null,
    val navPrice: Double? = null,
    val currencyNav: String? = null,
    var quantity: Long? = null,
    val datetime: Long? = null,
    var price: Double? = null,
    val type: String? = null,
)
