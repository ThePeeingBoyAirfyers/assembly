package com.ewoudje.assembly.visual.desk

import com.ewoudje.assembly.visual.Drawable
import com.ewoudje.assembly.visual.Scene
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bind
import org.kodein.di.inBindSet
import org.kodein.di.instance
import org.kodein.di.singleton

class DeskScene(override val di: DI) : Scene, DIAware {
    val drawables: Set<Drawable> by instance()

    override fun render() {
        for (drawable in drawables) {
            drawable.draw()
        }
    }

    companion object {
        val module = DI.Module("DeskScene") {
            bind<Scene> { singleton { DeskScene(di) }}
            inBindSet<Drawable> {
                add { singleton { Cabinet(di) } }
            }
        }
    }
}