package com.example.fblogin.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import com.example.fblogin.ui.theme.Vino
import com.example.fblogin.ui.theme.Carmesi
import com.example.fblogin.ui.theme.Crimson
import com.example.fblogin.ui.theme.GrayLight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fblogin.viewmodel.AuthViewModel

// modelo carrito
data class CarritoItem(val nombre: String, val cantidad: Double, val precioKg: Double)

// datos mock
val carritoMock = listOf(
    CarritoItem("Costillas de Res", 2.0, 85.0),
    CarritoItem("Pechuga de Pollo", 1.5, 45.0),
    CarritoItem("Lomo de Cerdo", 1.0, 75.0)
)

// pantalla carrito
@Composable
fun CarritoScreen(nav: NavController, vm: AuthViewModel) {

    val total = carritoMock.sumOf { it.cantidad * it.precioKg }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // header gradiente
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(Vino, Carmesi)))
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Mi Carrito",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            // volver
            OutlinedButton(
                onClick = { nav.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("← Volver", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // lista items
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(carritoMock) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = GrayLight),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.nombre,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${item.cantidad} kg x $${item.precioKg}/kg",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                            Text(
                                text = "$${String.format("%.2f", item.cantidad * item.precioKg)}",
                                color = Crimson,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // total
            Text(
                text = "Total: $${String.format("%.2f", total)}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Crimson,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // comprar
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Carmesi),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Comprar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
