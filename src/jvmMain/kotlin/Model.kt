import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class CalculatorModel {
    var expression: MutableState<String> = mutableStateOf("")
        private set

    private var lastResult: String = ""
    private var memory: Double = 0.0 // Added memory feature

    // Add input to the expression
    fun addInput(input: String) {
        if (input in listOf("+", "-", "*", "/")) {
            // Prevent consecutive operators
            if (expression.value.isNotEmpty() && expression.value.last() in listOf('+', '-', '*', '/')) {
                expression.value = expression.value.dropLast(1) + input
                return
            }
        }

        if (expression.value.isEmpty() && lastResult.isNotEmpty() && input !in listOf("+", "-", "*", "/")) {
            // Start a new calculation if there's a last result
            expression.value = input
            lastResult = ""
        } else {
            expression.value += input
        }
    }

    // Clear the expression
    fun clear() {
        expression.value = ""
        lastResult = ""
    }

    // Evaluate the expression
    fun evaluate() {
        try {
            if (!isValidExpression(expression.value)) {
                throw IllegalArgumentException("Invalid Expression")
            }

            val result = evaluateExpression(expression.value)
            lastResult = result.toString()
            expression.value = lastResult
        } catch (e: Exception) {
            expression.value = "Error"
        }
    }

    // Helper function to validate the expression
    private fun isValidExpression(expr: String): Boolean {
        // Check for basic invalid cases: empty, ends with an operator
        if (expr.isEmpty()) return false
        if (expr.last() in listOf('+', '-', '*', '/')) return false

        // Additional validations for parentheses and invalid characters
        val openParentheses = expr.count { it == '(' }
        val closeParentheses = expr.count { it == ')' }
        if (openParentheses != closeParentheses) return false

        return expr.all { it.isDigit() || it in listOf('+', '-', '*', '/', '(', ')', '.') }
    }

    // Tokenize and parse the expression manually
    private fun evaluateExpression(expr: String): Double {
        val tokens = tokenizeExpression(expr)
        return parseTokens(tokens)
    }

    private fun tokenizeExpression(expr: String): List<String> {
        val regex = Regex("(?<=[-+*/()])|(?=[-+*/()])")
        return expr.split(regex).filter { it.isNotBlank() }
    }

    private fun parseTokens(tokens: List<String>): Double {
        val operatorStack = mutableListOf<String>()
        val valueStack = mutableListOf<Double>()

        val precedence = mapOf("+" to 1, "-" to 1, "*" to 2, "/" to 2)

        fun applyOperation() {
            val op = operatorStack.removeAt(operatorStack.size - 1)
            val b = valueStack.removeAt(valueStack.size - 1)
            val a = valueStack.removeAt(valueStack.size - 1)
            valueStack.add(
                when (op) {
                    "+" -> a + b
                    "-" -> a - b
                    "*" -> a * b
                    "/" -> a / b
                    else -> throw IllegalArgumentException("Unknown operator: $op")
                }
            )
        }

        for (token in tokens) {
            when {
                token.toDoubleOrNull() != null -> valueStack.add(token.toDouble())
                token in precedence.keys -> {
                    while (operatorStack.isNotEmpty() &&
                        precedence[operatorStack.last()]!! >= precedence[token]!!
                    ) {
                        applyOperation()
                    }
                    operatorStack.add(token)
                }
                token == "(" -> operatorStack.add(token)
                token == ")" -> {
                    while (operatorStack.last() != "(") {
                        applyOperation()
                    }
                    operatorStack.removeAt(operatorStack.size - 1) // Remove "("
                }
            }
        }

        while (operatorStack.isNotEmpty()) {
            applyOperation()
        }

        return valueStack.last()
    }

    // Memory Operations
    private fun memoryClear() {
        memory = 0.0
    }

    fun memoryRecall() {
        expression.value += memory.toString()
    }

    fun memoryAdd() {
        try {
            val result = lastResult.toDoubleOrNull()
            if (result != null) {
                memory += result
            }
        } catch (e: Exception) {
            // Ignore invalid memory additions
        }
    }

    fun memorySubtract() {
        try {
            val result = lastResult.toDoubleOrNull()
            if (result != null) {
                memory -= result
            }
        } catch (e: Exception) {
            // Ignore invalid memory subtractions
        }
    }

    // Reset the calculator
    fun reset() {
        clear()
        memoryClear()
    }
}
