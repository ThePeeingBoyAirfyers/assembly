package com.ewoudje.assembly.state.computer

import com.ewoudje.assembly.state.shelves.ShelvePosition

class ComputerStorage: Iterable<Computer> {
    private val computers = mutableMapOf<ShelvePosition, Computer>()

    fun getComputer(position: ShelvePosition): Computer? = computers[position]
    fun removeComputer(position: ShelvePosition): Computer? = computers.remove(position)
    fun addComputer(computer: Computer) {
        if (computers.containsKey(computer.position)) {
            throw IllegalArgumentException("A computer already exists at position ${computer.position}")
        }

        computers[computer.position] = computer
    }

    override fun iterator(): Iterator<Computer> = computers.values.iterator()
}
