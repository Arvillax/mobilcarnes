package com.example.fblogin.ui.admin

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

// Modelo de datos para productos de carne
data class Producto(val id: String, val nombre: String, val precioKg: Double, val stock: Int, val descripcion: String)

// Lista mock de productos (en real vendría de Firestore)
val productosMock = listOf(
    Producto("1", "Costillas de Res", 85.0, 50, "Costillas frescas de res premium"),
    Producto("2", "Pechuga de Pollo", 45.0, 100, "Pechuga de pollo sin hueso"),
    Producto("3", "Lomo de Cerdo", 75.0, 30, "Lomo de cerdo magro"),
    Producto("4", "Chuletas de Cordero", 120.0, 20, "Chuletas de cordero premium"),
    Producto("5", "Albóndigas Mixtas", 55.0, 80, "Albóndigas de res y cerdo"),
    Producto("6", "Punta de Anca", 95.0, 25, "Punta de anca argentina")
)

// Pantalla de gestión de productos - lista y CRUD
@Composable
fun AdminProductsScreen(nav: NavController, vm: AuthViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Gestión de Productos")

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para volver
        Button(onClick = { nav.popBackStack() }) {
            Text("← Volver")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Lista de productos
        LazyColumn {
            items(productosMock) { producto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        // Info del producto
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Nombre: ${producto.nombre}")
                            Text("Precio/kg: $${producto.precioKg}")
                            Text("Stock: ${producto.stock} kg")
                            Text("Descripción: ${producto.descripcion}")
                        }
                        // Botón de editar (mock)
                        Button(onClick = { /* Editar */ }) {
                            Text("Editar")
                        }
                    }
                }
            }
        }
    }
}
