package com.ewoudje.assembly

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.graphics.Texture
import ktx.assets.assetDescriptor

/**
 * Marker interface for classes/objects that consume assets.
 * To be used in conjunction with [AssetRegistry] to ensure proper asset management.
 */
interface AssetConsumer {

    fun AssetRegistry.texture(
        name: String,
        params: TextureLoader.TextureParameter = TextureLoader.TextureParameter()
    ): AssetDelegate<Texture> =
        asset(assetDescriptor<Texture>(name, params))

    fun <T> AssetRegistry.asset(descriptor: AssetDescriptor<T>): AssetDelegate<T> =
        AssetDelegate(descriptor).apply { AssetRegistry._addAsset(this) }

}