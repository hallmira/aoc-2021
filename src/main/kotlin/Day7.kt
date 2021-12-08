import java.lang.Math.abs

class Day7(val input: List<String>) : DayN {
    private val initialCrabPositions = input.first().split(",").map { Integer.parseInt(it) }

    override fun part1() = minFuelForAlignmentWithConstantRate(initialCrabPositions)
    override fun part2() = minFuelForAlignmentWithLinearRate(initialCrabPositions)

    fun minFuelForAlignmentWithConstantRate(initialCrabs: List<Int>): Int =
        findMinFuelForRate(initialCrabs) { pos, it -> abs(pos - it) }

    fun minFuelForAlignmentWithLinearRate(initialCrabs: List<Int>): Int {
        return findMinFuelForRate(initialCrabs, this::fuelBetweenWithLinearRate)
    }
    private fun findMinFuelForRate(initialCrabs: List<Int>, positionsToFuel: (Int, Int) -> Int) =
        (initialCrabs.min()!!..initialCrabs.max()!!)
            .minOfOrNull { pos -> initialCrabs.map { positionsToFuel(pos, it) }.sum() }!!

    private fun fuelBetweenWithLinearRate(pos1: Int, pos2: Int) : Int {
        val stepsTaken = abs(pos1 - pos2)
        return stepsTaken * (1 + stepsTaken)/2
    }
}


