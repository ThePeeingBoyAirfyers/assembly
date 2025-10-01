package com.ewoudje.manypapers.state.computer

class ComputerStorage: Iterable<Computer> {
    private val computers = mutableMapOf<ComputerPosition, Computer>()

    fun getComputer(position: ComputerPosition): Computer? = computers[position]
    fun removeComputer(position: ComputerPosition): Computer? = computers.remove(position)
    fun addComputer(computer: Computer) {
        if (computers.containsKey(computer.position)) {
            throw IllegalArgumentException("A computer already exists at position ${computer.position}")
        }

        computers[computer.position] = computer
    }

    override fun iterator(): Iterator<Computer> = computers.values.iterator()
}
