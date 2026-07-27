package com.example.fblogin.ui.gestor

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

// Modelo de datos para ventas
data class Venta(val id: String, val fecha: String, val total: Double, val cliente: String, val productos: String)

// Lista mock de ventas del día (en real vendría de Firestore)
val ventasMock = listOf(
    Venta("1", "27/07/2026 10:30", 450.0, "Carlos Cliente", "Costillas x2kg, Pollo x1kg"),
    Venta("2", "27/07/2026 11:15", 280.0, "Ana Cliente", "Lomo x2kg, Chuletas x0.5kg"),
    Venta("3", "27/07/2026 14:00", 190.0, "Pedro Gestor", "Pechuga x3kg, Albóndigas x1kg"),
    Venta("4", "27/07/2026 15:45", 520.0, "Juan Admin", "Punta de Anca x4kg, Costillas x1kg"),
    Venta("5", "27/07/2026 16:30", 350.0, "Maria Gestor", "Lomo x3kg, Pollo x2kg")
)

// Pantalla de ventas del día - lista de transacciones
@Composable
fun GestorVentasScreen(nav: NavController, vm: AuthViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Ventas del Día")

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para volver
        Button(onClick = { nav.popBackStack() }) {
            Text("← Volver")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Total de ventas del día (mock)
        Text("Total ventas: $1,790")

        Spacer(modifier = Modifier.height(8.dp))

        // Lista de ventas
        LazyColumn {
            items(ventasMock) { venta ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Venta #${venta.id}")
                        Text("Fecha: ${venta.fecha}")
                        Text("Cliente: ${venta.cliente}")
                        Text("Productos: ${venta.productos}")
                        Text("Total: $${venta.total}")
                    }
                }
            }
        }
    }
}
