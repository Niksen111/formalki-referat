package machines

class FSMMinimizer {
    companion object {
        fun minimize(fsm: FiniteStateMachine): FiniteStateMachine {
            val achievableStates = findAchievableStates(fsm)
            val classes = extractKEquivalenceClasses(achievableStates, 0, fsm)

            val states = classes.map { it.joinToString(" ") }
            val endStates = classes.filter { t -> t.any { fsm.endStates.contains(it) } }
                .map { it.joinToString(" ") }

            val startStateClass = classes.first { it.contains(fsm.startState) }
            val startState = startStateClass.joinToString(" ")

            val map = HashMap<Rule, String>()

            classes.forEach { currClass ->
                val classRepresentative = currClass[0]
                val state = currClass.joinToString(" ")

                fsm.alphabet.forEach { c ->
                    val rule = Rule(classRepresentative, c)

                    fsm.getMap()[rule]?.let {
                        val valueClass = findStateClass(it, classes).joinToString(" ")
                        map[Rule(state, c)] = valueClass
                    }
                }
            }

            return FiniteStateMachine(states, fsm.alphabet, map, startState, endStates)
        }

        private fun extractKEquivalenceClasses(
            states: List<String>, k: Int, fsm: FiniteStateMachine
        ): List<List<String>> {
            val classes = divideIntoKEquivalenceClasses(states, k, fsm)
            val newK = k + 1

            if (classes.size == 1) {
                return classes
            }

            val subclasses = mutableListOf<MutableList<String>>()

            classes.forEach { c ->
                val currClasses = extractKEquivalenceClasses(c.toList(), newK, fsm)
                subclasses.addAll(currClasses.map { it.toMutableList() })
            }

            return subclasses
        }

        private fun divideIntoKEquivalenceClasses(
            states: List<String>,
            k: Int,
            fsm: FiniteStateMachine
        ): List<List<String>> {
            val equivalenceClasses = mutableListOf<MutableList<String>>()

            for (i in states.indices) {
                if (equivalenceClasses.any { it.contains(states[i]) }) {
                    continue
                }

                equivalenceClasses.add(mutableListOf(states[i]))

                for (j in i+1..<states.size) {
                    if (isStatesKEqual(states[i], states[j], k, fsm)) {
                        equivalenceClasses.last().add(states[j])
                    }
                }
            }

            return equivalenceClasses
        }

        private fun isStates0Equal(firstState: String?, secondState: String?, fsm: FiniteStateMachine): Boolean {
            if (fsm.endStates.contains(firstState) && fsm.endStates.contains(secondState)) {
                return true
            }

            val notEndStates = fsm.states.minus(fsm.endStates.toSet())

            return notEndStates.contains(firstState) && notEndStates.contains(secondState)
        }

        private fun isStatesKEqual(firstState: String?, secondState: String?, k: Int, fsm: FiniteStateMachine): Boolean {
            if (k == 0) {
                return isStates0Equal(firstState, secondState, fsm)
            }

            fsm.alphabet.forEach { c ->
                val firstValue = fsm.getMap()[Rule(firstState!!, c)]
                val secondValue = fsm.getMap()[Rule(secondState!!, c)]

                if (firstValue != secondValue && !isStatesKEqual(firstValue, secondValue, k - 1, fsm)) {
                    return false
                }
            }

            return isStatesKEqual(firstState, secondState, k - 1, fsm)
        }

        private fun findAchievableStates(fsm: FiniteStateMachine): List<String> {
            val achievableStates = mutableListOf(fsm.startState)
            var newStates = listOf(fsm.startState)

            while (true) {
                val states = mutableListOf<String>()

                newStates.forEach {  state ->
                    val currStates = fsm.getMap().filter { it.key.state == state }
                        .map { it.value }.minus(achievableStates.toSet())
                    states.addAll(currStates)
                }

                if (states.isEmpty()) {
                    achievableStates.sort()
                    return achievableStates.distinct()
                }

                achievableStates.addAll(states)
                newStates = states
            }
        }

        private fun findStateClass(state: String, classes: List<List<String>>): List<String> {
            return classes.first { it.contains(state) }
        }
    }
}