package com.ewoudje.manypapers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.ewoudje.manypapers.state.computer.Computer
import com.ewoudje.manypapers.state.computer.ComputerPosition
import com.ewoudje.manypapers.state.computer.ComputerStorage
import com.ewoudje.manypapers.state.computer.DefaultComputerCase
import com.ewoudje.manypapers.state.table.TableObject
import com.ewoudje.manypapers.visual.AssetCollection
import com.ewoudje.manypapers.visual.Drawable
import com.ewoudje.manypapers.visual.TableScreen
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
            addScreen(TableScreen(di))
            setScreen<TableScreen>()
            done()
        }
    }

    fun buildDI(collection: AssetCollection) =  DI {
        import(Computer.module)
        import(TableObject.module)
        import(TableScreen.module)
        import(Drawable.module)

        bind<AssetStorage> { instance(assetStorage) }
        bind<ModelBatch> { singleton { ModelBatch() } }
        bind<Environment> { singleton { Environment() } }
        bind<AssetCollection> { instance(collection) }
    }.apply { TestState(this).init() }

    private var dedup = true
    override fun render() {
        if (Gdx.input.isKeyPressed(Input.Keys.X) && deferredAssets.isCompleted) {
            if (dedup) {
                println("Rebuilding DI Tree")
                val di = buildDI(deferredAssets.getCompleted())
                removeScreen<TableScreen>()
                addScreen(TableScreen(di))
                setScreen<TableScreen>()
                dedup = false
                RenderDoc.triggerCapture()
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.P) && !RenderDoc.isReplayUIConnected()) {
            if (dedup) {
                RenderDoc.launchReplayUI(true)
                dedup = false
            }
        } else  {
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
            override val position = ComputerPosition(ComputerPosition.X.LEFT_4, ComputerPosition.Y.BOTTOM)
            override val case = DefaultComputerCase
        })
        computers.addComputer(object : Computer() {
            override val position = ComputerPosition(ComputerPosition.X.LEFT_2, ComputerPosition.Y.MIDDLE)
            override val case = DefaultComputerCase
        })
        computers.addComputer(object : Computer() {
            override val position = ComputerPosition(ComputerPosition.X.LEFT_2, ComputerPosition.Y.BOTTOM)
            override val case = DefaultComputerCase
        })
        computers.addComputer(object : Computer() {
            override val position = ComputerPosition(ComputerPosition.X.LEFT_3, ComputerPosition.Y.BOTTOM)
            override val case = DefaultComputerCase
        })
        computers.addComputer(object : Computer() {
            override val position = ComputerPosition(ComputerPosition.X.LEFT_2, ComputerPosition.Y.TOP)
            override val case = DefaultComputerCase
        })
    }
}
