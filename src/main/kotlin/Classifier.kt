/**
 * The "chromosome" of the Classifier system. It consists of a list of Characteristics and the means to "reproduce".
 * It can be sub-classed for more specific purposes.
 *
 * Currently, it is assuming that whatever it is classifying is going to use a relatively simple system of measuring
 * the average score over a number of trials, as is the case in Iterated Prisoner's Dilemma, and using the system
 * of fitness selection and reproduction described in John Holland's paper. Right now this is unlikely to change,
 * but as the system gets more advanced I may depart from this.
 */
abstract class Classifier(
    val characteristics: List<Characteristic>,
    var score: Double = 0.0,
) {
    var age = 0

    /**
     * Returns a copy of the classifier that is one generation older.
     */
    abstract fun emitSurvivor(): Classifier

    /**
     * Combines two Classifiers, producing two new Classifiers, with a chance for mutation and with the use of
     * "crossover", as described in John Holland's paper.
     */
    fun combine(other: Classifier): List<Classifier> {
        val childACharacteristics = mutableListOf<Characteristic>()
        val childBCharacteristics = mutableListOf<Characteristic>()

        // Pick random index:
        val crossoverIndex = characteristics.indices.random()

        // Swap all characteristics to the left of the index:
        repeat (crossoverIndex) { index ->
            childACharacteristics.add(other.characteristics[index])
            childBCharacteristics.add(characteristics[index])
        }

        // Leave characteristics to the right of the index:
        for (index in crossoverIndex until characteristics.size) {
            childACharacteristics.add(characteristics[index])
            childBCharacteristics.add(other.characteristics[index])
        }

        // Apply a mutation chance to each offspring characteristic:
        repeat (characteristics.size) { index ->
            childACharacteristics[index].applyMutationFrequency(defaultMutationFrequency)
            childBCharacteristics[index].applyMutationFrequency(defaultMutationFrequency)
        }

        return listOf(
            PrisonersDilemmaPlayer(childACharacteristics),
            PrisonersDilemmaPlayer(childBCharacteristics),
        )
    }

    /**
     * Returns a binary string of all the possible characteristics (which should be in a consistent order among
     * all instances of the Classifier -- e.g. alphabetical) represented as 0s for Inactive and 1st for Active, as
     * suggested in John Holland's paper.
     */
    fun asBinaryString(): String {
        var bitString = ""
        characteristics.forEach { bitString += it.asBitString() }
        return bitString
    }

    /**
     * Returns whether or not the named "gene" is active.
     */
    fun hasActiveGene(geneName: String): Boolean {
        return characteristics.any { it.name == geneName && it.active }
    }
}
