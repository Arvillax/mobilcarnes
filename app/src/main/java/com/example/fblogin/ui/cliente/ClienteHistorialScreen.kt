package com.example.fblogin.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import com.example.fblogin.ui.theme.Vino
import com.example.fblogin.ui.theme.Carmesi
import com.example.fblogin.ui.theme.Crimson
import com.example.fblogin.ui.theme.Rosa
import com.example.fblogin.ui.theme.GrayLight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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

// modelo pedido
data class PedidoHistorial(val id: String, val fecha: String, val total: Double, val productos: String, val estado: String)

// datos mock
val historialMock = listOf(
    PedidoHistorial("1", "25/07/2026", 450.0, "Costillas x2kg, Pollo x1kg", "Entregado"),
    PedidoHistorial("2", "20/07/2026", 280.0, "Lomo x2kg, Chuletas x0.5kg", "Entregado"),
    PedidoHistorial("3", "15/07/2026", 190.0, "Pechuga x3kg, Albóndigas x1kg", "Entregado"),
    PedidoHistorial("4", "10/07/2026", 520.0, "Punta de Anca x4kg, Costillas x1kg", "Entregado")
)

// pantalla historial
@Composable
fun ClienteHistorialScreen(nav: NavController, vm: AuthViewModel) {
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // header gradiente
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(Vino, Carmesi)))
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Mi Historial",
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

            // lista pedidos
            LazyColumn {
                items(historialMock) { pedido ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = GrayLight),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Pedido #${pedido.id}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = pedido.fecha,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = pedido.productos,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "$${pedido.total}",
                                color = Crimson,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            // badge estado
                            Text(
                                text = pedido.estado,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .padding(top = 6.dp)
                                    .background(Rosa, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
