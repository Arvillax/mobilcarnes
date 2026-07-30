package com.example.fblogin.ui.admin

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.fblogin.ui.theme.Vino
import com.example.fblogin.ui.theme.Carmesi
import com.example.fblogin.ui.theme.Crimson
import com.example.fblogin.ui.theme.GrayLight
import com.example.fblogin.viewmodel.AuthViewModel
import com.example.fblogin.viewmodel.ProductosViewModel

// colores locales
private val White = Color(0xFFFFFFFF)
private val GreenAccent = Color(0xFF2E7D32)
private val RedAccent = Color(0xFFD32F2F)

// modelo producto
data class Producto(
    val id: String = "",
    val nombre: String = "",
    val precioKg: Double = 0.0,
    val stock: Int = 0,
    val descripcion: String = "",
    val imagenRes: Int? = null,
    val imagenUri: String? = null,
    val habilitado: Boolean = true
)

// datos mock
val productosMock = listOf(
    Producto("1", "Costillas de Res", 85.0, 50, "Costillas frescas de res premium", com.example.fblogin.R.drawable.producto_costillas_de_res),
    Producto("2", "Pechuga de Pollo", 45.0, 100, "Pechuga de pollo sin hueso", com.example.fblogin.R.drawable.producto_pechuga_pollo),
    Producto("3", "Lomo de Cerdo", 75.0, 30, "Lomo de cerdo magro", com.example.fblogin.R.drawable.producto_lomo),
    Producto("4", "Chuletas de Cordero", 120.0, 20, "Chuletas de cordero premium", null),
    Producto("5", "Albóndigas Mixtas", 55.0, 80, "Albóndigas de res y cerdo", null),
    Producto("6", "Punta de Anca", 95.0, 25, "Punta de anca argentina", null)
)

// helper para resolver modelo de imagen
fun modeloImagen(producto: Producto): Any? {
    return when {
        !producto.imagenUri.isNullOrBlank() -> producto.imagenUri
        producto.imagenRes != null -> producto.imagenRes
        else -> null
    }
}

