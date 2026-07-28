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
private val White = Color(0xFFFFFFFF)
private val GrayLight = Color(0xFFF5F5F5)

// modelo producto
data class Producto(val id: String, val nombre: String, val precioKg: Double, val stock: Int, val descripcion: String)

// datos mock
val productosMock = listOf(
    Producto("1", "Costillas de Res", 85.0, 50, "Costillas frescas de res premium"),
    Producto("2", "Pechuga de Pollo", 45.0, 100, "Pechuga de pollo sin hueso"),
    Producto("3", "Lomo de Cerdo", 75.0, 30, "Lomo de cerdo magro"),
    Producto("4", "Chuletas de Cordero", 120.0, 20, "Chuletas de cordero premium"),
    Producto("5", "Albóndigas Mixtas", 55.0, 80, "Albóndigas de res y cerdo"),
    Producto("6", "Punta de Anca", 95.0, 25, "Punta de anca argentina")
)

// pantalla productos
@Composable
fun AdminProductsScreen(nav: NavController, vm: AuthViewModel) {
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
                text = "Productos",
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

            // lista productos
            LazyColumn(
                modifier = Modifier.padding(top = 12.dp)
            ) {
                items(productosMock) { producto ->
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
                            // info producto
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = producto.nombre,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "$${producto.precioKg}/kg",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Crimson
                                )
                                Text(
                                    text = "Stock: ${producto.stock} kg",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = producto.descripcion,
                                    fontSize = 14.sp,
                                    color = Color.DarkGray,
                                    modifier = Modifier.padding(top = 4.dp)
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
