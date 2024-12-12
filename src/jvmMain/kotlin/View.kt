import CalculatorModel
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Display Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = model.expression.value.ifEmpty { "0" },
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = androidx.compose.ui.text.style.TextAlign.End,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
        }

        // Buttons Section
        val buttonRows = listOf(
            listOf("AC", "+/-", "%", "÷"),
            listOf("7", "8", "9", "×"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("icon", "0", ",", "=") // Add the icon to the last row
        )

        buttonRows.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { button ->
                    if (button == "icon") {
                        IconButton(model)
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
            .size(75.dp)
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
                    "+/-" -> model.addInput("-")
                    "%" -> model.addInput("%")
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

@Composable
fun IconButton(model: CalculatorModel) {
    Box(
        modifier = Modifier
            .size(75.dp)
            .clip(CircleShape)
            .background(Color(0xFF323232)) // Same background as number buttons
            .clickable {
                // You can define the behavior for this button
                model.clear() // Example: Clear on click
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource("Small_icons-calculator-50.png"), // Path to your icon file
            contentDescription = "Calculator Icon",
            modifier = Modifier.size(50.dp),
            contentScale = ContentScale.Fit
        )
    }
}
