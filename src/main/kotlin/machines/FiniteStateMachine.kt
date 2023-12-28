package machines

import dnl.utils.text.table.TextTable

class FiniteStateMachine(
    val states: List<String>,
    val alphabet: List<Char>,
    map: Map<Rule, String>,
    val startState: String,
    val endStates: List<String>
) {
    init {
        require(states.isNotEmpty()) { "The states should not be empty." }
        require(states.size == states.distinct().size) { "The states should not contain duplicate elements." }
        require(map.keys.all { states.contains(it.state) }
                && map.keys.all { alphabet.contains(it.symbol) }
                && map.values.all { states.contains(it) }) { "The map has an incorrect definition scope." }
        require(states.contains(startState)) { "Start state is not an element of states." }
        require(endStates.all { states.contains(it) }) { "The end states are not a subset of states." }
    }

    private val map: HashMap<Rule, String> = HashMap(map)
    fun getMap(): Map<Rule, String> = map

    var currentState: String = startState
        private set
    var currentStateType: StateType = recognizeCurrentStateType()

    var isStarted: Boolean = false
        private set
    var isFinished: Boolean = false
        private set

    fun makeStep(symbol: Char) {
        if (isFinished) {
            return
        }

        if (!isStarted) {
            isStarted = true
        }

        val rule = Rule(currentState, symbol)

        currentState = map[rule].orEmpty()
        if (currentState.isEmpty()) {
            isFinished = true
        }

        currentStateType = recognizeCurrentStateType()
    }

    fun makeSteps(symbols: Iterable<Char>) {
        require(!isStarted) { "Machine has already started reading other characters." }
        symbols.forEach { makeStep(it) }
    }

    fun reset() {
        currentState = startState
        currentStateType = recognizeCurrentStateType()
        isStarted = false
        isFinished = false
    }

    fun printToConsole(moveFirstSymbolToEnd: Boolean = false) {
        val sortedMap = this.getMap().toSortedMap(compareBy<Rule> { it.state }.thenBy { it.symbol })
        val states = sortedMap.keys.map { it.state }.distinct()
        var symbols = sortedMap.keys.map { it.symbol }.distinct().toMutableList()

        if (moveFirstSymbolToEnd) {
            symbols.addLast(symbols.first())
            symbols = symbols.drop(1).toMutableList()
        }

        val symbolsStr = symbols.map { "$it " }.toMutableList()
        symbolsStr.addFirst(" ")
        val table = mutableListOf<List<String>>()

        states.forEach { state ->
            val row = mutableListOf(state)

            symbols.forEach { symbol ->
                val rule = Rule(state, symbol)
                val value = map[rule].orEmpty()

                row.add(value)
            }

            table.add(row)
        }

        val tt = TextTable(symbolsStr.toTypedArray(), table.map { it.toTypedArray() }.toTypedArray())
        tt.printTable()
    }

    private fun recognizeCurrentStateType(): StateType {
        return if (endStates.contains(currentState))
            StateType.End
        else if (startState == currentState)
            StateType.Start
        else if (states.contains(currentState))
            StateType.Ordinary
        else
            StateType.NotSpecified
    }
}