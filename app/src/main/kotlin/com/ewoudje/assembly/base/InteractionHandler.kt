package com.ewoudje.assembly.base

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import java.util.*


class InteractionHandler(override val di: DI) : DIAware {
    private val draggables = Collections.newSetFromMap(WeakHashMap<Draggable, Boolean?>())

    private val scale: Int by instance(tag = "scale")
    private val height: Int by instance(tag = "height")
    private val x get() = Gdx.input.x / scale
    private val y get() = height - (Gdx.input.y / scale)
    private var leftButtonPushed = false
    private var lastX = 0
    private var lastY = 0

    private var currentDraggable: Draggable? = null


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
        for (draggable in draggables.sortedByDescending { it.zDepth }) {
            if (x > draggable.x && y > draggable.y) {
                val rX = x - draggable.x
                val rY = y - draggable.y

                if (rX < draggable.width && rY < draggable.height) {
                    currentDraggable = draggable
                    draggable.dragStart(rX, rY)
                    break
                }
            }
        }
    }

    private fun handleContinuesLeftButton() {
        currentDraggable?.drag((x - lastX).toFloat(), (y - lastY).toFloat())
    }

    private fun handleStopLeftButton() {
        if (currentDraggable != null) {
            currentDraggable?.dragStop()

            currentDraggable = null
        }
    }

    fun configureMovable(draggable: Draggable) {
        draggables.add(draggable)
    }

    private var z = 1
    fun stackZDepth() = z++

}