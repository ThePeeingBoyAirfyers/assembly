package com.ewoudje.assembly.visual

import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Disposable
import ktx.assets.assetDescriptor
import ktx.assets.async.AssetStorage

data class AssetCollection(
    val cabinet: Texture,
    val cabinetOpen: Texture,
) : Disposable {
    override fun dispose() {
        cabinetOpen.dispose()
        cabinet.dispose()
    }

    companion object {
        suspend fun loadAsync(assets: AssetStorage): AssetCollection {
            val cabinet = assets.loadAsync(assetDescriptor<Texture>("cabinet.png", TextureLoader.TextureParameter()))
            val cabinetOpen = assets.loadAsync(assetDescriptor("cabinet_open.png", TextureLoader.TextureParameter()))

            return AssetCollection(
                cabinet.await(),
                cabinetOpen.await()
            )
        }
    }
}
