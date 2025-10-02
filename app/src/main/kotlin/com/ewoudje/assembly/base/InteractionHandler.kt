package com.ewoudje.assembly.base

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import java.util.*


class InteractionHandler(override val di: DI) : DIAware {
    private val movables = Collections.newSetFromMap(WeakHashMap<Movable, Boolean?>())

    private val scale: Int by instance(tag = "scale")
    private val height: Int by instance(tag = "height")
    private val x get() = Gdx.input.x / scale
    private val y get() = height - (Gdx.input.y / scale)
    private var leftButtonPushed = false
    private var lastX = 0
    private var lastY = 0

    private var currentMovable: Movable? = null


    fun update(delta: Float) {
        val leftButtonPushing = Gdx.input.isButtonPressed(Input.Buttons.LEFT)
        val startLeftButton = !leftButtonPushed && leftButtonPushing
        val stopLeftButton = leftButtonPushed && !leftButtonPushing

        when {
            startLeftButton -> handleStartLeftButton()
            stopLeftButton -> handleStopLeftButton()
            leftButtonPushing -> handleContinuesLeftButton()
        }

        leftButtonPushed = leftButtonPushing
        lastX = x
        lastY = y
    }

    private fun handleStartLeftButton() {
        for (movable in movables.sortedByDescending { it.zDepth }) {
            if (x > movable.x && y > movable.y) {
                val rX = x - movable.x
                val rY = y - movable.y

                if (rX < movable.width && rY < movable.height) {
                    currentMovable = movable
                    movable.dragStart(rX, rY)
                    break
                }
            }
        }
    }

    private fun handleContinuesLeftButton() {
        currentMovable?.drag((x - lastX).toFloat(), (y - lastY).toFloat())
    }

    private fun handleStopLeftButton() {
        if (currentMovable != null) {
            currentMovable?.dragStop()

            currentMovable = null
        }
    }

    fun configureMovable(movable: Movable) {
        movables.add(movable)
    }

    private var z = 1
    fun stackZDepth() = z++

}