class Day8(val input: List<String>) : DayN {

    override fun part1() =
        patternsToOutput.map { (pattern, output) -> output.filter { it.length in uniqueLengths }.count() }.sum()

    override fun part2() = patternsToOutput.map { (patterns, outputs) ->
        val encoding = getEncodingForPatterns(patterns)

        outputs.joinToString("") { decode(it, encoding).toString() }
            .let { Integer.parseInt(it) }
    }.sum()

    private val uniqueLengths = listOf(2, 3, 4, 7)
    private val alphabet = listOf('a', 'b', 'c', 'd', 'e', 'f', 'g')
    private val possibleEncodings: List<Map<Char, Char>> = permutationsOf(alphabet).map { alphabet.zip(it).toMap() }
    private val plainTextToDigit = mapOf(
        "abcefg" to 0,
        "cf" to 1,
        "acdeg" to 2,
        "acdfg" to 3,
        "bcdf" to 4,
        "abdfg" to 5,
        "abdefg" to 6,
        "acf" to 7,
        "abcdefg" to 8,
        "abcdfg" to 9,
    )

    private val patternsToOutput: List<Pair<List<String>, List<String>>> = input.map { line ->
        val p = line.split(" | ").map { part -> part.split(" ") }
        Pair(p[0], p[1])
    }

    private fun decode(output: String, encodedCharToRealChar: Map<Char, Char>): Int? =
        output.toCharArray()
            .map { encodedCharToRealChar[it] }
            .joinToString("")
            .let { plainTextToDigit[it.sorted()] }

    private fun getEncodingForPatterns(patterns: List<String>): Map<Char, Char> {
        return possibleEncodings
            .first { encoding -> patterns.asSequence().map { decode(it, encoding) }.none { it == null } }
    }
}

fun String.sorted() = this.split("").sorted().joinToString("")

fun <T> permutationsOf(l: List<T>): List<List<T>> {
    // base case! only combo
    if (l.size <= 1) return listOf(l)

    // get permutations where each elm is picked first
    return l.flatMap { i -> permutationsOf(l - i).map { it.plus(i) } }
}


