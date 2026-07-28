package com.example.fblogin.ui.gestor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fblogin.viewmodel.AuthViewModel

private val Vino = Color(0xFF610000)
private val Carmesi = Color(0xFF9C0720)
private val Crimson = Color(0xFFDC143C)
private val Coral = Color(0xFFF1666D)
private val Rosa = Color(0xFFFF9EA2)
private val White = Color(0xFFFFFFFF)

// modelo venta
data class Venta(val id: String, val fecha: String, val total: Double, val cliente: String, val productos: String)

// datos mock
val ventasMock = listOf(
    Venta("1", "27/07/2026 10:30", 450.0, "Carlos Cliente", "Costillas x2kg, Pollo x1kg"),
    Venta("2", "27/07/2026 11:15", 280.0, "Ana Cliente", "Lomo x2kg, Chuletas x0.5kg"),
    Venta("3", "27/07/2026 14:00", 190.0, "Pedro Gestor", "Pechuga x3kg, Albóndigas x1kg"),
    Venta("4", "27/07/2026 15:45", 520.0, "Juan Admin", "Punta de Anca x4kg, Costillas x1kg"),
    Venta("5", "27/07/2026 16:30", 350.0, "Maria Gestor", "Lomo x3kg, Pollo x2kg")
)

// pantalla ventas
@Composable
fun GestorVentasScreen(nav: NavController, vm: AuthViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        // header gradiente
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Vino, Carmesi)
                    )
                )
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Text(
                text = "Ventas del Día",
                style = MaterialTheme.typography.headlineLarge,
                color = White,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // boton volver
            OutlinedButton(
                onClick = { nav.popBackStack() },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Carmesi
                )
            ) {
                Text(
                    text = "← Volver",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // total ventas
            Text(
                text = "Total: $1,790",
                style = MaterialTheme.typography.headlineMedium,
                color = Crimson,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // lista ventas
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(ventasMock) { venta ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Rosa.copy(alpha = 0.3f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            // id y fecha
                            Text(
                                text = "Venta #${venta.id}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Vino
                            )
                            Text(
                                text = venta.fecha,
                                style = MaterialTheme.typography.bodySmall,
                                color = Coral
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // cliente
                            Text(
                                text = venta.cliente,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Carmesi
                            )

                            // productos
                            Text(
                                text = venta.productos,
                                style = MaterialTheme.typography.bodySmall,
                                color = Coral
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // total
                            Text(
                                text = "$${venta.total}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Crimson
                            )
                        }
                    }
                }
            }
        }
    }
}
