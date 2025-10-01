package com.ewoudje.assembly.state.computer

import com.ewoudje.assembly.state.shelves.ShelvePosition
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.bindings.Scope
import org.kodein.di.bindings.ScopeRegistry
import org.kodein.di.bindings.StandardScopeRegistry
import org.kodein.di.provider
import org.kodein.di.scoped
import org.kodein.di.singleton

abstract class Computer {
    val registry: ScopeRegistry = StandardScopeRegistry()

    abstract val position: ShelvePosition
    abstract val case: ComputerCase

    companion object {
        val module = DI.Module(name = "Computer") {
            bind<ComputerStorage> { singleton { ComputerStorage() } }
            bind<ShelvePosition> { scoped(ComputerScope).provider { context.position } }
            bind<ComputerCase> { scoped(ComputerScope).provider { context.case }}
        }
    }
}

object ComputerScope : Scope<Computer> {
    override fun getRegistry(context: Computer): ScopeRegistry = context.registry
}
