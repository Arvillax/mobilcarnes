package com.example.fblogin.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fblogin.ui.theme.Vino
import com.example.fblogin.ui.theme.Carmesi
import com.example.fblogin.ui.theme.Crimson
import com.example.fblogin.ui.theme.GrayLight
import com.example.fblogin.ui.theme.Rosa
import com.example.fblogin.viewmodel.AuthViewModel
import com.example.fblogin.viewmodel.UsuariosViewModel

// colores locales
private val White = Color(0xFFFFFFFF)
private val GreenAccent = Color(0xFF2E7D32)

// modelo usuario
data class Usuario(
    val id: String = "",
    val nombre: String = "",
    val email: String = "",
    val rol: String = "CLIENTE"
)

// datos mock
val usuariosMock = listOf(
    Usuario("1", "Juan Admin", "admin@carne.com", "ADMIN"),
    Usuario("2", "Maria Gestor", "gestor@carne.com", "GESTOR"),
    Usuario("3", "Carlos Cliente", "cliente@carne.com", "CLIENTE"),
    Usuario("4", "Ana Cliente", "ana@carne.com", "CLIENTE"),
    Usuario("5", "Pedro Gestor", "pedro@carne.com", "GESTOR"),
    Usuario("6", "PR Gestor", "pr@test.com", "GESTOR")
)

// roles disponibles
private val rolesDisponibles = listOf("ADMIN", "GESTOR", "CLIENTE")

