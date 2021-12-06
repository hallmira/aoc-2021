import java.math.BigInteger

class Day6(val input: List<String>) : DayN {

    override fun part1() = simulateDaysSimple(80, initialFish)
    override fun part2() = simulateDaysForStateByFrequency(256, initialFish)

    private fun simulateDaysSimple(days: Int, fish: List<Fish>) =
        (0 until days)
            .fold(fish.asSequence(), { soFar, _ -> soFar.flatMap { it.nextDay() } })
            .count()

    private fun simulateDaysForStateByFrequency(days: Int, fish: List<Fish>): BigInteger {
        return simulateDaysForStateByFrequencyForKernel(days, fish.countFrequency()).entries
            .map { (k, c) -> c }
            .sumOf { it }
    }

    private fun simulateDaysForStateByFrequencyForKernel(
        days: Int,
        fish: Map<Fish, BigInteger>
    ): Map<Fish, BigInteger> {
        if (days == 0) return fish
        return simulateDaysForStateByFrequencyForKernel(days - 1, fish)
            .flatMap { (k, c) -> k.nextDay().map { Pair(it, c) } }
            .sumFrequencies()
    }

    private val initialFish = input.first().split(",").map { Integer.parseInt(it) }.map { Fish(it) }

    data class Fish(val daysUntilRepro: Int) {
        companion object {
            fun new() = Fish(8)
            fun reset() = Fish(6)
        }

        fun nextDay(): Sequence<Fish> {
            return when (daysUntilRepro) {
                0 -> sequenceOf(new(), reset())
                else -> sequenceOf(Fish(daysUntilRepro - 1))
            }
        }

        override fun toString(): String = daysUntilRepro.toString()
    }
}

fun <T> List<T>.countFrequency(): Map<T, BigInteger> =
    this.groupBy { it }.mapValues { (k, v) -> v.count().toBigInteger() }

private fun <T> List<Pair<T, BigInteger>>.sumFrequencies(): Map<T, BigInteger> =
    this.groupBy { it.first }.mapValues { (k, v) -> v.sumOf { it.second } }


