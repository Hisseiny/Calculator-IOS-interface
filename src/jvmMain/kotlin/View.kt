import CalculatorModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalculatorView(model: CalculatorModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween // Space display and buttons
    ) {
        // Display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp), // Fixed height for the display area
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = model.expression.value.ifEmpty { "0" },
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.End // Align text to the end
            )
        }

        // Buttons
        val buttonRows = listOf(
            listOf("AC", "+/-", "%", "÷"),
            listOf("7", "8", "9", "×"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("0", ",", "=")
        )

        buttonRows.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { button ->
                    if (button == "0") {
                        // Special handling for "0" to span two columns
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.5f) // Spans two columns proportionally
                                .aspectRatio(4f) // Maintain proper proportions
                                .clip(CircleShape)
                                .background(Color(0xFF323232))
                                .clickable { model.addInput(button) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = button,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    } else {
                        CalculatorButton(
                            text = button,
                            model = model,
                            isOperator = button in listOf("÷", "×", "-", "+", "="),
                            isSpecial = button in listOf("AC", "+/-", "%")
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    model: CalculatorModel,
    isOperator: Boolean = false,
    isSpecial: Boolean = false
) {
    Box(
        modifier = Modifier
            .size(75.dp) // Set size directly for consistent button dimensions
            .clip(CircleShape)
            .background(
                when {
                    isOperator -> Color(0xFFFF9500) // Orange for operators
                    isSpecial -> Color(0xFF505050) // Dark gray for special buttons
                    else -> Color(0xFF323232) // Gray for numbers
                }
            )
            .clickable {
                when (text) {
                    "=" -> model.evaluate()
                    "AC" -> model.clear()
                    "+/-" -> model.addInput("-") // Simplified +/- logic
                    "%" -> model.addInput("%") // Implement modulo logic as needed
                    else -> model.addInput(text)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}
