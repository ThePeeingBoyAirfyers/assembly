package com.ewoudje.assembly

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.ewoudje.assembly.state.computer.Computer
import com.ewoudje.assembly.state.shelves.ShelvePosition
import com.ewoudje.assembly.state.computer.ComputerStorage
import com.ewoudje.assembly.state.computer.DefaultComputerCase
import com.ewoudje.assembly.state.table.TableObject
import com.ewoudje.assembly.visual.AssetCollection
import com.ewoudje.assembly.visual.Drawable
import com.ewoudje.assembly.visual.GameScreen
import com.ewoudje.assembly.visual.desk.DeskScene
import com.ewoudje.renderdoc.RenderDoc
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.log.logger
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bind
import org.kodein.di.bindConstant
import org.kodein.di.bindInstance
import org.kodein.di.instance
import org.kodein.di.singleton


class Game(val done: () -> Unit = {}): KtxGame<KtxScreen>() {
    val assetStorage = AssetStorage()
    val deferredAssets by lazy { KtxAsync.async { AssetCollection.loadAsync(assetStorage) } }

    val logger = logger<Game>()

    override fun create() {
        logger { "Starting" }
        KtxAsync.initiate()
        KtxAsync.launch {
            val di = buildDI(deferredAssets.await())
            addScreen(GameScreen.forScene(di, DeskScene.module))
            setScreen<GameScreen>()
            done()
        }
    }

    fun buildDI(collection: AssetCollection) =  DI {
        bindConstant(tag = "width") { 384 }
        bindConstant(tag = "height") { 216 }
        bindConstant(tag = "scale") { 4 }

        bind<AssetStorage> { instance(assetStorage) }
        bind<Batch> { singleton { SpriteBatch() } }
        bind<AssetCollection> { instance(collection) }
    }//.apply { TestState(this).init() }

    private var dedup = true
    override fun render() {
        if (Gdx.input.isKeyPressed(Input.Keys.X) && deferredAssets.isCompleted) {
            if (dedup) {
                println("Rebuilding DI Tree")
                val di = buildDI(deferredAssets.getCompleted())
                removeScreen<GameScreen>()
                addScreen(GameScreen.forScene(di, DeskScene.module))
                setScreen<GameScreen>()
                dedup = false
            }
        }  else  {
            dedup = true
        }

        super.render()
    }

    override fun dispose() {
        super.dispose()
    }
}

class TestState(override val di: DI): DIAware {
    val computers: ComputerStorage by instance()

    fun init() {
        computers.addComputer(object : Computer() {
            override val position = ShelvePosition(ShelvePosition.X.LEFT_4, ShelvePosition.Y.BOTTOM)
            override val case = DefaultComputerCase
        })
        computers.addComputer(object : Computer() {
            override val position = ShelvePosition(ShelvePosition.X.LEFT_2, ShelvePosition.Y.MIDDLE)
            override val case = DefaultComputerCase
        })
        computers.addComputer(object : Computer() {
            override val position = ShelvePosition(ShelvePosition.X.LEFT_2, ShelvePosition.Y.BOTTOM)
            override val case = DefaultComputerCase
        })
        computers.addComputer(object : Computer() {
            override val position = ShelvePosition(ShelvePosition.X.LEFT_3, ShelvePosition.Y.BOTTOM)
            override val case = DefaultComputerCase
        })
        computers.addComputer(object : Computer() {
            override val position = ShelvePosition(ShelvePosition.X.LEFT_2, ShelvePosition.Y.TOP)
            override val case = DefaultComputerCase
        })
    }
}
