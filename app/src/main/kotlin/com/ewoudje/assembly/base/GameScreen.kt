package com.ewoudje.assembly.base

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Matrix4
import com.ewoudje.assembly.AssetRegistry
import kotlinx.coroutines.launch
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.graphics.use
import ktx.log.logger
import org.kodein.di.*

class GameScreen(override val di: DI) : KtxScreen, DIAware {
    val logger = logger<GameScreen>()
    val scene: Scene by instance()
    val batch: Batch by instance()
    val interactionHandler: InteractionHandler by instance()
    val width: Int by instance(tag = "width")
    val height: Int by instance(tag = "height")
    val scale: Int by instance(tag = "scale")

    val projection = Matrix4()
        .translate(-1f, -1f, -1f)
        .scale(2 / width.toFloat(), 2 / height.toFloat(), 1f)

    override fun render(delta: Float) {
        Gdx.gl.glViewport(0, 0, scale * width, scale * height)
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)

        interactionHandler.update(delta)

        batch.use(projection) {
            try {
                scene.render()
            } catch (e: Exception) {
                logger.error(e) { "Failed to render scene" }
            }
        }
    }

    companion object {
        fun forScene(di: DI, module: DI.Module): GameScreen =
            GameScreen(DI {
                extend(di)

                bind<InteractionHandler> { singleton { InteractionHandler(di) } }
                bindSet<Drawable>()

                import(module)
            })
    }
}