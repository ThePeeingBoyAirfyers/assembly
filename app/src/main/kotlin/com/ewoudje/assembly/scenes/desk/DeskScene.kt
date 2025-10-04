package com.ewoudje.assembly.scenes.desk

import com.ewoudje.assembly.base.Drawable
import com.ewoudje.assembly.base.Scene
import com.ewoudje.assembly.scenes.desk.objects.types.LandlordReport
import com.ewoudje.assembly.scenes.desk.objects.TableObject
import com.ewoudje.assembly.scenes.desk.objects.TableObjectType
import com.ewoudje.assembly.scenes.desk.objects.TableObjectsBoundary
import org.kodein.di.*

class DeskScene(override val di: DI) : Scene, DIAware {
    val sceneDrawables: Set<Drawable> by instance()
    val tableObjects = mutableSetOf<TableObject>()
    val tableObjectFactory by factory<TableObjectType, TableObject>()

    override fun init() {
        tableObjects.add(tableObjectFactory(LandlordReport))
        tableObjects.add(tableObjectFactory(LandlordReport))
    }

    override fun render() {
        for (drawable in sceneDrawables) {
            drawable.draw()
        }

        tableObjects
            .mapNotNull { it.direct.instanceOrNull<Drawable>() }
            .sortedBy { it.zDepth }
            .forEach { it.draw() }
    }

    companion object {
        val module = DI.Module("DeskScene") {
            bind<Scene> { singleton { DeskScene(di) } }
            bind<TableObjectsBoundary> { singleton { TableObjectsBoundary(
                0f, 0f,
                di.direct.instance<Int>(tag = "width").toFloat(),
                di.direct.instance<Int>(tag = "height").toFloat(),
                cursorOverrunUp = false,
                cursorOverrunDown = true,
                cursorOverrunLeft = false,
                cursorOverrunRight = true
            ) } }
            inBindSet<Drawable> {
                add { singleton { Cabinet(di) } }
            }

            import(TableObject.module)
        }
    }
}