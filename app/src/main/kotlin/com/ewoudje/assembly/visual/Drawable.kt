package com.ewoudje.manypapers.visual

import com.ewoudje.manypapers.state.computer.ComputerScope
import com.ewoudje.manypapers.state.table.TableObjectScope
import com.ewoudje.manypapers.visual.drawables.CaseDrawable
import com.ewoudje.manypapers.visual.drawables.PaperDrawable
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bind
import org.kodein.di.diContext
import org.kodein.di.provider
import org.kodein.di.scoped

interface Drawable: DIAware {
    fun draw()

    companion object {
        val module = DI.Module(name = "Drawables") {
            bind<CaseDrawable> { scoped(ComputerScope).provider { CaseDrawable(di, diContext(context)) } }
            bind<PaperDrawable> { scoped(TableObjectScope).provider { PaperDrawable(di, diContext(context)) } }
        }
    }
}
