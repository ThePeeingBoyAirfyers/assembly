package com.ewoudje.assembly.util

import org.kodein.di.DI
import org.kodein.di.DIContext
import org.kodein.di.bindings.Scope
import org.kodein.type.TypeToken
import org.kodein.type.generic

inline fun <reified C : Any> DI.Builder.smartScope(
    scope: Scope<C>
): DI.BindBuilder.WithScope<C> = DI.BindBuilder.ImplWithScope(SmartToken(generic(), scope), scope)

inline fun <reified C : Any> smartDiContext(context: C, scope: Scope<C>): DIContext<C> =
    DIContext(SmartToken(generic(), scope), context)

class SmartToken<C>(val c: TypeToken<C>, val scope: Scope<C>) : TypeToken<C> by c {
    override fun isAssignableFrom(typeToken: TypeToken<*>): Boolean {
        if (!c.isAssignableFrom(typeToken)) return false
        if (typeToken !is SmartToken<*>) return true
        return scope == typeToken.scope
    }

    override fun equals(other: Any?): Boolean =
        super.equals(other) && (other is SmartToken<*>) && scope == other.scope

    override fun hashCode(): Int {
        return scope.hashCode() * 31 + super.hashCode()
    }
}