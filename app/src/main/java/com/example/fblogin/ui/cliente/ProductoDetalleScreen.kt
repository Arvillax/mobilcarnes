package com.example.fblogin.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
// import androidx.compose.material3.OutlinedIconButton // COMENTADO: no se usa en este archivo
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.fblogin.ui.admin.modeloImagen
// import com.example.fblogin.ui.admin.productosMock // COMENTADO: no se usa, datos vienen de Firestore
import com.example.fblogin.ui.theme.Vino
import com.example.fblogin.ui.theme.Carmesi
import com.example.fblogin.ui.theme.Crimson
import com.example.fblogin.viewmodel.AuthViewModel
import com.example.fblogin.viewmodel.CarritoViewModel
import com.example.fblogin.viewmodel.ProductosViewModel

// pantalla detalle
@Composable
fun ProductoDetalleScreen(
    nav: NavController,
    vm: AuthViewModel,
    productoId: String,
    carritoViewModel: CarritoViewModel,
    productosVm: ProductosViewModel
) {

    val productos by productosVm.productosFiltrados.collectAsState()
    val producto = productos.find { it.id == productoId }
    var cantidad by remember { mutableStateOf(1.0) }
    var agregado by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // header gradiente
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(Vino, Carmesi)))
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Column {
                Text(
                    text = "Detalle",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // volver
            OutlinedButton(
                onClick = { nav.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("← Volver", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (producto != null) {
                // imagen del producto
                val imgModel = modeloImagen(producto)
                if (imgModel != null) {
                    AsyncImage(
                        model = imgModel,
                        contentDescription = producto.nombre,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "$${producto.precioKg}/kg",
                    color = Crimson,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Stock disponible: ${producto.stock} kg",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = producto.descripcion,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(24.dp))

                // selector de cantidad
                Text(
                    text = "Cantidad (kg):",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // boton restar
                    OutlinedButton(
                        onClick = {
                            if (cantidad > 1.0) {
                                cantidad -= 1.0
                                agregado = false
                            }
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("−", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // cantidad actual
                    Text(
                        text = "${String.format("%.0f", cantidad)} kg",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // boton sumar
                    OutlinedButton(
                        onClick = {
                            if (cantidad < producto.stock) {
                                cantidad += 1.0
                                agregado = false
                            }
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // subtotal preview
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Subtotal: $${String.format("%.2f", cantidad * producto.precioKg)}",
                    fontSize = 16.sp,
                    color = Crimson,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.weight(1f))

                // feedback
                if (agregado) {
                    Text(
                        text = "✓ Agregado al carrito",
                        color = Color(0xFF2E7D32),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // agregar carrito
                Button(
                    onClick = {
                        carritoViewModel.agregarProducto(producto, cantidad)
                        agregado = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Carmesi),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Agregar al carrito", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Text(
                    text = "Producto no encontrado",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        }
    }
}
