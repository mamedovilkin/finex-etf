package io.github.mamedovilkin.finexetf.view.adapter.history

import io.github.mamedovilkin.database.entity.Asset

interface OnClickListener {
    fun onTransactionClickListener(asset: Asset, position: Int)
}