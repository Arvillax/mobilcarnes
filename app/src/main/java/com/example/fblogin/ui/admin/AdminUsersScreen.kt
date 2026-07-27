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

// Modelo de datos para usuarios
data class Usuario(val id: String, val nombre: String, val email: String, val rol: String)

// Lista mock de usuarios (en real vendría de Firestore)
val usuariosMock = listOf(
    Usuario("1", "Juan Admin", "admin@carne.com", "ADMIN"),
    Usuario("2", "Maria Gestor", "gestor@carne.com", "GESTOR"),
    Usuario("3", "Carlos Cliente", "cliente@carne.com", "CLIENTE"),
    Usuario("4", "Ana Cliente", "ana@carne.com", "CLIENTE"),
    Usuario("5", "Pedro Gestor", "pedro@carne.com", "GESTOR")
)

// Pantalla de gestión de usuarios - lista y CRUD
@Composable
fun AdminUsersScreen(nav: NavController, vm: AuthViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Gestión de Usuarios")

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para volver al dashboard
        Button(onClick = { nav.popBackStack() }) {
            Text("← Volver")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Lista de usuarios con LazyColumn (renderizado eficiente)
        LazyColumn {
            items(usuariosMock) { usuario ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        // Info del usuario
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Nombre: ${usuario.nombre}")
                            Text("Email: ${usuario.email}")
                            Text("Rol: ${usuario.rol}")
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
