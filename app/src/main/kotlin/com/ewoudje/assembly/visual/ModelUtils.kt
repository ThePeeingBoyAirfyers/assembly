package com.ewoudje.manypapers.visual

import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3

fun Model.center(): Model {
    nodes.forEach { n -> n.translation.setZero() }
    calculateTransforms()
    return this
}

fun Model.rotateZ(): Model {
    nodes.forEach { n -> n.rotation.mulLeft(Quaternion().set(Vector3.Y, -90f)) }
    calculateTransforms()
    return this
}
