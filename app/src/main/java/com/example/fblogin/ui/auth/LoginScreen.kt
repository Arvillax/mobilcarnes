package com.example.fblogin.ui.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fblogin.viewmodel.AuthViewModel

// pantalla de login
@Composable
fun LoginScreen(nav: NavController, vm: AuthViewModel) {

    // campos
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    // observar estado
    val state by vm.state.collectAsState()
    val role by vm.role.collectAsState()

    // layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login - Tienda de Carne")

        // campo email
        OutlinedTextField(email, { email = it }, label = { Text("Email") })

        // campo password
        OutlinedTextField(pass, { pass = it }, label = { Text("Password") })

        // boton login
        Button(onClick = { vm.login(email, pass) }) {
            Text("Entrar")
        }

        // link registro
        TextButton(onClick = { nav.navigate("register") }) {
            Text("Ir a registro")
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
                    popUpTo("login") { inclusive = true }
                }
            }
        }
    }
}
