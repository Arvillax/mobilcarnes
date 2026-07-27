package com.example.fblogin.ui.cliente

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fblogin.viewmodel.AuthViewModel

// Modelo de pedidos del historial
data class PedidoHistorial(val id: String, val fecha: String, val total: Double, val productos: String, val estado: String)

// Lista mock del historial (en real vendría de Firestore)
val historialMock = listOf(
    PedidoHistorial("1", "25/07/2026", 450.0, "Costillas x2kg, Pollo x1kg", "Entregado"),
    PedidoHistorial("2", "20/07/2026", 280.0, "Lomo x2kg, Chuletas x0.5kg", "Entregado"),
    PedidoHistorial("3", "15/07/2026", 190.0, "Pechuga x3kg, Albóndigas x1kg", "Entregado"),
    PedidoHistorial("4", "10/07/2026", 520.0, "Punta de Anca x4kg, Costillas x1kg", "Entregado")
)

// Pantalla del historial de compras del cliente
@Composable
fun ClienteHistorialScreen(nav: NavController, vm: AuthViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Mi Historial de Compras")

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para volver
        Button(onClick = { nav.popBackStack() }) {
            Text("← Volver al catálogo")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Lista de pedidos anteriores
        LazyColumn {
            items(historialMock) { pedido ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Pedido #${pedido.id}")
                        Text("Fecha: ${pedido.fecha}")
                        Text("Productos: ${pedido.productos}")
                        Text("Total: $${pedido.total}")
                        Text("Estado: ${pedido.estado}")
                    }
                }
            }
        }
    }
}
