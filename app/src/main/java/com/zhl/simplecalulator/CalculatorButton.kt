package com.zhl.simplecalulator

class CalculatorButton(val name: String, val code: Int) {
    companion object {
        const val CODE_PLUS = 11
        const val CODE_MINUS = 12
        const val CODE_MULTIPLY = 13
        const val CODE_DIVIDE = 14
        const val CODE_POINT = 20

        private val row1 = listOf(CalculatorButton("7", 7),
            CalculatorButton("8", 8),
            CalculatorButton("9", 9),
            CalculatorButton("+", CODE_PLUS)
        )
        private val row2 = listOf(CalculatorButton("4", 4),
            CalculatorButton("5", 5),
            CalculatorButton("6", 6),
            CalculatorButton("-", CODE_MINUS)
        )
        private val row3 = listOf(CalculatorButton("1", 1),
            CalculatorButton("2", 2),
            CalculatorButton("3", 3),
            CalculatorButton("*", CODE_MULTIPLY)
        )
        private val row4 = listOf(CalculatorButton("Del", -1),
            CalculatorButton("0", 0),
            CalculatorButton(".", CODE_POINT),
            CalculatorButton("/", CODE_DIVIDE)
        )

        val calculatorButtons = listOf(row1, row2, row3, row4)
    }
}