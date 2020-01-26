package logic

import gui.FieldElement
import gui.FieldElement.*
import toDeque
import java.io.FileOutputStream
import java.util.*
import java.io.ObjectOutputStream
import java.io.Serializable
import kotlin.collections.HashMap
import kotlin.math.max


class Game(
    val fieldWidth: Int,
    val fieldHeight: Int,
    private val initSnakesCount: Int,
    private val algorithmForLoad: Algorithm?,
    private val maxFoodCount: Int,
    private val energyForOneFood: Int = 15
) : Serializable {
    private var reloadsCount = 0
    var ticksCount = 0
        private set
    private var currentReloadTickCounter = 0
    private val snakes: MutableSet<Snake> = mutableSetOf()
    private var maxGeneration = 0
    private var oldestGenerations = mutableListOf<Pair<Algorithm, Int>>()
    private var mutableField: MutableList<MutableList<FieldElement>>

    val field: List<MutableList<FieldElement>>
        get() = mutableField

    private val fieldPoints = generateSequence { }.take(fieldHeight).mapIndexed { y, _ ->
        generateSequence { }.take(fieldWidth).mapIndexed { x, _ ->
            Point(x, y)
        }.toList()
    }.toList().flatten()

    init {
        mutableField = generateEmptyFieldWithBoarders()
        generateSnakes()
        generateFood()
    }

    private fun generateEmptyFieldWithBoarders(): MutableList<MutableList<FieldElement>> {
        val mutableField = generateSequence {
            generateSequence { EMPTY_CELL }.take(fieldWidth).toMutableList()
        }.take(fieldHeight).toMutableList()

        for (x in 0 until fieldWidth)
            for (y in 0 until fieldHeight)
                if (x == 0 || y == 0 || x == fieldWidth - 1 || y == fieldHeight - 1)
                    mutableField[y][x] = WALL
        return mutableField
    }

    private fun generateSnakes() {

        fun generateSnake(algorithm: Algorithm) {
            val emptyCells = fieldPoints.filter { it.y > 4 || field[it.y][it.x] == EMPTY_CELL }.shuffled()
            for (emptyCell in emptyCells) {
                val snakeCells = generateSequence(0) { it + 1 }
                    .take(4)
                    .map { Point(emptyCell.x, emptyCell.y - it) }
                if (snakeCells.all { mutableField[it.y][it.x] == EMPTY_CELL }) {
                    val snakeTaleDeque = snakeCells.toList().toDeque()
                    mutableField[snakeTaleDeque.first.y][snakeTaleDeque.first.x] = SNAKE_HEAD
                    snakes.add(Snake(snakeTaleDeque, algorithm, energyForOneFood))
                    break
                }
            }
        }

        fun getAlgorithmsForSnakes(): List<Algorithm> {
            val algorithms = mutableListOf<Algorithm>()

            oldestGenerations.map { it.first }.forEach {
                for (i in 0 until Config.OLDERST_GENERATION_RELOAD_WORLD_LOCATE_COUNT)
                    algorithms.add(it)
            }

            if (algorithmForLoad != null) {
                for (i in 0 until initSnakesCount)
                    algorithms.add(algorithmForLoad)
            }

            while (algorithms.size < initSnakesCount)
                algorithms.add(Algorithm.generateRandomAlgorithm())

            return algorithms
        }

        getAlgorithmsForSnakes().forEach { generateSnake(it) }
    }

    private fun generateFood() {
        val foodCount = fieldPoints.map { mutableField[it.y][it.x] }.filter { it == FOOD }.count()
        if (foodCount < maxFoodCount) {
            val needCreateFoodCount = max(maxFoodCount - foodCount - snakes.size * 5, 0)
            val emptyCells = fieldPoints
                .filter { mutableField[it.y][it.x] == EMPTY_CELL }
                .shuffled()
                .take(needCreateFoodCount)
                .toList()
            generateSequence(0) { it + 1 }.take(needCreateFoodCount).forEach { idx ->
                val pointForFood = emptyCells[idx]
                mutableField[pointForFood.y][pointForFood.x] = FOOD
            }
        }
    }

    private fun updateOldestGenerationList(algorithm: Algorithm, generation: Int) {
        oldestGenerations.add(algorithm to generation)
        oldestGenerations.sortBy { it.second }
        oldestGenerations = oldestGenerations.takeLast(Config.OLDEST_GENERATIONS_STORE_COUNT).toMutableList()
    }

    private fun removeSnake(snake: Snake) {
        snakes.remove(snake)
        snake.cells.forEach { mutableField[it.y][it.x] = EMPTY_CELL }
    }

    private fun shareSnake(parentSnake: Snake) {

        fun getChildAlgorithm(parentAlgorithm: Algorithm): Algorithm {
            val randomNum = Random().nextInt(100)
            val childAlgorithm =
                if (randomNum < Config.CHANCE_TO_MUTATE_IN_PERCENT)
                    parentAlgorithm.getMutatedClone()
                else
                    parentAlgorithm
            maxGeneration = Math.max(maxGeneration, childAlgorithm.generation)
            return childAlgorithm
        }

        fun generateChildSnake(snakeCells: List<Point>) {
            val cellsDeque = snakeCells.toDeque()
            val algorithm = getChildAlgorithm(parentSnake.algorithm)
            val snake = Snake(cellsDeque, algorithm, Config.ENERGY_FOR_ONE_FOOD)
            snake.cells.forEach { mutableField[it.y][it.x] = SNAKE_BODY }
            mutableField[snake.headPosition.y][snake.headPosition.x] = SNAKE_HEAD
            snakes.add(snake)
        }

        removeSnake(parentSnake)
        updateOldestGenerationList(parentSnake.algorithm, parentSnake.algorithm.generation)
        val parentSnakeCells = parentSnake.cells.toList()
        val snake1cells = parentSnakeCells.take(8)
        val snake2cells = parentSnakeCells.takeLast(8).reversed()
        generateChildSnake(snake1cells)
        generateChildSnake(snake2cells)
    }

    private fun moveSnake(snake: Snake, direction: Direction) {
        val newHeadPosition = snake.headPosition + direction.toOffset()

        val elementAtNewHeadPosition = field[newHeadPosition.y][newHeadPosition.x]
        snake.cells.forEach { mutableField[it.y][it.x] = EMPTY_CELL }
        when (elementAtNewHeadPosition) {
            WALL, SNAKE_HEAD, SNAKE_BODY -> {
                removeSnake(snake)
                return
            }
            EMPTY_CELL -> snake.move(direction, false)
            FOOD -> {
                snake.move(direction, true)
            }
        }
        snake.cells.forEach { mutableField[it.y][it.x] = SNAKE_BODY }
        mutableField[snake.headPosition.y][snake.headPosition.x] = SNAKE_HEAD
    }

    private fun reloadWorld() {
        mutableField = generateEmptyFieldWithBoarders()
        generateSnakes()
        generateFood()
        reloadsCount++
        currentReloadTickCounter = 0
        oldestGenerations.clear()
    }

    private fun serializeYoungestSnakeAlgorithm() {
        val youngestSnakeAlgorithm = snakes.maxBy { it.algorithm.generation }?.algorithm
        val outputStream = FileOutputStream("algorithms/algorithm${youngestSnakeAlgorithm?.generation}.alg")
        val objectOutputStream = ObjectOutputStream(outputStream)
        objectOutputStream.writeObject(youngestSnakeAlgorithm)
        objectOutputStream.close()
    }

    fun serializeGame() {
        val averageGeneration =snakes.map { it.algorithm.generation }.sum() / snakes.size
        val outputStream = FileOutputStream("games/game$averageGeneration.save")
        val objectOutputStream = ObjectOutputStream(outputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
    }

    fun getDebugData(): HashMap<String, String> {
        val debugData = HashMap<String, String>()
        debugData["1. Snakes count"] = snakes.size.toString()
        debugData["2. Snakes average generations"] =
            (snakes.map { it.algorithm.generation }.sum() / snakes.size).toString()
        debugData["3. Youngest snake in field"] =
            snakes.maxBy { it.algorithm.generation }?.algorithm?.generation.toString()
        debugData["4. Oldest snake in field"] =
            snakes.minBy { it.algorithm.generation }?.algorithm?.generation.toString()
        debugData["5. Count of reloads"] = reloadsCount.toString()
        debugData["6. All ticks count"] = ticksCount.toString()
        debugData["7. Max generation"] = maxGeneration.toString()
        debugData["8. Current reload ticks count"] = currentReloadTickCounter.toString()
        debugData["9. FoodCount"] = fieldPoints.filter { field[it.y][it.x] == FOOD }.size.toString()
        debugData["9. Oldest generations"] = oldestGenerations.map { it.second }.toString()
        return debugData
    }

    fun tick() {
        fun makeOperationsWithSnakes() {
            val foodCells = fieldPoints.filter { mutableField[it.y][it.x] == FOOD }
            val wallCells = fieldPoints.filter { mutableField[it.y][it.x].isImpassable() }
            snakes.toList().forEach { snake -> moveSnake(snake, snake.getMoveDirection(wallCells, foodCells)) }

            snakes.filter { it.energy <= 0 }.forEach { removeSnake(it) }
            snakes.filter { it.cells.size >= 16 }.forEach { shareSnake(it) }
        }

        ticksCount++
        currentReloadTickCounter++

        makeOperationsWithSnakes()
        generateFood()

        if (snakes.size == 0) {
            reloadWorld()
        }

        if (ticksCount % 10000 == 0 && snakes.isNotEmpty()) {
            serializeYoungestSnakeAlgorithm()
        }

        if (ticksCount % 100000 == 0 && snakes.isNotEmpty()) {
            serializeGame()
        }
    }
}