// pantalla productos admin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductsScreen(
    nav: NavController,
    vm: AuthViewModel,
    productosVm: ProductosViewModel
) {
    val productos by productosVm.todosLosProductos.collectAsState()
    val busqueda by productosVm.busqueda.collectAsState()

    var mostrarDialogoAgregar by remember { mutableStateOf(false) }
    var productoAEditar by remember { mutableStateOf<Producto?>(null) }
    var productoADeshabilitar by remember { mutableStateOf<Producto?>(null) }

    Column(modifier = Modifier.fillMaxSize().background(White)) {
        // header gradiente
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(Vino, Carmesi)))
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Text(
                text = "Productos",
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
                onValueChange = { productosVm.buscar(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar producto...") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Carmesi,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // lista de productos
            if (productos.isEmpty()) {
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = "No se encontraron productos",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(productos) { producto ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (producto.habilitado) GrayLight else GrayLight.copy(alpha = 0.5f)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // imagen del producto
                                val imgModel = modeloImagen(producto)
                                if (imgModel != null) {
                                    AsyncImage(
                                        model = imgModel,
                                        contentDescription = producto.nombre,
                                        modifier = Modifier
                                            .size(80.dp)
                                            .clip(RoundedCornerShape(10.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(80.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFFE0E0E0)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("📷", fontSize = 24.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                // info producto
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = producto.nombre,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = "$${producto.precioKg}/kg · Stock: ${producto.stock} kg",
                                        fontSize = 12.sp,
                                        color = Crimson
                                    )
                                    Text(
                                        text = producto.descripcion,
                                        fontSize = 11.sp,
                                        color = Color.Gray,
                                        maxLines = 1,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (producto.habilitado) "HABILITADO" else "DESHABILITADO",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (producto.habilitado) GreenAccent else RedAccent,
                                        modifier = Modifier
                                            .background(
                                                color = if (producto.habilitado) GreenAccent.copy(alpha = 0.12f) else RedAccent.copy(alpha = 0.12f),
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                            .padding(horizontal = 6.dp, vertical = 1.dp)
                                    )
                                }

                                // acciones
                                Column(horizontalAlignment = Alignment.End) {
                                    OutlinedButton(
                                        onClick = { productoAEditar = producto },
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Editar", fontSize = 12.sp)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    OutlinedButton(
                                        onClick = { productoADeshabilitar = producto },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                                            contentColor = if (producto.habilitado) RedAccent else GreenAccent
                                        )
                                    ) {
                                        Text(
                                            if (producto.habilitado) "Deshabilitar" else "Habilitar",
                                            fontSize = 12.sp
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
                Text("+ Agregar producto", fontWeight = FontWeight.SemiBold)
            }
        }
    }

    // ========== DIALOGO AGREGAR ==========
    if (mostrarDialogoAgregar) {
        DialogoProducto(
            titulo = "Agregar producto",
            onGuardar = { nombre, precio, stock, descripcion, imagenUri ->
                productosVm.agregarProducto(nombre, precio, stock, descripcion, imagenUri = imagenUri)
                mostrarDialogoAgregar = false
            },
            onCancelar = { mostrarDialogoAgregar = false }
        )
    }

    // ========== DIALOGO EDITAR ==========
    productoAEditar?.let { producto ->
        DialogoProducto(
            titulo = "Editar producto",
            nombreInicial = producto.nombre,
            precioInicial = producto.precioKg,
            stockInicial = producto.stock,
            descripcionInicial = producto.descripcion,
            imagenUriInicial = producto.imagenUri,
            onGuardar = { nombre, precio, stock, descripcion, imagenUri ->
                productosVm.editarProducto(producto.id, nombre, precio, stock, descripcion, imagenUri = imagenUri)
                productoAEditar = null
            },
            onCancelar = { productoAEditar = null }
        )
    }

    // ========== DIALOGO DESHABILITAR ==========
    productoADeshabilitar?.let { producto ->
        AlertDialog(
            onDismissRequest = { productoADeshabilitar = null },
            title = {
                Text(
                    if (producto.habilitado) "Deshabilitar producto" else "Habilitar producto",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    if (producto.habilitado)
                        "¿Deshabilitar ${producto.nombre}? No será visible para clientes."
                    else
                        "¿Habilitar ${producto.nombre}? Volverá a ser visible para clientes."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (producto.habilitado) {
                            productosVm.deshabilitarProducto(producto.id)
                        } else {
                            productosVm.habilitarProducto(producto.id)
                        }
                        productoADeshabilitar = null
                    }
                ) {
                    Text(
                        if (producto.habilitado) "Deshabilitar" else "Habilitar",
                        color = if (producto.habilitado) RedAccent else GreenAccent,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { productoADeshabilitar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

// ========== COMPONENTE: DIALOGO AGREGAR/EDITAR PRODUCTO ==========
@Composable
private fun DialogoProducto(
    titulo: String,
    nombreInicial: String = "",
    precioInicial: Double = 0.0,
    stockInicial: Int = 0,
    descripcionInicial: String = "",
    imagenUriInicial: String? = null,
    onGuardar: (String, Double, Int, String, Uri?) -> Unit,
    onCancelar: () -> Unit
) {
    var nombre by remember { mutableStateOf(nombreInicial) }
    var precio by remember { mutableStateOf(if (precioInicial > 0) precioInicial.toString() else "") }
    var stock by remember { mutableStateOf(if (stockInicial > 0) stockInicial.toString() else "") }
    var descripcion by remember { mutableStateOf(descripcionInicial) }
    var imagenUri by remember { mutableStateOf<Uri?>(imagenUriInicial?.let { Uri.parse(it) }) }
    var errorNombre by remember { mutableStateOf(false) }
    var errorPrecio by remember { mutableStateOf(false) }
    var errorStock by remember { mutableStateOf(false) }

    // photo picker launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        imagenUri = uri
    }

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text(titulo, fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // preview de imagen
                if (imagenUri != null) {
                    AsyncImage(
                        model = imagenUri,
                        contentDescription = "Imagen del producto",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFE0E0E0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📷 Sin imagen", fontSize = 14.sp, color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // boton seleccionar imagen
                OutlinedButton(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Seleccionar imagen de galería", fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it; errorNombre = false },
                    label = { Text("Nombre del producto") },
                    isError = errorNombre,
                    supportingText = if (errorNombre) {{ Text("Requerido") }} else null,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // precio
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it; errorPrecio = false },
                    label = { Text("Precio por kg ($)") },
                    isError = errorPrecio,
                    supportingText = if (errorPrecio) {{ Text("Número inválido") }} else null,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // stock
                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it; errorStock = false },
                    label = { Text("Stock (kg)") },
                    isError = errorStock,
                    supportingText = if (errorStock) {{ Text("Número inválido") }} else null,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // descripcion
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    errorNombre = nombre.length < 2
                    errorPrecio = precio.toDoubleOrNull() == null || precio.toDouble() <= 0
                    errorStock = stock.toIntOrNull() == null || stock.toInt() < 0
                    if (!errorNombre && !errorPrecio && !errorStock) {
                        onGuardar(nombre, precio.toDouble(), stock.toInt(), descripcion, imagenUri)
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
