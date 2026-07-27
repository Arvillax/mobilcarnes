package com.example.fblogin.ui.gestor

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
import com.example.fblogin.ui.admin.productosMock
import com.example.fblogin.viewmodel.AuthViewModel

// Pantalla de gestión de stock - permite actualizar cantidades
@Composable
fun GestorStockScreen(nav: NavController, vm: AuthViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Gestión de Stock")

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para volver
        Button(onClick = { nav.popBackStack() }) {
            Text("← Volver")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Lista de productos con su stock actual
        LazyColumn {
            items(productosMock) { producto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        // Info del stock
                        Column(modifier = Modifier.weight(1f)) {
                            Text("${producto.nombre}")
                            Text("Stock actual: ${producto.stock} kg")
                            Text("Precio: $${producto.precioKg}/kg")
                        }
                        // Botón para actualizar stock (mock)
                        Button(onClick = { /* Actualizar stock */ }) {
                            Text("Actualizar")
                        }
                    }
                }
            }
        }
    }
}
