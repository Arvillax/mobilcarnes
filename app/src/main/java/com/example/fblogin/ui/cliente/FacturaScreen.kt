package com.example.fblogin.ui.cliente

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fblogin.ui.theme.Carmesi
import com.example.fblogin.ui.theme.Crimson
import com.example.fblogin.viewmodel.CarritoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// colores factura
private val FacturaBg = Color(0xFFFAFAF5)
private val FacturaBorder = Carmesi
private val SeparatorColor = Color(0xFFBBBBBB)

// pantalla factura (recibo)
@Composable
fun FacturaScreen(
    nav: NavController,
    carritoViewModel: CarritoViewModel,
    vm: com.example.fblogin.viewmodel.AuthViewModel
) {

    val items = carritoViewModel.items.value
    val total = carritoViewModel.total()
    val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    val hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // recibo
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            border = BorderStroke(2.dp, FacturaBorder),
            colors = CardDefaults.cardColors(containerColor = FacturaBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // header tienda
                Text(
                    text = "CARNE FRESCA",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace,
                    color = Carmesi
                )
                Text(
                    text = "Tu compra",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(12.dp))

                // fecha y hora
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Fecha: $fecha",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color.DarkGray
                    )
                    Text(
                        text = hora,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color.DarkGray
                    )
                }

                // separador
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "─".repeat(34),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    color = SeparatorColor
                )

                // items
                items.forEach { item ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.producto.nombre,
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color.Black,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${String.format("%.1f", item.cantidad)} kg",
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        Text(
                            text = "$${String.format("%.2f", item.cantidad * item.producto.precioKg)}",
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }

                // separador
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "─".repeat(34),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    color = SeparatorColor
                )

                // subtotal
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Subtotal (${items.size} ${if (items.size == 1) "item" else "items"})",
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "$${String.format("%.2f", total)}",
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color.Black
                    )
                }

                // separador doble
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "═".repeat(34),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    color = SeparatorColor
                )

                // total
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "TOTAL",
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.ExtraBold,
                        color = Carmesi
                    )
                    Text(
                        text = "$${String.format("%.2f", total)}",
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.ExtraBold,
                        color = Carmesi
                    )
                }

                // separador
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "─".repeat(34),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    color = SeparatorColor
                )

                // mensaje cierre
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "¡Gracias por tu compra!",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Carmesi
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // botones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // guardar (solo visual, no funciona)
            OutlinedButton(
                onClick = { },
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.5.dp, Carmesi)
            ) {
                Text("Guardar", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Carmesi)
            }

            // regresar
            Button(
                onClick = {
                    carritoViewModel.vaciarCarrito()
                    nav.popBackStack()
                },
                modifier = Modifier.weight(1f).height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Carmesi),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Regresar", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
