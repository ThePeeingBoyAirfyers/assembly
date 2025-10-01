package com.ewoudje.manypapers.state.computer


data class ComputerPosition(
    val x: X,
    val y: Y
) {
    enum class X {
        LEFT_4, LEFT_3, LEFT_2, LEFT_1, RIGHT_1, RIGHT_2, RIGHT_3, RIGHT_4;

        val isLeft get() = this in listOf(LEFT_1, LEFT_2, LEFT_3, LEFT_4)
        val isRight get() = this in listOf(RIGHT_1, RIGHT_2, RIGHT_3, RIGHT_4)
        val index get() = when(this) {
            LEFT_4 -> 3
            LEFT_3 -> 2
            LEFT_2 -> 1
            LEFT_1 -> 0
            RIGHT_1 -> 0
            RIGHT_2 -> 1
            RIGHT_3 -> 2
            RIGHT_4 -> 3
        }
    }

    enum class Y {
        TOP, MIDDLE, BOTTOM;

        val index get() = when(this) {
            TOP -> 2
            MIDDLE -> 1
            BOTTOM -> 0
        }
    }

    val isLeft get() = x.isLeft
    val isRight get() = x.isRight
}

