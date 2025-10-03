package com.ewoudje.assembly.scenes.desk.objects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.ewoudje.assembly.AssetCollection
import com.ewoudje.assembly.base.Drawable
import org.kodein.di.*

class TableObjectDrawable(
    override val di: DI,
    override val diContext: DIContext<TableObject>,
    asset: (AssetCollection) -> Texture
) : Drawable, DIAware {
    val position: TableObjectPosition by instance()
    val batch: Batch by instance()
    val texture by newInstance { asset(instance()) }
    override var zDepth: Int = 0

    override fun draw() {
        batch.draw(texture, position.x, position.y)
    }
}