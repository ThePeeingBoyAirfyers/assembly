package com.ewoudje.manypapers.visual.drawables

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.ewoudje.manypapers.state.table.TableObject
import com.ewoudje.manypapers.visual.AssetCollection
import com.ewoudje.manypapers.visual.Drawable
import org.kodein.di.DI
import org.kodein.di.DIContext
import org.kodein.di.instance

class PaperDrawable(override val di: DI, override val diContext: DIContext<TableObject>) : Drawable {
    val batch: Batch by instance()
    val assets: AssetCollection by instance()
    val position: Vector2 by instance("transformedTablePosition")
    val screenRatio: Float by instance(tag = "ratio")

    override fun draw() {
        val scale = 0.1f
        val ratio = assets.paper.width / assets.paper.height.toFloat()
        val scaledWidth = scale * ratio

        batch.draw(assets.paper, position.x - (scaledWidth / 2), position.y * screenRatio - (scale / 2), scaledWidth, scale)
    }
}
