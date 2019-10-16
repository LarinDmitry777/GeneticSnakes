package logic

import gui.FieldElement
import gui.FieldElement.*
import toDeque
import java.util.*

class Game(
    private val fieldWidth: Int,
    private val fieldHeight: Int,
    private val initSnakesCount: Int,
    private val algorithm: Algorithm,
    private val maxFoodCount: Int,
    private val maxSnakeSize: Int = 16,
    private val energyForOneFood: Int = 15
) {
    var reloadCouter = 0
    var tickCount = 0
    var currentTicksCount = 0
    val snakes: MutableSet<Snake> = mutableSetOf()
    var foodCount = 0
    var maxGeneration = 0
    var algorithmsLifes = mutableListOf<Pair<Algorithm, Int>>()

    var field = generateEmptyField()
    val fieldPoints = generateSequence { }.take(fieldHeight).mapIndexed { y, _ ->
        generateSequence { }.take(fieldWidth).mapIndexed { x, _ ->
            Point(x, y)
        }.toList()
    }.toList().flatten()

    init {

        generateSnakes()
        generateFood()

    }

    fun generateEmptyField(): MutableList<MutableList<FieldElement>> {
        val field = generateSequence {
            generateSequence { EMPTY_CELL }.take(fieldWidth).toMutableList()
        }.take(fieldHeight).toMutableList()

        for (x in 0 until fieldWidth)
            for (y in 0 until fieldHeight)
                if (x == 0 || y == 0 || x == fieldWidth - 1 || y == fieldHeight - 1)
                    field[y][x] = WALL
        return field
    }

    fun generateSnakes() {
        val algorithms = ArrayDeque<Algorithm>()
        algorithmsLifes.map { it.first }.forEach {
            algorithms.add(it)
            algorithms.add(it)
            println(it)
        }
        while (algorithms.size < initSnakesCount)
            algorithms.add(Algorithm.generateRandomAlgorithm())

        fun generateSnake() {
            val emptyCells = fieldPoints.filter { it.y > 4 }.shuffled()
            for (emptyCell in emptyCells) {
                val snakeTale = generateSequence(0) { it + 1 }
                    .take(4)
                    .map { Point(emptyCell.x, emptyCell.y - it) }
                if (snakeTale.any { field[it.y][it.x] == EMPTY_CELL }) {
                    val snakeTaleDeque = ArrayDeque<Point>()
                    snakeTale.forEach {
                        snakeTaleDeque.add(it)
                        field[it.y][it.x] = SNAKE_BODY
                    }
                    field[snakeTaleDeque.first.y][snakeTaleDeque.first.x] = SNAKE_HEAD
                    snakes.add(Snake(snakeTaleDeque, algorithms.pop(), energyForOneFood))
                    break
                }
            }
        }
        for (i in 0 until initSnakesCount)
            generateSnake()
    }

    private fun generateFood() {
        if (foodCount < maxFoodCount)
            fieldPoints
                .filter { field[it.y][it.x] == EMPTY_CELL }
                .shuffled()
                .take(maxFoodCount - foodCount)
                .forEach {
                    field[it.y][it.x] = FOOD
                    foodCount++
                }
    }

    fun updateAlgorithmsLifes(algorithm: Algorithm, lifeCount: Int) {
        algorithmsLifes.add(algorithm to lifeCount)
        algorithmsLifes.sortBy { it.second }
        algorithmsLifes = algorithmsLifes.take(10).toMutableList()
    }

    fun tick() {
        fun removeSnake(snake: Snake) {
            snakes.remove(snake)
            snake.cells.forEach { field[it.y][it.x] = EMPTY_CELL }

        }

        fun shareSnake(snake: Snake) {
            removeSnake(snake)
            val snake1cells = snake.cells.take(8)
            val snake2cells = snake.cells.reversed().take(8)
            val algorithm = snake.algorithm
            updateAlgorithmsLifes(snake.algorithm, snake.lifeTicksCount)
            val snake1Algorithm = if (Random().nextInt(4) == 0) Algorithm.mutate(algorithm) else algorithm
            val snake2Algorithm = if (Random().nextInt(4) == 0) Algorithm.mutate(algorithm) else algorithm
//            val snake1Algorithm = algorithm
//            val snake2Algorithm = Algorithm.mutate(algorithm)
            val snake1 = Snake(snake1cells.toDeque(), snake1Algorithm, energyForOneFood)
            val snake2 = Snake(snake2cells.toDeque(), snake2Algorithm, energyForOneFood)
            snake1.cells.forEach { field[it.y][it.x] == SNAKE_BODY }
            snake2.cells.forEach { field[it.y][it.x] == SNAKE_BODY }
            snakes.add(snake1)
            snakes.add(snake2)
        }

        fun moveSnake(snake: Snake, direction: Direction) {
            val head = snake.getHeadPosition()
            val offset = direction.toOffset()
            val newHeadPosition = head + offset
            if (newHeadPosition.x !in 0 until fieldWidth || newHeadPosition.y !in 0 until fieldHeight) {
                removeSnake(snake)
                return
            }
            val elementAtNewHeadPosition = field[newHeadPosition.y][newHeadPosition.x]
            snake.cells.forEach { field[it.y][it.x] = EMPTY_CELL }
            when (elementAtNewHeadPosition) {
                WALL, SNAKE_HEAD, SNAKE_BODY -> {
                    removeSnake(snake)
                    return
                }
                EMPTY_CELL -> snake.move(direction, false)
                FOOD -> {
                    snake.move(direction, true)
                    foodCount--
                }
            }
            snake.cells.forEach { field[it.y][it.x] = SNAKE_BODY }
            field[snake.getHeadPosition().y][snake.getHeadPosition().x] = SNAKE_HEAD

        }


        val foodCells = fieldPoints.filter { field[it.y][it.x] == FOOD }
        val wallCells =
            fieldPoints.filter { field[it.y][it.x] == WALL || field[it.y][it.x] == SNAKE_HEAD || field[it.y][it.x] == SNAKE_BODY }
        snakes.map { it to it.getMoveDirection(wallCells, foodCells) }
            .forEach { moveSnake(it.first, it.second) }

        //ToDo Можно ли удалять прям тут
        val snakesWithoutEnergy = snakes.filter { it.energy <= 0 }
        snakesWithoutEnergy.forEach { removeSnake(it) }
        snakes.filter { it.cells.size >= 16 }.forEach { shareSnake(it) }
        generateFood()


        if (snakes.size == 0) {
            field = generateEmptyField()
            generateSnakes()
            reloadCouter++
            currentTicksCount = 0
            foodCount = 0
            algorithmsLifes.clear()
        }
        ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor()
        println("Snakes count: ${snakes.size}")
        println("Snakes generations: ${snakes.map { it.algorithm.generation }.sorted()}")
        println("Count of reloads: $reloadCouter")
        println("Ticks: ${tickCount++}")
        println("Current reload ticks: ${currentTicksCount++}")
        maxGeneration = Math.max(maxGeneration, snakes.map { it.algorithm.generation }.max()!!)
        println("Max generation: $maxGeneration")
        println("Algorithms: ${algorithmsLifes.map { it.second }}")
    }
}