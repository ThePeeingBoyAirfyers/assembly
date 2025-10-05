package com.ewoudje.assembly.scenes.desk

import com.badlogic.gdx.graphics.g2d.Batch
import com.ewoudje.assembly.AssetConsumer
import com.ewoudje.assembly.AssetRegistry
import com.ewoudje.assembly.base.Drawable
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import org.kodein.di.newInstance

class Cabinet(override val di: DI) : Drawable, DIAware {
    val batch: Batch by instance()
    val states by newInstance { arrayOf(false, false, false) }

    override val zDepth: Int = 0

    override fun draw() {
        batch.draw(cabinetTexture, x.toFloat(), y.toFloat())

        for (i in states.indices) {
            if (states[i]) {
                val cy = cabinetTexture.height - cabinetYs[i]
                batch.draw(cabinetOpenTexture, (cabinetX + x).toFloat(), (y + cy - cabinetOpenTexture.height).toFloat())
            }
        }
    }

    companion object : AssetConsumer {
        val x = 0
        val y = 15
        val cabinetYs = listOf(30, 78, 125)
        val cabinetX = 1

        val cabinetTexture by AssetRegistry.texture("cabinet.png")
        val cabinetOpenTexture by AssetRegistry.texture("cabinet_open.png")
    }
}