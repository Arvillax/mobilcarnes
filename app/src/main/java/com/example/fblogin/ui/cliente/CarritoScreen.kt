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

    // total
    val total = carritoMock.sumOf { it.cantidad * it.precioKg }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Mi Carrito")

        Spacer(modifier = Modifier.height(16.dp))

        // boton volver
        Button(onClick = { nav.popBackStack() }) {
            Text("← Volver al catálogo")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // lista
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(carritoMock) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        // info
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.nombre)
                            Text("${item.cantidad} kg x $${item.precioKg}/kg")
                        }
                        // subtotal
                        Text("$${item.cantidad * item.precioKg}")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // total
        Text("Total: $${total}")

        Spacer(modifier = Modifier.height(8.dp))

        // comprar
        Button(onClick = { }) {
            Text("Comprar")
        }
    }
}
