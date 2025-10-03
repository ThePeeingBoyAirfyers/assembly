package com.ewoudje.assembly.scenes.desk.objects.types

import com.ewoudje.assembly.scenes.desk.objects.TableObjectType
import org.kodein.di.DI

object LandlordReport : TableObjectType() {
    override fun DI.Builder.createModule() {
        configureSize(48f, 64f)
        configureDrawable { it.landlordReport }

        makeMovable()
    }
}