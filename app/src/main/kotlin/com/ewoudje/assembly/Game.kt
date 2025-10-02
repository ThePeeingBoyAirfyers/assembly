package com.ewoudje.assembly

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.ewoudje.assembly.base.GameScreen
import com.ewoudje.assembly.scenes.desk.DeskScene
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.log.logger
import org.kodein.di.*


class Game(val done: () -> Unit = {}) : KtxGame<KtxScreen>() {
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

    override fun <Type : KtxScreen> setScreen(type: Class<Type>) {
        super.setScreen(type)
        if (GameScreen::class.java == type)
            getScreen<GameScreen>().scene.init()
    }

    fun buildDI(collection: AssetCollection) = DI {
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
                logger.info { "Rebuilding DI Tree" }
                val di = buildDI(deferredAssets.getCompleted())
                removeScreen<GameScreen>()
                addScreen(GameScreen.forScene(di, DeskScene.module))
                setScreen<GameScreen>()
                dedup = false
            }
        } else {
            dedup = true
        }

        super.render()
    }

    override fun dispose() {
        super.dispose()
    }
}
