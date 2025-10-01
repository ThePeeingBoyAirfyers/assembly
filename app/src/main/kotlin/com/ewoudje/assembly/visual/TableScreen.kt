package com.ewoudje.manypapers.visual

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.ewoudje.manypapers.state.computer.ComputerStorage
import com.ewoudje.manypapers.state.table.TableObjectStorage
import com.ewoudje.manypapers.visual.drawables.CaseDrawable
import com.ewoudje.renderdoc.RenderDoc
import ktx.app.KtxScreen
import ktx.app.clearScreen
import org.kodein.di.*


class TableScreen(override val di: DI) : KtxScreen, DIAware {
    private val collection: AssetCollection by instance()
    private val batch: ModelBatch by instance()
    private val environment: Environment by instance()
    private val camera: Camera by instance()
    private val computers: ComputerStorage by instance()
    private val objects: TableObjectStorage by instance()
    private var width: Int = 0
    private var height: Int = 0
    private val table by lazy { ModelInstance(collection.table, Matrix4().translate(0f, 0.25f, 0.3f)) }

    private val camController by lazy { CameraInputController(camera) }

    init {
        camera.position.y = 1f
        camera.position.z = 0.75f
        camera.near = 0.1f
        camera.far = 10f
        camera.lookAt(0f, 0.5f, 0f)
        camera.update()

        Gdx.input.inputProcessor = camController

        environment.set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
        environment.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))
    }

    override fun resize(width: Int, height: Int) {
        this.width = width
        this.height = height
        camera.viewportWidth = width.toFloat()
        camera.viewportHeight = height.toFloat()
        camera.update()
/*
        val expectedWidth = min(height.toFloat() / ratio, width.toFloat())
        val expectedHeight = expectedWidth * ratio

        widthFix = (width - expectedWidth) / 2f / width
        val widthScale = expectedWidth / width
        val heightScale = expectedHeight / height
        */
    }

    override fun render(delta: Float) {
        camController.update()


        Gdx.gl.glViewport(0, 0, width, height)
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)


        batch.begin(camera)
        batch.render(table, environment)

        for (computer in computers) {
            val scoped = on(computer).direct
            scoped.instance<CaseDrawable>().draw()
        }

        for (obj in objects) {
            val scoped = on(obj).direct
            //scoped.instance<PaperDrawable>().draw()
        }

        batch.end()
    }

    companion object {
        val module = DI.Module("TableScreen") {
            bind<Camera> { singleton { PerspectiveCamera(70f, 0f, 0f) }}
        }
    }
}
