import machines.FSMMinimizer
import machines.FiniteStateMachine
import machines.Rule
import machines.StateType

fun main(args: Array<String>) {
    test1()
    test2()
    test3()

    println("Конец тестовых сценариев")
}

fun testEquality(source: FiniteStateMachine, min: FiniteStateMachine, tapePostfix: String = "") {
    while (true) {
        println("Введите 'next' чтобы перейти к следующему тесту")
        println("Введите последовательность: ")
        var sequence = readln()

        if (sequence == "next") {
            return
        }

        sequence += tapePostfix

        source.makeSteps(sequence.toCharArray().toList())
        min.makeSteps(sequence.toCharArray().toList())

        val xAnswer = source.currentStateType == StateType.End
        val yAnswer = min.currentStateType == StateType.End

        println("Оканчивается ли данная строка в конечном состоянии")
        println("Исходный автомат: $xAnswer")
        println("Минимальный автомат: $yAnswer")
        println()

        source.reset()
        min.reset()
    }
}

fun test1() {
    val startState = "q0"
    val alphabet = listOf('0', '1')

    val states = listOf("q0", "q1")
    val endStates = listOf("q0", "q1")

    val map = hashMapOf(
        Rule("q0", '0') to "q0",
        Rule("q0", '1') to "q1",
        Rule("q1", '1') to "q1",
        Rule("q1", '0') to "q0",
    )

    val fsm = FiniteStateMachine(states, alphabet, map, startState, endStates)
    val minFsm = FSMMinimizer.minimize(fsm)

    println("Начальное состояние:")
    println(startState)
    println("Конечные состояния:")
    println(endStates)
    println()

    println("Исходный автомат")
    fsm.printToConsole(true)
    println()

    println("Минимальный автомат")
    minFsm.printToConsole(true)
    println()

    testEquality(fsm, minFsm)
}

private fun test2() {
    val startState = "A"
    val alphabet = listOf('a', 'b')

    val states = listOf("A", "B", "C", "D", "E", "F")
    val endStates = listOf("E")

    val map = hashMapOf(
        Rule("A", 'a') to "B",
        Rule("A", 'b') to "C",
        Rule("B", 'a') to "B",
        Rule("B", 'b') to "D",
        Rule("C", 'a') to "B",
        Rule("C", 'b') to "C",
        Rule("D", 'a') to "B",
        Rule("D", 'b') to "E",
        Rule("E", 'a') to "B",
        Rule("E", 'b') to "C",
        Rule("F", 'a') to "E",
    )

    val fsm = FiniteStateMachine(states, alphabet, map, startState, endStates)
    val minFsm = FSMMinimizer.minimize(fsm)

    println("Начальное состояние:")
    println(startState)
    println("Конечные состояния:")
    println(endStates)
    println()

    println("Исходный автомат")
    fsm.printToConsole(true)
    println()

    println("Минимальный автомат")
    minFsm.printToConsole(true)
    println()

    testEquality(fsm, minFsm)
}

private fun test3() {
    val startState = "q0"
    val alphabet = listOf('0', '1')

    val states = listOf("q0", "q1", "q2", "q3", "q4", "q5")
    val endStates = listOf("q0", "q2")

    val map = hashMapOf(
        Rule("q0", '1') to "q1",
        Rule("q1", '0') to "q2",
        Rule("q1", '1') to "q3",
        Rule("q2", '1') to "q4",
        Rule("q2", '0') to "q5",
        Rule("q3", '0') to "q2",
        Rule("q3", '1') to "q3",
        Rule("q4", '0') to "q2",
        Rule("q4", '1') to "q3",
        Rule("q5", '0') to "q5",
        Rule("q5", '1') to "q3",
    )

    val fsm = FiniteStateMachine(states, alphabet, map, startState, endStates)
    val minFsm = FSMMinimizer.minimize(fsm)

    println("Начальное состояние:")
    println(startState)
    println("Конечные состояния:")
    println(endStates)
    println()

    println("Исходный автомат")
    fsm.printToConsole(true)
    println()

    println("Минимальный автомат")
    minFsm.printToConsole(true)
    println()

    testEquality(fsm, minFsm)
}

private fun test4() {
    val startState = "q0"
    val alphabet = listOf('d', '.', 'e', '+', '-', 'N')
    val states = listOf("q0", "q1", "q2", "q3", "q4", "q5", "q6", "q#")
    val endStates = listOf("q#")

    val map = hashMapOf(
        Rule("q0", 'd') to "q1",
        Rule("q1", 'd') to "q1",
        Rule("q1", '.') to "q2",
        Rule("q1", 'e') to "q3",
        Rule("q1", 'N') to "q#",
        Rule("q2", 'd') to "q4",
        Rule("q3", 'd') to "q5",
        Rule("q3", '+') to "q6",
        Rule("q3", '-') to "q6",
        Rule("q4", 'd') to "q4",
        Rule("q4", 'e') to "q3",
        Rule("q4", 'N') to "q#",
        Rule("q5", 'd') to "q5",
        Rule("q5", 'N') to "q#",
        Rule("q6", 'd') to "q5"
    )

    val fsm = FiniteStateMachine(states, alphabet, map, startState, endStates)
    val minFsm = FSMMinimizer.minimize(fsm)

    println("Начальное состояние:")
    println(startState)
    println("Конечные состояния:")
    println(endStates)
    println()

    println("Исходный автомат")
    fsm.printToConsole(true)
    println()

    println("Минимальный автомат")
    minFsm.printToConsole(true)
    println()

    testEquality(fsm, minFsm, "N")
}