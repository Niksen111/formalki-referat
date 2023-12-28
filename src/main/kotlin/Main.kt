import machines.FSMMinimizer
import machines.FiniteStateMachine
import machines.Rule
import machines.StateType

fun main(args: Array<String>) {
    test()
}

fun testEquality(source: FiniteStateMachine, min: FiniteStateMachine, tapePostfix: String? = null) {
    while (true) {
        println("Введите 'exit' чтобы выйти")
        println("Введите последовательность: ")
        var sequence = readln()

        if (sequence == "exit") {
            return
        }

        sequence += tapePostfix

        source.makeSteps(sequence.toCharArray().toList())
        min.makeSteps(sequence.toCharArray().toList())

        val xAnswer = source.currentStateType == StateType.End
        val yAnswer = min.currentStateType == StateType.End

        println("Исходный автомат: $xAnswer")
        println("Минимальный автомат: $yAnswer")
        println()

        source.reset()
        min.reset()
    }
}

private fun test() {
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

    println("Исходный автомат")
    fsm.printToConsole(true)
    println()

    println("Минимальный автомат")
    minFsm.printToConsole(false)
    println()

    testEquality(fsm, minFsm, "N")
}