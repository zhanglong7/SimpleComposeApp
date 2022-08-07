package com.zhl.simplecalulator.algorithm

import com.zhl.simplecalulator.CalculatorButton
import kotlin.math.round

const val CHAR_PLUS = '+'
const val CHAR_MINUS = '-'
const val CHAR_MULTIPLY = '*'
const val CHAR_DIVIDE = '/'
const val CHAR_POINT = '.'

object Calculation {

    private var state = State.INIT
//    private val numbersStack = Stack<Double>()
//    private val operatorStack = Stack<Int>()
    var calculationString = ""
        private set

    fun input(code: Int) {
        when (code) {
            in 0..9 -> {
                inputNumber(code)
            }
            in CalculatorButton.CODE_PLUS..CalculatorButton.CODE_DIVIDE -> {
                inputOperator(code)
            }
            CalculatorButton.CODE_POINT -> {
                inputPoint()
            }
            CalculatorButton.CODE_DELETE -> {
                inputDelete()
            }
            CalculatorButton.CODE_RESET -> {
                clear()
            }
        }
    }

    private fun inputDelete() {
        if (state != State.INIT) {
            calculationString.lastOrNull()?.let {
                calculationString = calculationString.substring(0, calculationString.length - 1)
                if (calculationString.isEmpty()) {
                    state = State.INIT
                } else {
                    val last = calculationString.last()
                    if (last == CHAR_POINT) {
                        state = State.POINT
                    } else if (last in charArrayOf(CHAR_PLUS, CHAR_MINUS, CHAR_MULTIPLY, CHAR_DIVIDE)) {
                        state = State.OPERATOR
                    } else { //最后是数字
                        state = State.NUMBER
                        val lastIndexOfPoint = calculationString.lastIndexOf(CHAR_POINT)
                        val lastIndexOfOp = calculationString.lastIndexOfAny(charArrayOf(CHAR_PLUS, CHAR_MINUS, CHAR_MULTIPLY, CHAR_DIVIDE))
                        if (lastIndexOfPoint > lastIndexOfOp) {
                            state = State.POINT_NUMBER
                        }
                    }

                }
            }
        }
    }

    private fun inputPoint() {
        if (state == State.INIT || state == State.OPERATOR) {
            inputNumber(0) //小数点前面自动加0
        }
        if (state == State.NUMBER) {
            state = State.POINT
            calculationString += CHAR_POINT
        }
    }

    private fun inputOperator(code: Int) {
        if (state == State.NUMBER || state == State.POINT_NUMBER) {
            calculationString += CalculatorButton.code2String(code)
            state = State.OPERATOR
        }
    }

    private fun inputNumber(code: Int) {
        state = if (state == State.POINT || state == State.POINT_NUMBER) {
            State.POINT_NUMBER
        } else {
            State.NUMBER
        }
        calculationString += CalculatorButton.code2String(code)
    }

    fun calculate(): Double {
        if (calculationString.isEmpty()) {
            return 0.0
        }
        //先去掉最后的非数字部分
        val lastChar = calculationString.last()
        val trimString = if (lastChar.isDigit()) {
            calculationString
        } else {
            calculationString.substring(0, calculationString.length - 1)
        }
        val res = calculate(trimString) * 1e10 //消除浮点计算误差
        return round(res) / 1e10
    }

    private fun calculate(calculationString: String): Double {
        if (calculationString.isEmpty()) return 0.0
        val numbers = calculationString.split(CHAR_PLUS, CHAR_MINUS, CHAR_MULTIPLY, CHAR_DIVIDE)
        if (numbers.size < 2) {
            return numbers[0].toDouble()
        } else if (numbers.size == 2) {
            return calculate(numbers[0].toDouble(), calculationString[numbers[0].length], numbers[1].toDouble())
        }

        var indexOp = numbers[0].length
        var indexNumber = 0
        var firstNum = numbers[0].toDouble()

        //先乘除后回加减
        val newCalculationBuilder = StringBuilder()
        while (indexOp < calculationString.length) {
            when (val op = calculationString[indexOp]) {
                CHAR_PLUS, CHAR_MINUS -> {
                    newCalculationBuilder.append("${firstNum}$op")
                    indexNumber++
                    indexOp++
                    firstNum = numbers[indexNumber].toDouble()
                    indexOp += numbers[indexNumber].length
                }
                CHAR_MULTIPLY, CHAR_DIVIDE -> {
                    indexNumber++
                    val nextNum = numbers[indexNumber].toDouble()
                    indexOp += numbers[indexNumber].length + 1
                    firstNum = calculate(firstNum, op, nextNum)
                }
                else -> {
                    throw IllegalArgumentException()
                }
            }
        }
        newCalculationBuilder.append(firstNum)
        return calculatePlusAndMinus(newCalculationBuilder.toString())
    }

    /**
     * 计算只包含加减的算式
     */
    private fun calculatePlusAndMinus(calculationStr: String): Double {
        val numbers = calculationStr.split(CHAR_PLUS, CHAR_MINUS)
        if (numbers.size < 2) {
            return numbers[0].toDouble()
        }
        var indexOp = numbers[0].length
        var indexNumber = 0
        var result = numbers[0].toDouble()
        while (indexOp < calculationStr.length) {
            val op = calculationStr[indexOp]
            indexNumber++
            val nextNumber = numbers[indexNumber].toDouble()
            result = calculate(result, op, nextNumber)
            indexOp += numbers[indexNumber].length + 1
        }

        return result
    }

    private fun calculate(num1: Double, op: Char, num2: Double): Double {
        return when(op) {
            CHAR_PLUS -> num1 + num2
            CHAR_MINUS -> num1 - num2
            CHAR_MULTIPLY -> num1 * num2
            CHAR_DIVIDE -> num1 / num2
            else -> throw IllegalArgumentException()
        }
    }

    fun clear() {
        calculationString = ""
        state = State.INIT
    }

    enum class State {
        INIT, NUMBER, OPERATOR, POINT,
        POINT_NUMBER //小数点之后输入了数字
    }
}
