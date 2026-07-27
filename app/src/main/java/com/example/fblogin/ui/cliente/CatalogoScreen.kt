package com.example.fblogin.ui.cliente

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fblogin.ui.admin.productosMock
import com.example.fblogin.viewmodel.AuthViewModel

// pantalla catalogo
@Composable
fun CatalogoScreen(nav: NavController, vm: AuthViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Catálogo de Carnes")

        Spacer(modifier = Modifier.height(16.dp))

        // boton carrito
        Button(onClick = { nav.navigate("cliente/carrito") }) {
            Text("🛒 Ver Carrito")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // boton historial
        Button(onClick = { nav.navigate("cliente/historial") }) {
            Text("📋 Mi Historial")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // logout
        Button(onClick = { vm.logout(); nav.navigate("login") { popUpTo(0) { inclusive = true } } }) {
            Text("Cerrar sesión")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // lista
        LazyColumn {
            items(productosMock) { producto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { nav.navigate("cliente/detalle/${producto.id}") }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(producto.nombre)
                        Text("$${producto.precioKg}/kg")
                        Text("Stock: ${producto.stock} kg")
                    }
                }
            }
        }
    }
}
