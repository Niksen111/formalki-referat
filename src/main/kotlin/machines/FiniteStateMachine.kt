package machines

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
    var currentStateType: StateType = getCurrentStateType()

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

        currentStateType = getCurrentStateType()
    }

    fun makeSteps(symbols: Iterable<Char>) {
        require(!isStarted) { "Machine has already started reading other characters." }
        symbols.forEach { makeStep(it) }
    }

    fun reset() {
        currentState = startState
        currentStateType = getCurrentStateType()
        isStarted = false
        isFinished = false
    }

    fun printToConsole(moveFirstSymbolToEnd: Boolean = false) {
        val states = map.keys.map { it.state }.distinct()
        val symbols = map.keys.map { it.symbol }.distinct().toMutableList()

        if (moveFirstSymbolToEnd) {
            symbols.addLast(symbols.first())
            symbols.drop(1)
        }

        val symbolsStr = symbols.map { it.toString() }
        val firstRow = mutableListOf("").addAll(symbolsStr)
        // TODO доделать вывод
    }

    private fun getCurrentStateType(): StateType {
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