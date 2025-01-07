package io.github.mamedovilkin.finexetf.di

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class GlideAppModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val diskCacheSizeBytes: Long = 1024 * 1024 * 100
        builder.setDiskCache(InternalCacheDiskCacheFactory(context,  diskCacheSizeBytes))
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}