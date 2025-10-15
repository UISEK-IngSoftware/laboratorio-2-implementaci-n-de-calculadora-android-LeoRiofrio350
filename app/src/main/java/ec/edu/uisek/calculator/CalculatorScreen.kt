package ec.edu.uisek.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = viewModel()
) {
    val state = viewModel.state

    // 🌈 Fondo con nuevo degradado
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F172A), // azul gris oscuro
            Color(0xFF1E293B)  // gris azulado
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        // 🧾 Pantalla del resultado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF111827))
                .padding(20.dp)
        ) {
            Text(
                text = state.display,
                modifier = Modifier.align(Alignment.BottomEnd),
                color = Color.White,
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                textAlign = TextAlign.End
            )
        }

        // 🧮 Cuadrícula de botones
        CalculatorGrid(onEvent = viewModel::onEvent)
    }
}

@Composable
fun CalculatorGrid(onEvent: (CalculatorEvent) -> Unit) {
    val buttons = listOf(
        "7", "8", "9", "÷",
        "4", "5", "6", "*",
        "1", "2", "3", "−",
        "0", ".", "=", "+"
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(buttons.size) { index ->
            val label = buttons[index]
            CalculatorButton(label = label) {
                when (label) {
                    in "0".."9" -> onEvent(CalculatorEvent.Number(label))
                    "." -> onEvent(CalculatorEvent.Decimal)
                    "=" -> onEvent(CalculatorEvent.Calculate)
                    else -> onEvent(CalculatorEvent.Operator(label))
                }
            }
        }

        item(span = { GridItemSpan(2) }) {
            CalculatorButton(label = "AC") { onEvent(CalculatorEvent.AllClear) }
        }
        item {}
        item {
            CalculatorButton(label = "C") { onEvent(CalculatorEvent.Clear) }
        }
    }
}

@Composable
fun CalculatorButton(label: String, onClick: () -> Unit) {
    val isOperator = label in listOf("÷", "*", "−", "+", "=", ".")
    val backgroundColor = when {
        label == "AC" || label == "C" -> Color(0xFFEF4444) // rojo suave
        isOperator -> Color(0xFFF97316) // naranja
        else -> Color(0xFF2563EB) // azul brillante
    }

    Box(
        modifier = Modifier
            .aspectRatio(if (label == "AC") 2f else 1f)
            .clip(CircleShape)
            .shadow(8.dp, CircleShape)
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0x000000)
@Composable
fun CalculatorPreview() {
    CalculatorScreen()
}
