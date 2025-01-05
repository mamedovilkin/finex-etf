package io.github.mamedovilkin.finexetf.view.adapter

import io.github.mamedovilkin.finexetf.model.database.Asset

interface OnClickListener {
    fun onFundClickListener(ticker: String)
    fun onTransactionClickListener(asset: Asset)
}