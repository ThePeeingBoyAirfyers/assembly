package com.ewoudje.assembly.base

interface Movable {
    val x: Float
    val y: Float
    val width: Float
    val height: Float
    val zDepth: Int

    // Start coordinates
    fun dragStart(rX: Float, rY: Float)

    // Relative coordinates since move
    fun drag(dX: Float, dY: Float)
    fun dragStop()
}