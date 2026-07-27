package com.example.fblogin.ui.admin

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

// dashboard admin
@Composable
fun AdminDashboard(nav: NavController, vm: AuthViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Admin Dashboard")

        Spacer(modifier = Modifier.height(16.dp))

        // estadisticas
        Text("Total Usuarios: 25")
        Text("Total Productos: 15")
        Text("Ventas del mes: $12,500")

        Spacer(modifier = Modifier.height(16.dp))

        // boton usuarios
        Button(onClick = { nav.navigate("admin/users") }) {
            Text("Gestionar Usuarios")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // boton productos
        Button(onClick = { nav.navigate("admin/products") }) {
            Text("Gestionar Productos")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // logout
        Button(onClick = { vm.logout(); nav.navigate("login") { popUpTo(0) { inclusive = true } } }) {
            Text("Cerrar sesión")
        }
    }
}
