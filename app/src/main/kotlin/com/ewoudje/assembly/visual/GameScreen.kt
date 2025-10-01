package com.ewoudje.assembly.visual

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Matrix4
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.graphics.use
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bindSet
import org.kodein.di.instance

class GameScreen(override val di: DI) : KtxScreen, DIAware {
    val scene: Scene by instance()
    val batch: Batch by instance()
    val width: Int by instance(tag = "width")
    val height: Int by instance(tag = "height")
    val scale: Int by instance(tag = "scale")

    val projection = Matrix4()
        .translate(-1f, -1f, -1f)
        .scale(2 / width.toFloat(), 2 / height.toFloat(), 1f)

    override fun render(delta: Float) {
        Gdx.gl.glViewport(0, 0, scale * width, scale * height)
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)

        batch.use(projection) {
            scene.render()
        }
    }

    companion object {
        fun forScene(di: DI, module: DI.Module): GameScreen =
            GameScreen(DI {
                extend(di)

                bindSet<Drawable>()

                import(module)
            })
    }
}