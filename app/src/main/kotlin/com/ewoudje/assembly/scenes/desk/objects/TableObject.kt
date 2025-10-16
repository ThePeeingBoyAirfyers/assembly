package com.ewoudje.assembly.scenes.desk.objects

import com.ewoudje.assembly.scenes.desk.objects.types.Dossier
import com.ewoudje.assembly.scenes.desk.objects.types.LandlordReport
import com.ewoudje.assembly.util.smartDiContext
import org.kodein.di.*
import org.kodein.di.bindings.ScopeRegistry
import org.kodein.di.bindings.StandardScopeRegistry

class TableObject(override val di: DI, val type: TableObjectType) : DIAware {
    override val diContext: DIContext<TableObject> = smartDiContext(this, type)
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
