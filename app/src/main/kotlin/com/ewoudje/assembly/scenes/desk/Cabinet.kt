package com.ewoudje.assembly.scenes.desk

import com.badlogic.gdx.graphics.g2d.Batch
import com.ewoudje.assembly.AssetCollection
import com.ewoudje.assembly.base.Drawable
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import org.kodein.di.newInstance

class Cabinet(override val di: DI) : Drawable, DIAware {
    val batch: Batch by instance()
    val assets: AssetCollection by instance()
    val states by newInstance { arrayOf(false, false, false) }

    override val zDepth: Int = 0

    override fun draw() {
        batch.draw(assets.cabinet, x.toFloat(), y.toFloat())

        for (i in states.indices) {
            if (states[i]) {
                val cy = assets.cabinet.height - cabinetYs[i]
                batch.draw(assets.cabinetOpen, (cabinetX + x).toFloat(), (y + cy - assets.cabinetOpen.height).toFloat())
            }
        }
    }

    companion object {
        val x = 0
        val y = 15
        val cabinetYs = listOf(30, 78, 125)
        val cabinetX = 1
    }
}