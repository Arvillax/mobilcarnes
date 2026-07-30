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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.fblogin.ui.admin.modeloImagen
import com.example.fblogin.ui.admin.productosMock
import com.example.fblogin.viewmodel.AuthViewModel
import com.example.fblogin.viewmodel.ProductosViewModel

private val Vino = Color(0xFF610000)
private val Carmesi = Color(0xFF9C0720)
private val Crimson = Color(0xFFDC143C)
private val Coral = Color(0xFFF1666D)
private val Rosa = Color(0xFFFF9EA2)
private val White = Color(0xFFFFFFFF)

// pantalla stock
@Composable
fun GestorStockScreen(nav: NavController, vm: AuthViewModel, productosVm: ProductosViewModel) {
    val productos by productosVm.productosFiltrados.collectAsState()
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
                                onClick = { },
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
}
