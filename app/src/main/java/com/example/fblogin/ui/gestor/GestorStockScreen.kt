package com.example.fblogin.ui.gestor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.fblogin.ui.admin.Producto
import com.example.fblogin.ui.admin.modeloImagen
import com.example.fblogin.viewmodel.AuthViewModel
import com.example.fblogin.viewmodel.ProductosViewModel

private val Vino = Color(0xFF610000)
private val Carmesi = Color(0xFF9C0720)
private val Crimson = Color(0xFFDC143C)
private val Coral = Color(0xFFF1666D)
private val Rosa = Color(0xFFFF9EA2)
private val White = Color(0xFFFFFFFF)
private val Green = Color(0xFF2E7D32)

// pantalla stock
@Composable
fun GestorStockScreen(nav: NavController, vm: AuthViewModel, productosVm: ProductosViewModel) {
    val productos by productosVm.productosFiltrados.collectAsState()

    // estados para dialogos
    var productoSeleccionado by remember { mutableStateOf<Producto?>(null) }
    var mostrarDialogoOpciones by remember { mutableStateOf(false) }
    var mostrarDialogoInput by remember { mutableStateOf(false) }
    var tipoOperacion by remember { mutableStateOf("") } // "añadir" o "perdida"
    var cantidadTexto by remember { mutableStateOf("") }
    var errorCantidad by remember { mutableStateOf(false) }

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
                    Brush.linearGradient(
                        colors = listOf(Vino, Carmesi)
                    )
                )
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Text(
                text = "Stock",
                style = MaterialTheme.typography.headlineLarge,
                color = White,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // boton volver
            OutlinedButton(
                onClick = { nav.popBackStack() },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Carmesi
                )
            ) {
                Text(
                    text = "← Volver",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // lista productos
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(productos) { producto ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Rosa.copy(alpha = 0.3f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // imagen
                            val imgModel = modeloImagen(producto)
                            if (imgModel != null) {
                                AsyncImage(
                                    model = imgModel,
                                    contentDescription = producto.nombre,
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                            }

                            // info producto
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = producto.nombre,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Vino
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Stock: ${producto.stock} kg",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (producto.stock < 10) Crimson else Coral
                                )
                                Text(
                                    text = "Precio: $${producto.precioKg}/kg",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Coral
                                )
                            }

                            // boton actualizar
                            OutlinedButton(
                                onClick = {
                                    productoSeleccionado = producto
                                    mostrarDialogoOpciones = true
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Carmesi
                                )
                            ) {
                                Text(
                                    text = "Actualizar",
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // ========== DIALOGO: Opciones Añadir / Pérdida ==========
    if (mostrarDialogoOpciones && productoSeleccionado != null) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogoOpciones = false
                productoSeleccionado = null
            },
            title = {
                Text(
                    text = "Actualizar stock",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Producto: ${productoSeleccionado?.nombre}\nStock actual: ${productoSeleccionado?.stock} kg"
                )
            },
            confirmButton = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            tipoOperacion = "añadir"
                            cantidadTexto = ""
                            errorCantidad = false
                            mostrarDialogoOpciones = false
                            mostrarDialogoInput = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Green),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("+ Añadir", color = White)
                    }
                    Button(
                        onClick = {
                            tipoOperacion = "perdida"
                            cantidadTexto = ""
                            errorCantidad = false
                            mostrarDialogoOpciones = false
                            mostrarDialogoInput = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Crimson),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("− Pérdida", color = White)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    mostrarDialogoOpciones = false
                    productoSeleccionado = null
                }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // ========== DIALOGO: Input de cantidad ==========
    if (mostrarDialogoInput && productoSeleccionado != null) {
        val titulo = if (tipoOperacion == "añadir") "Añadir stock" else "Registrar pérdida"
        val colorBoton = if (tipoOperacion == "añadir") Green else Crimson
        val stockActual = productoSeleccionado?.stock ?: 0

        AlertDialog(
            onDismissRequest = {
                mostrarDialogoInput = false
                productoSeleccionado = null
            },
            title = {
                Text(
                    text = titulo,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = if (tipoOperacion == "añadir")
                            "¿Cuántos kg deseas añadir?"
                        else
                            "Stock actual: $stockActual kg\n¿Cuántos kg se perdieron?",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = cantidadTexto,
                        onValueChange = {
                            cantidadTexto = it.filter { c -> c.isDigit() || c == '.' }
                            errorCantidad = false
                        },
                        label = { Text("Cantidad (kg)") },
                        singleLine = true,
                        isError = errorCantidad,
                        supportingText = if (errorCantidad) {
                            { Text("Ingrese una cantidad válida", color = Crimson) }
                        } else null,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val cantidad = cantidadTexto.toDoubleOrNull()
                        if (cantidad == null || cantidad <= 0) {
                            errorCantidad = true
                            return@Button
                        }
                        if (tipoOperacion == "perdida" && cantidad > stockActual) {
                            errorCantidad = true
                            return@Button
                        }

                        val nuevoStock = if (tipoOperacion == "añadir") {
                            stockActual + cantidad.toInt()
                        } else {
                            stockActual - cantidad.toInt()
                        }

                        productoSeleccionado?.let { prod ->
                            productosVm.editarProducto(
                                id = prod.id,
                                nombre = prod.nombre,
                                precioKg = prod.precioKg,
                                stock = nuevoStock,
                                descripcion = prod.descripcion,
                                imagenUriExistente = prod.imagenUri
                            )
                        }

                        mostrarDialogoInput = false
                        productoSeleccionado = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorBoton),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Confirmar", color = White)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    mostrarDialogoInput = false
                    productoSeleccionado = null
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
