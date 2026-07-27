package com.example.fblogin.ui.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.fblogin.viewmodel.AuthViewModel

// Pantalla de Registro
@Composable
fun RegisterScreen(nav: NavController, vm: AuthViewModel) {

    // campos
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    // observar estado del ViewModel
    val state by vm.state.collectAsState()
    val role by vm.role.collectAsState()

    // registro
    Column {
        Text("Registro - Tienda de Carne")

        // Campo de email
        OutlinedTextField(email, { email = it }, label = { Text("Email") })

        // Campo de password
        OutlinedTextField(pass, { pass = it }, label = { Text("Password") })

        // Botón de registro - crea nueva cuenta
        Button(onClick = { vm.register(email, pass) }) {
            Text("Crear cuenta")
        }

        // alerta
        Text(state)

        // validacion
        if (state == "SUCCESS") {
            LaunchedEffect(Unit) {
                val route = when (role) {
                    com.example.fblogin.viewmodel.UserRole.ADMIN -> "admin/dashboard"
                    com.example.fblogin.viewmodel.UserRole.GESTOR -> "gestor/dashboard"
                    com.example.fblogin.viewmodel.UserRole.CLIENTE -> "cliente/catalogo"
                }
                nav.navigate(route) {
                    popUpTo("register") { inclusive = true }
                }
            }
        }
    }
}
