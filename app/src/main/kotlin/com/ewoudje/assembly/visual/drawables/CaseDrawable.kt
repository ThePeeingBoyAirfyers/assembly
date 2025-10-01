package com.ewoudje.manypapers.visual.drawables

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.ewoudje.manypapers.state.computer.Computer
import com.ewoudje.manypapers.state.computer.ComputerCase
import com.ewoudje.manypapers.state.computer.ComputerPosition
import com.ewoudje.manypapers.util.negativeIf
import com.ewoudje.manypapers.visual.AssetCollection
import com.ewoudje.manypapers.visual.Drawable
import org.kodein.di.DI
import org.kodein.di.DIContext
import org.kodein.di.instance
import org.kodein.di.newInstance

class CaseDrawable(override val di: DI, override val diContext: DIContext<Computer>) : Drawable {
    val assets: AssetCollection by instance()
    val case: ComputerCase by instance()
    val position: ComputerPosition by instance()
    val batch: ModelBatch by instance()
    val model by newInstance { assets.cases[case] ?: throw IllegalStateException("Missing case model?") }
    val modelInstance by newInstance { ModelInstance(model, Matrix4()
        .translate((position.x.index * 0.21f - 0.1f).negativeIf(position.isLeft), 0.76f, 0.1f)) }

    override fun draw() {
        batch.render(modelInstance)
    }
}

