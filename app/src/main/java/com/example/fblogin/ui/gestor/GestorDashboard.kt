package com.example.fblogin.ui.gestor

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
import com.example.fblogin.viewmodel.AuthViewModel

// dashboard gestor
@Composable
fun GestorDashboard(nav: NavController, vm: AuthViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Gestor Dashboard")

        Spacer(modifier = Modifier.height(16.dp))

        // estadisticas
        Text("Ventas hoy: $3,200")
        Text("Stock bajo: 3 productos")
        Text("Pedidos pendientes: 5")

        Spacer(modifier = Modifier.height(16.dp))

        // boton stock
        Button(onClick = { nav.navigate("gestor/stock") }) {
            Text("Gestionar Stock")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // boton ventas
        Button(onClick = { nav.navigate("gestor/ventas") }) {
            Text("Ver Ventas")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // logout
        Button(onClick = { vm.logout(); nav.navigate("login") { popUpTo(0) { inclusive = true } } }) {
            Text("Cerrar sesión")
        }
    }
}
