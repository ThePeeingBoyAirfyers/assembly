package com.ewoudje.assembly

import com.badlogic.gdx.assets.AssetDescriptor
import ktx.assets.async.AssetStorage
import kotlin.reflect.KProperty

class AssetDelegate<T>(val descriptor: AssetDescriptor<T>) {
    private var asset: T? = null

    val isLoaded: Boolean
        get() = asset != null

    suspend fun load(storage: AssetStorage) {
        if (isLoaded) return
        asset = storage.loadAsync(descriptor).await()
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
        asset ?: throw IllegalStateException("Asset ${descriptor.fileName} not loaded yet")
}