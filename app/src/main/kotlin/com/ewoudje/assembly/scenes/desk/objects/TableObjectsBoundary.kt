package com.ewoudje.assembly.scenes.desk.objects

class TableObjectsBoundary(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val cursorOverrunUp: Boolean,
    val cursorOverrunDown: Boolean,
    val cursorOverrunLeft: Boolean,
    val cursorOverrunRight: Boolean,
    val offset: Float = 5f,
) {
    val rightX = x + width
    val topY = y + height

    fun itemX(itemX: Float, itemWidth: Float) = itemX(x, rightX, cursorOverrunLeft, cursorOverrunRight, itemX, itemWidth)
    fun itemY(itemY: Float, itemHeight: Float) = itemX(x, topY, cursorOverrunDown, cursorOverrunUp, itemY, itemHeight)

    companion object {
        private fun itemX(x: Float, rightX: Float, min: Boolean, max: Boolean, itemX: Float, itemWidth: Float): Float {
            if (itemX > x && itemX + itemWidth < rightX) return itemX

            var testWidth = itemWidth
            var leftX = 0f
            if (max) testWidth = 5f
            if (min) leftX = itemWidth - 5f

            if (itemX + testWidth > rightX) return rightX - testWidth
            if (itemX < x - leftX) return x - leftX

            return itemX
        }
    }
}