package com.zhl.simplecalulator

import com.zhl.simplecalulator.algorithm.*
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testCalculation() {
        val strings = listOf(
            "5*3-1",
            "1+3*9-8",
            "1/3",
            "1/3*3",
            "2/0",
            "2/0*0"
        )

        for (s in strings) {
            for (c in s) {
                Calculation.input(char2Code(c))
                println("=== " + Calculation.calculate())
            }
            println(Calculation.calculationString)
            println("--------------")
            Calculation.clear()
        }
    }

    private fun char2Code(c: Char): Int {
        when {
            c.isDigit() -> {
                return c - '0'
            }
            c == CHAR_PLUS -> {
                return CalculatorButton.CODE_PLUS
            }
            c == CHAR_MINUS -> {
                return CalculatorButton.CODE_MINUS
            }
            c == CHAR_MULTIPLY -> {
                return CalculatorButton.CODE_MULTIPLY
            }
            c == CHAR_DIVIDE -> {
                return CalculatorButton.CODE_DIVIDE
            }
            c == '.' -> {
                return CalculatorButton.CODE_POINT
            }
            else -> throw RuntimeException()
        }
    }
}