package com.ewoudje.assembly.scenes.desk.objects.types

import com.ewoudje.assembly.AssetConsumer
import com.ewoudje.assembly.AssetRegistry
import com.ewoudje.assembly.scenes.desk.objects.TableObjectType
import org.kodein.di.DI

object LandlordReport : TableObjectType(), AssetConsumer {
    val texture by AssetRegistry.texture("llreport.png")

    override fun DI.Builder.createModule() {
        configureSize(48f, 64f)
        configureDrawable { texture }

        makeMovable()
    }
}