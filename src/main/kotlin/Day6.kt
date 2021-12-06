import java.math.BigInteger

class Day6(val input: List<String>) : DayN {

    override fun part1() = simulateDaysSimple(80, initialFish)
    override fun part2() = simulateDaysSimplyByFrequency(256, initialFish)

    private fun simulateDaysSimple(days: Int, fish: List<Fish>) =
        (0 until days)
            .fold(fish.asSequence(), { soFar, _ -> soFar.flatMap { it.nextDay() } })
            .count()

    private fun simulateDaysSimplyByFrequency(days: Int, fish: List<Fish>) =
        (0 until days)
            .fold(
                fish.countFrequency(),
                { soFar, _ ->
                    // move each fish state forward a day, tracking how many fish are in each state
                    soFar.flatMap { (fishState, count) ->
                        fishState.nextDay().map { nextDayState -> Pair(nextDayState, count) }
                    }.sumFrequencies()
                }
            )
            .entries.sumOf { (fishState, count) -> count }

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

fun <T> List<T>.countFrequency(): Map<T, Long> =
    this.groupBy { it }.mapValues { (k, v) -> v.count().toLong() }

private fun <T> List<Pair<T, Long>>.sumFrequencies(): Map<T, Long> =
    this.groupBy { it.first }.mapValues { (k, v) -> v.sumOf { it.second } }


