package com.ewoudje.assembly

import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Disposable
import ktx.assets.assetDescriptor
import ktx.assets.async.AssetStorage

data class AssetCollection(
    val cabinet: Texture,
    val cabinetOpen: Texture,
    val landlordReport: Texture,
) : Disposable {
    override fun dispose() {
        cabinetOpen.dispose()
        cabinet.dispose()
        landlordReport.dispose()
    }

    companion object {
        suspend fun loadAsync(assets: AssetStorage): AssetCollection {
            val cabinet = assets.loadAsync(assetDescriptor<Texture>("cabinet.png", TextureLoader.TextureParameter()))
            val cabinetOpen = assets.loadAsync(assetDescriptor("cabinet_open.png", TextureLoader.TextureParameter()))
            val landlordReport = assets.loadAsync(
                assetDescriptor<Texture>(
                    "llreport.png",
                    TextureLoader.TextureParameter()
                )
            )

            return AssetCollection(
                cabinet.await(),
                cabinetOpen.await(),
                landlordReport.await(),
            )
        }
    }
}