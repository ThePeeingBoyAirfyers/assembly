package com.ewoudje.assembly.state.table


class TableObjectStorage : Iterable<TableObject> {
    val s = System.currentTimeMillis()

    override fun iterator(): Iterator<TableObject> = listOf(
        object : TableObject() {
            override val position: TablePosition
                get() {
                    val r = ((System.currentTimeMillis() - s) / 2000) % 4
                    return when (r) {
                        0L -> TablePosition(-1f, -1f)
                        1L -> TablePosition(1f, -1f)
                        2L -> TablePosition(1f, 1f)
                        3L -> TablePosition(-1f, 1f)
                        else -> TablePosition(0f, 0f)
                    }
                }

        }
    ).iterator()
}
