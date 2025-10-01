package com.ewoudje.manypapers.state.table

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.bindings.Scope
import org.kodein.di.bindings.ScopeRegistry
import org.kodein.di.bindings.StandardScopeRegistry
import org.kodein.di.provider
import org.kodein.di.scoped
import org.kodein.di.singleton

abstract class TableObject {
    val registry: ScopeRegistry = StandardScopeRegistry()

    abstract val position: TablePosition

    companion object {
        val module = DI.Module(name = "TableObject") {
            bind<TableObjectStorage> { singleton { TableObjectStorage() } }
            bind<TablePosition> { scoped(TableObjectScope).provider { context.position } }
        }
    }
}

object TableObjectScope : Scope<TableObject> {
    override fun getRegistry(context: TableObject): ScopeRegistry = context.registry
}
