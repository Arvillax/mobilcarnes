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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fblogin.ui.theme.Vino
import com.example.fblogin.ui.theme.Carmesi
import com.example.fblogin.ui.theme.Crimson
import com.example.fblogin.ui.theme.GrayLight
import com.example.fblogin.viewmodel.AuthViewModel
import com.example.fblogin.viewmodel.CarritoViewModel

// pantalla carrito
@Composable
fun CarritoScreen(
    nav: NavController,
    vm: AuthViewModel,
    carritoViewModel: CarritoViewModel
) {

    val items by carritoViewModel.items.collectAsState()
    val total = carritoViewModel.total()

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // header gradiente
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(Vino, Carmesi)))
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Mi Carrito",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            // volver
            OutlinedButton(
                onClick = { nav.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("← Volver", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (items.isEmpty()) {
                // carrito vacio
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = "Tu carrito está vacío",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            } else {
                // lista items
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(items) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            colors = CardDefaults.cardColors(containerColor = GrayLight),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                // nombre y boton eliminar
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.producto.nombre,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f)
                                    )
                                    TextButton(
                                        onClick = {
                                            carritoViewModel.eliminarProducto(item.producto.id)
                                        }
                                    ) {
                                        Text("✕", color = Crimson, fontSize = 16.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                // precio por kg
                                Text(
                                    text = "$${item.producto.precioKg}/kg",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // selector de cantidad inline
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            val nueva = item.cantidad - 1.0
                                            if (nueva >= 1.0) {
                                                carritoViewModel.actualizarCantidad(
                                                    item.producto, nueva
                                                )
                                            }
                                        },
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("−", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Text(
                                        text = "${String.format("%.1f", item.cantidad)} kg",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.width(12.dp))

                                    OutlinedButton(
                                        onClick = {
                                            val nueva = item.cantidad + 1.0
                                            if (nueva <= item.producto.stock) {
                                                carritoViewModel.actualizarCantidad(
                                                    item.producto, nueva
                                                )
                                            }
                                        },
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("+", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    }

                                    Spacer(modifier = Modifier.weight(1f))

                                    // subtotal del item
                                    Text(
                                        text = "$${String.format("%.2f", item.cantidad * item.producto.precioKg)}",
                                        color = Crimson,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // total
            Text(
                text = "Total: $${String.format("%.2f", total)}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Crimson,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // comprar
            Button(
                onClick = { nav.navigate("cliente/factura") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Carmesi),
                shape = RoundedCornerShape(12.dp),
                enabled = items.isNotEmpty()
            ) {
                Text("Comprar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
