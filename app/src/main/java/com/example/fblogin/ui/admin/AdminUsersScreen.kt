package com.example.fblogin.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fblogin.viewmodel.AuthViewModel

// colores tienda carne
private val Vino = Color(0xFF610000)
private val Carmesi = Color(0xFF9C0720)
private val Crimson = Color(0xFFDC143C)
private val Rosa = Color(0xFFFF9EA2)
private val White = Color(0xFFFFFFFF)
private val GrayLight = Color(0xFFF5F5F5)

// modelo usuario
data class Usuario(val id: String, val nombre: String, val email: String, val rol: String)

// datos mock
val usuariosMock = listOf(
    Usuario("1", "Juan Admin", "admin@carne.com", "ADMIN"),
    Usuario("2", "Maria Gestor", "gestor@carne.com", "GESTOR"),
    Usuario("3", "Carlos Cliente", "cliente@carne.com", "CLIENTE"),
    Usuario("4", "Ana Cliente", "ana@carne.com", "CLIENTE"),
    Usuario("5", "Pedro Gestor", "pedro@carne.com", "GESTOR")
)

// pantalla usuarios
@Composable
fun AdminUsersScreen(nav: NavController, vm: AuthViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        // header gradiente
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(listOf(Vino, Carmesi))
                )
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Text(
                text = "Usuarios",
                color = White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // boton volver
            OutlinedButton(
                onClick = { nav.popBackStack() },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "← Volver")
            }

            // lista usuarios
            LazyColumn(
                modifier = Modifier.padding(top = 12.dp)
            ) {
                items(usuariosMock) { usuario ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = GrayLight)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // info usuario
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = usuario.nombre,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = usuario.email,
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                                // badge rol
                                Text(
                                    text = usuario.rol,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Crimson,
                                    modifier = Modifier
                                        .padding(top = 6.dp)
                                        .background(
                                            color = Rosa.copy(alpha = 0.25f),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }

                            // boton editar
                            OutlinedButton(
                                onClick = { },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Editar",
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