// pantalla usuarios admin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsersScreen(
    nav: NavController,
    vm: AuthViewModel,
    usuariosVm: UsuariosViewModel
) {
    val usuarios by usuariosVm.usuariosFiltrados.collectAsState()
    val busqueda by usuariosVm.busqueda.collectAsState()
    val filtroRol by usuariosVm.filtroRol.collectAsState()

    // estado dialogs
    var mostrarDialogoAgregar by remember { mutableStateOf(false) }
    var usuarioAEditar by remember { mutableStateOf<Usuario?>(null) }
    var usuarioAEliminar by remember { mutableStateOf<Usuario?>(null) }

    Column(modifier = Modifier.fillMaxSize().background(White)) {
        // header gradiente
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(Vino, Carmesi)))
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
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // volver
            OutlinedButton(
                onClick = { nav.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("← Volver", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // barra de busqueda
            OutlinedTextField(
                value = busqueda,
                onValueChange = { usuariosVm.buscar(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar usuario...") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Carmesi,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // filtro por rol (chips)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = filtroRol == null,
                    onClick = { usuariosVm.filtrarPorRol(null) },
                    label = { Text("Todos", fontSize = 13.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Carmesi,
                        selectedLabelColor = White
                    )
                )
                FilterChip(
                    selected = filtroRol == "ADMIN",
                    onClick = { usuariosVm.filtrarPorRol("ADMIN") },
                    label = { Text("Admin", fontSize = 13.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Carmesi,
                        selectedLabelColor = White
                    )
                )
                FilterChip(
                    selected = filtroRol == "GESTOR",
                    onClick = { usuariosVm.filtrarPorRol("GESTOR") },
                    label = { Text("Gestor", fontSize = 13.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Carmesi,
                        selectedLabelColor = White
                    )
                )
                FilterChip(
                    selected = filtroRol == "CLIENTE",
                    onClick = { usuariosVm.filtrarPorRol("CLIENTE") },
                    label = { Text("Cliente", fontSize = 13.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Carmesi,
                        selectedLabelColor = White
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // lista de usuarios
            if (usuarios.isEmpty()) {
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = "No se encontraron usuarios",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(usuarios) { usuario ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = GrayLight),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                // nombre
                                Text(
                                    text = usuario.nombre,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                // email
                                Text(
                                    text = usuario.email,
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // rol badge + acciones
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // badge rol (clickeable para cambiar)
                                    RolBadge(
                                        rol = usuario.rol,
                                        onCambiarRol = { nuevoRol ->
                                            usuariosVm.cambiarRol(usuario.id, nuevoRol)
                                        }
                                    )

                                    Spacer(modifier = Modifier.weight(1f))

                                    // boton editar
                                    OutlinedButton(
                                        onClick = { usuarioAEditar = usuario },
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Editar", fontSize = 13.sp)
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    // boton eliminar
                                    IconButton(
                                        onClick = { usuarioAEliminar = usuario }
                                    ) {
                                        Text(
                                            "✕",
                                            color = Crimson,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // boton agregar
            OutlinedButton(
                onClick = { mostrarDialogoAgregar = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                    contentColor = Carmesi
                )
            ) {
                Text("+ Agregar usuario", fontWeight = FontWeight.SemiBold)
            }
        }
    }

    // ========== DIALOGO AGREGAR ==========
    if (mostrarDialogoAgregar) {
        DialogoUsuario(
            titulo = "Agregar usuario",
            nombreInicial = "",
            emailInicial = "",
            rolInicial = "CLIENTE",
            esEdicion = false,
            onGuardar = { nombre, email, rol, password ->
                if (password.isNotEmpty()) {
                    // crear en Firebase Auth + Firestore, luego re-login admin
                    usuariosVm.agregarUsuarioConAuth(nombre, email, password, rol) { exito ->
                        if (exito) {
                            vm.reLoginAdmin {}
                        }
                    }
                } else {
                    // solo Firestore (sin contraseña)
                    usuariosVm.agregarUsuario(nombre, email, rol)
                }
                mostrarDialogoAgregar = false
            },
            onCancelar = { mostrarDialogoAgregar = false }
        )
    }

    // ========== DIALOGO EDITAR ==========
    usuarioAEditar?.let { usuario ->
        DialogoUsuario(
            titulo = "Editar usuario",
            nombreInicial = usuario.nombre,
            emailInicial = usuario.email,
            rolInicial = usuario.rol,
            esEdicion = true,
            onGuardar = { nombre, email, rol, _ ->
                usuariosVm.editarUsuario(usuario.id, nombre, email, rol)
                usuarioAEditar = null
            },
            onCancelar = { usuarioAEditar = null }
        )
    }

    // ========== DIALOGO ELIMINAR ==========
    usuarioAEliminar?.let { usuario ->
        AlertDialog(
            onDismissRequest = { usuarioAEliminar = null },
            title = { Text("Eliminar usuario", fontWeight = FontWeight.Bold) },
            text = { Text("¿Eliminar a ${usuario.nombre}? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        usuariosVm.eliminarUsuario(usuario.id)
                        usuarioAEliminar = null
                    }
                ) {
                    Text("Eliminar", color = Crimson, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { usuarioAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

// ========== COMPONENTE: BADGE ROL CON DROPDOWN ==========
@Composable
private fun RolBadge(rol: String, onCambiarRol: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Text(
            text = rol,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = when (rol) {
                "ADMIN" -> Vino
                "GESTOR" -> Carmesi
                else -> Crimson
            },
            modifier = Modifier
                .background(
                    color = when (rol) {
                        "ADMIN" -> Vino.copy(alpha = 0.15f)
                        "GESTOR" -> Carmesi.copy(alpha = 0.15f)
                        else -> Rosa.copy(alpha = 0.25f)
                    },
                    shape = RoundedCornerShape(6.dp)
                )
                .clickable { expanded = true }
                .padding(horizontal = 8.dp, vertical = 2.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            rolesDisponibles.forEach { rolOpcion ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = rolOpcion,
                            fontWeight = if (rolOpcion == rol) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        onCambiarRol(rolOpcion)
                        expanded = false
                    }
                )
            }
        }
    }
}

// ========== COMPONENTE: DIALOGO AGREGAR/EDITAR ==========
@Composable
private fun DialogoUsuario(
    titulo: String,
    nombreInicial: String,
    emailInicial: String,
    rolInicial: String,
    esEdicion: Boolean,
    onGuardar: (String, String, String, String) -> Unit,
    onCancelar: () -> Unit
) {
    var nombre by remember { mutableStateOf(nombreInicial) }
    var email by remember { mutableStateOf(emailInicial) }
    var password by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf(rolInicial) }
    var errorNombre by remember { mutableStateOf(false) }
    var errorEmail by remember { mutableStateOf(false) }
    var errorPassword by remember { mutableStateOf(false) }
    var rolMenuExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onCancelar,
        title = {
            Text(titulo, fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                // nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        errorNombre = false
                    },
                    label = { Text("Nombre") },
                    isError = errorNombre,
                    supportingText = if (errorNombre) {
                        { Text("Mínimo 2 caracteres") }
                    } else null,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // email
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorEmail = false
                    },
                    label = { Text("Email") },
                    isError = errorEmail,
                    supportingText = if (errorEmail) {
                        { Text("Email inválido") }
                    } else null,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // contraseña (solo en modo agregar)
                if (!esEdicion) {
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorPassword = false
                        },
                        label = { Text("Contraseña") },
                        isError = errorPassword,
                        supportingText = if (errorPassword) {
                            { Text("Mínimo 6 caracteres") }
                        } else null,
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                // selector de rol
                Box {
                    OutlinedTextField(
                        value = rol,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Rol") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { rolMenuExpanded = true },
                        shape = RoundedCornerShape(10.dp),
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.Black,
                            disabledBorderColor = Color.LightGray,
                            disabledLabelColor = Color.Gray
                        )
                    )
                    // overlay invisible para capturar clicks
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { rolMenuExpanded = true }
                    )
                    DropdownMenu(
                        expanded = rolMenuExpanded,
                        onDismissRequest = { rolMenuExpanded = false }
                    ) {
                        rolesDisponibles.forEach { rolOpcion ->
                            DropdownMenuItem(
                                text = { Text(rolOpcion) },
                                onClick = {
                                    rol = rolOpcion
                                    rolMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    errorNombre = nombre.length < 2
                    errorEmail = !email.contains("@") || !email.contains(".")
                    errorPassword = !esEdicion && password.length < 6
                    if (!errorNombre && !errorEmail && !errorPassword) {
                        onGuardar(nombre, email, rol, password)
                    }
                }
            ) {
                Text("Guardar", color = Carmesi, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text("Cancelar")
            }
        }
    )
}
