package com.example.fblogin.ui.cliente

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fblogin.ui.admin.productosMock
import com.example.fblogin.viewmodel.AuthViewModel

// pantalla detalle
@Composable
fun ProductoDetalleScreen(nav: NavController, vm: AuthViewModel, productoId: String) {

    // buscar producto
    val producto = productosMock.find { it.id == productoId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // boton volver
        Button(onClick = { nav.popBackStack() }) {
            Text("← Volver al catálogo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (producto != null) {
            // info
            Text(producto.nombre, style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(8.dp))

            Text("Precio: $${producto.precioKg}/kg")
            Text("Stock disponible: ${producto.stock} kg")

            Spacer(modifier = Modifier.height(8.dp))

            Text("Descripción:")
            Text(producto.descripcion)

            Spacer(modifier = Modifier.height(16.dp))

            // agregar al carrito
            Button(onClick = { }) {
                Text("Agregar al carrito")
            }
        } else {
            // no encontrado
            Text("Producto no encontrado")
        }
    }
}
