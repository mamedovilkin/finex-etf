package io.github.mamedovilkin.finexetf.view.adapter.history

import io.github.mamedovilkin.finexetf.model.database.Asset

interface OnClickListener {
    fun onTransactionClickListener(asset: Asset)
}