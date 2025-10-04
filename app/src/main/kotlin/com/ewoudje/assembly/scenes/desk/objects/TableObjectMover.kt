package com.ewoudje.assembly.scenes.desk.objects

import com.ewoudje.assembly.base.InteractionHandler
import com.ewoudje.assembly.base.Movable
import org.kodein.di.*

class TableObjectMover(
    override val di: DI,
    override val diContext: DIContext<TableObject>
) : DIAware, Movable {
    val pos: TableObjectPosition by instance()
    val size: TableObjectSize by instance()
    val drawable: TableObjectDrawable? by instanceOrNull()
    val handler: InteractionHandler by instance()
    val boundary: TableObjectsBoundary by instance()

    override val x: Float get() = pos.x
    override val y: Float get() = pos.y
    override val width: Float = size.width
    override val height: Float = size.height
    override val zDepth: Int get() = drawable?.zDepth ?: 0

    override fun dragStart(rX: Float, rY: Float) {
        drawable?.zDepth = handler.stackZDepth()
    }

    override fun drag(dX: Float, dY: Float) {
        pos.x = boundary.itemX(pos.x + dX, width)
        pos.y = boundary.itemY(pos.y + dY, height)
    }

    override fun dragStop() {}

    init {
        handler.configureMovable(this)
    }
}