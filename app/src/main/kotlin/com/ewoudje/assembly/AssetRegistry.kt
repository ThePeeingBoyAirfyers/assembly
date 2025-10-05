package com.ewoudje.assembly

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.graphics.Texture
import io.github.classgraph.ClassGraph
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import ktx.assets.assetDescriptor
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync

/**
 * Registry for game assets. Use [AssetConsumer] together with this to manage assets.
 */
object AssetRegistry {
    private val logger = ktx.log.logger<AssetRegistry>()
    private val assets = mutableSetOf<AssetDelegate<*>>()

    suspend fun loadAllAssets(assetStorage: AssetStorage) {
        // Loads all AssetConsumer implementations to register their assets
        ClassGraph()
            .enableClassInfo()
            .scan()
            .use { scanResult ->
                scanResult.getClassesImplementing(AssetConsumer::class.qualifiedName)
                    .loadClasses()
                    .map { it.kotlin.objectInstance as AssetConsumer }
                    .forEach { logger.debug { it::class.qualifiedName!! + " registered assets" } }
            }

        assets.filter { !it.isLoaded }.map { KtxAsync.async { it.load(assetStorage) } }.joinAll()
    }

    /** Internal function to register an asset delegate. Not to be used directly. **/
    internal fun _addAsset(delegate: AssetDelegate<*>) = assets.add(delegate)
}