package com.example.fblogin.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fblogin.ui.theme.Carmesi
import com.example.fblogin.ui.theme.Crimson
import com.example.fblogin.ui.theme.Vino
import com.example.fblogin.ui.theme.White
import com.example.fblogin.viewmodel.AuthViewModel

// pantalla de registro
@Composable
fun RegisterScreen(nav: NavController, vm: AuthViewModel) {

    // campos
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    // observar estado
    val state by vm.state.collectAsState()
    val role by vm.role.collectAsState()

    // layout principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // header gradiente
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Vino, Carmesi)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("\uD83E\uDD69", fontSize = 52.sp)
                Text(
                    "Crear cuenta",
                    color = White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "CARNES Premium",
                    color = White.copy(alpha = 0.85f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }

        // formulario
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // campo email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Crimson,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = Crimson,
                    cursorColor = Crimson
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // campo password
            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("Password") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Crimson,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = Crimson,
                    cursorColor = Crimson
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            // boton registro
            Button(
                onClick = { vm.register(email, pass) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    "REGISTRARSE",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    letterSpacing = 2.sp
                )
            }

            // link login
            TextButton(onClick = { nav.navigate("login") }) {
                Text(
                    "\u00BFYa ten\u00E9s cuenta? Ingres\u00E1 ac\u00E1",
                    color = Crimson,
                    fontSize = 14.sp
                )
            }

            // alerta
            if (state.isNotEmpty()) {
                Text(
                    text = state,
                    color = if (state == "SUCCESS") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

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
