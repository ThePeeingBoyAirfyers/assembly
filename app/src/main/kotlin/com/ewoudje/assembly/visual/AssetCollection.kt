package com.ewoudje.manypapers.visual

import com.badlogic.gdx.assets.loaders.ModelLoader
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.utils.Disposable
import com.ewoudje.manypapers.state.computer.ComputerCase
import com.ewoudje.manypapers.state.computer.DefaultComputerCase
import kotlinx.coroutines.awaitAll
import ktx.assets.assetDescriptor
import ktx.assets.async.AssetStorage
import ktx.assets.dispose
import org.kodein.di.DITrigger

data class AssetCollection(
    val cases: Map<ComputerCase, Model>,
    val table: Model,
    val paper: Texture,
    val background: Texture
) : Disposable {
    override fun dispose() {
        cases.forEach { it.value.dispose() }
        table.dispose()
        paper.dispose()
        background.dispose()
    }

    companion object {
        suspend fun loadAsync(assets: AssetStorage): AssetCollection {
            val case =  assets.loadAsync(assetDescriptor<Model>("models/computer.g3db", ModelLoader.ModelParameters()))

            val table = assets.loadAsync(assetDescriptor<Model>("models/table.g3db", ModelLoader.ModelParameters()))

            val paper = assets.loadAsync(
                assetDescriptor<Texture>("paper.png")
            )

            val background = assets.loadAsync(assetDescriptor<Texture>("background/background.png", TextureLoader.TextureParameter()))

            return AssetCollection(
                mapOf(DefaultComputerCase to case.await().center().rotateZ()),
                table.await().center().rotateZ(),
                paper.await(),
                background.await()
            )
        }
    }
}
