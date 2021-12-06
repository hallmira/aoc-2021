class Day5(val input: List<String>) : DayN {

    override fun part1() = countOverlappingPoints(ignoreDiagonal = true)
    override fun part2() = countOverlappingPoints(ignoreDiagonal = false)

    private fun countOverlappingPoints(ignoreDiagonal: Boolean): Int {
        val countsForCoord = lineEdges
            .map { it.getPath(ignoreDiagonal) }
            .filterNotNull()
            .flatten()
            .groupBy { it }.entries
            .map { (v, copies) -> Pair(v, copies.count()) }

        paint(countsForCoord)
        return countsForCoord
            .filter { (v, c) -> c >= 2 }
            .map { (v, _) -> v }
            .count()
    }

    private fun paint(countsForCoord: List<Pair<Coordinate, Int>>) {
        val coordMap: Map<Coordinate, Int> = countsForCoord.toMap()
        val maxX = countsForCoord.map { (v, c) -> v.x }.maxByOrNull { it }!!
        val maxY = countsForCoord.map { (v, c) -> v.y }.maxByOrNull { it }!!

        IntRange(0, maxY).forEach { y ->
            IntRange(0, maxX).map { x ->
                coordMap.getOrDefault(Coordinate(x, y), null) ?: "."
            }.joinToString("").let { println(it) }
        }
    }

    private val lineEdges: List<Line> = input.map { line ->
        line.split(" -> ")
            .filter { it.isNotBlank() }
            .map { g -> g.split(",").map { Integer.parseInt(it) } }
            .map { g -> Coordinate(g[0], g[1]) }
            .let { Line(it[0], it[1]) }
    }

    data class Line(val start: Coordinate, val end: Coordinate) {
        fun getPath(ignoreDiagonal: Boolean): List<Coordinate>? {
            return when {
                start.x == end.x -> rangeBetween(start.y, end.y).map { Coordinate(start.x, it) }
                start.y == end.y -> rangeBetween(start.x, end.x).map { Coordinate(it, start.y) }
                else -> if (ignoreDiagonal) null else {
                    // assume 45deg diagonal
                    rangeBetween(start.x, end.x)
                        .zip(rangeBetween(start.y, end.y))
                        .map { (x, y) -> Coordinate(x, y) }
                }
            }
        }

        private fun rangeBetween(a: Int, b: Int) = (if (b > a) (a.rangeTo(b)) else (b.rangeTo(a)).toList().reversed())
    }

    data class Coordinate(val x: Int, val y: Int)
}