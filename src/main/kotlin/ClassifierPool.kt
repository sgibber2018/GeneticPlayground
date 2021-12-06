/**
 * A blueprint for a "pool" of Classifiers.
 */
abstract class ClassifierPool(
    val poolSize: Int,
) {
    /**
     * The gene pool should be initialized with newGenePool.
     */
    lateinit var pool: List<Classifier>

    /**
     * Initializes a new gene as a list of the appropriate Classifier type.
     */
    abstract fun newClassifierPool(): List<Classifier>

    /**
     * Produces a new generation by measuring the old generation and
     * reproducing based on criteria appropriate for the given Classifier.
     */
    abstract fun nextGeneration(): List<Classifier>

    /**
     * Returns the average score of a generation.
     */
    fun averageScore(): Double {
        return pool.sumOf { it.score } / poolSize
    }

    /**
     * Returns the number of unique "genomes" in the pool. Usually there is some overlap,
     * but not much.
     */
    fun numDistinctClassifiers(): Int {
        return pool
            .map { it.asBinaryString() }
            .toSet()
            .size
    }
}