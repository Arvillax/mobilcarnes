package com.example.fblogin.ui.cliente

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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

// Modelo de items del carrito
data class CarritoItem(val nombre: String, val cantidad: Double, val precioKg: Double)

// Lista mock del carrito (en real se guardaría en Firestore o local)
val carritoMock = listOf(
    CarritoItem("Costillas de Res", 2.0, 85.0),
    CarritoItem("Pechuga de Pollo", 1.5, 45.0),
    CarritoItem("Lomo de Cerdo", 1.0, 75.0)
)

// Pantalla del carrito - muestra productos seleccionados y total
@Composable
fun CarritoScreen(nav: NavController, vm: AuthViewModel) {

    // Calcular total del carrito
    val total = carritoMock.sumOf { it.cantidad * it.precioKg }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Mi Carrito")

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para volver
        Button(onClick = { nav.popBackStack() }) {
            Text("← Volver al catálogo")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Lista de items en el carrito
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(carritoMock) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        // Info del item
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.nombre)
                            Text("${item.cantidad} kg x $${item.precioKg}/kg")
                        }
                        // Subtotal del item
                        Text("$${item.cantidad * item.precioKg}")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Total a pagar
        Text("Total: $${total}")

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para comprar (mock)
        Button(onClick = { /* Realizar compra */ }) {
            Text("Comprar")
        }
    }
}
