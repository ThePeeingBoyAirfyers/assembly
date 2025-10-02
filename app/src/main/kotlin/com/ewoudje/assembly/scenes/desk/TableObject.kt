package com.ewoudje.assembly.scenes.desk

import com.ewoudje.assembly.scenes.desk.objects.LandlordReport
import org.kodein.di.*
import org.kodein.di.bindings.ScopeRegistry
import org.kodein.di.bindings.StandardScopeRegistry

class TableObject(override val di: DI, val type: TableObjectType) : DIAware {
    override val diContext: DIContext<TableObject> = diContext(this)
    val registry: ScopeRegistry = StandardScopeRegistry()

    init {
        type.init(di, this)
    }

    companion object {
        val module = DI.Module(name = "TableObject") {
            bind<TableObject> { factory { type: TableObjectType -> TableObject(di, type) } }
            import(LandlordReport.module)
        }
    }
}
