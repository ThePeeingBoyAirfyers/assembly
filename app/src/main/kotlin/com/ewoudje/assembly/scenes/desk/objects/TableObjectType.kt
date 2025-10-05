package com.ewoudje.assembly.scenes.desk.objects

import com.badlogic.gdx.graphics.Texture
import com.ewoudje.assembly.AssetCollection
import com.ewoudje.assembly.base.Drawable
import org.kodein.di.*
import org.kodein.di.bindings.Scope
import org.kodein.di.bindings.ScopeRegistry

/**
 * Extend to define a new type of TableObject.
 * Use [createModule] to define the dependencies of this type.
 * Use [configureDrawable] and [configureSize] to define the basic properties of this type.
 * Use [makeMovable] to make this type movable by the player.
 *
 * A good reference is the [com.ewoudje.assembly.scenes.desk.objects.types.LandlordReport] type.
 */
abstract class TableObjectType : Scope<TableObject> {
    override fun getRegistry(context: TableObject): ScopeRegistry = context.registry
    private val toInit = mutableSetOf<(DI, TableObject) -> Unit>()

    protected val DI.Builder.myScope
        get() =
            scoped(this@TableObjectType)

    protected abstract fun DI.Builder.createModule()
    val module = DI.Module(name = this::class.simpleName ?: "Unknown") {
        val scope = scoped(this@TableObjectType)
        bind<TableObjectPosition> { scope.singleton { TableObjectPosition(0.0f, 0.0f) } }
        createModule()
    }

    protected fun DI.Builder.configureDrawable(asset: (AssetCollection) -> Texture) {
        bind<TableObjectDrawable> { myScope.singleton { TableObjectDrawable(di, context.diContext, asset) } }
        bind<Drawable> { provider { directDI.instance<TableObjectDrawable>() } }
    }


    protected fun DI.Builder.configureSize(width: Float, height: Float) =
        bind<TableObjectSize> {
            myScope.singleton { TableObjectSize(width, height) }
        }

    protected fun DI.Builder.makeMovable() =
        bind<TableObjectMover> {
            toInit.add { di, o -> di.on(o).direct.instance<TableObjectMover>() }
            myScope.singleton { TableObjectMover(di, context.diContext) }
        }

    internal fun init(di: DI, o: TableObject) {
        toInit.forEach { it(di, o) }
    }
}