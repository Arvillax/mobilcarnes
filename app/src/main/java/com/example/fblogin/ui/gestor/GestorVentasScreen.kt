package com.example.fblogin.ui.gestor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fblogin.viewmodel.AuthViewModel
import com.example.fblogin.viewmodel.GestorVentasViewModel

private val Vino = Color(0xFF610000)
private val Carmesi = Color(0xFF9C0720)
private val Crimson = Color(0xFFDC143C)
private val Coral = Color(0xFFF1666D)
private val Rosa = Color(0xFFFF9EA2)
private val White = Color(0xFFFFFFFF)

// pantalla ventas
@Composable
fun GestorVentasScreen(nav: NavController, vm: AuthViewModel, gestorVentasVm: GestorVentasViewModel) {
    val ventas by gestorVentasVm.ventas.collectAsState()
    val totalVentas by gestorVentasVm.totalVentas.collectAsState()
    val cargando by gestorVentasVm.cargando.collectAsState()

    LaunchedEffect(Unit) {
        gestorVentasVm.recargar()
    }

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
                text = "Ventas del Día",
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

            if (cargando) {
                // loading
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Carmesi)
                }
            } else {
                // total ventas
                Text(
                    text = "Total: $${String.format("%,.0f", totalVentas)}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Crimson,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (ventas.isEmpty()) {
                    // sin ventas hoy
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay ventas registradas hoy",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                    }
                } else {
                    // lista ventas
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(ventas) { venta ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Rosa.copy(alpha = 0.3f)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    // id y fecha
                                    Text(
                                        text = "Venta #${venta.id.take(8)}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Vino
                                    )
                                    Text(
                                        text = venta.fecha,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Coral
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // cliente
                                    Text(
                                        text = venta.clienteEmail,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Carmesi
                                    )

                                    // items
                                    venta.items.forEach { item ->
                                        val nombre = item["nombre"] as? String ?: ""
                                        val cantidad = (item["cantidad"] as? Number)?.toDouble() ?: 0.0
                                        Text(
                                            text = "$nombre × ${String.format("%.1f", cantidad)} kg",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Coral
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // total
                                    Text(
                                        text = "$${String.format("%.2f", venta.total)}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Crimson
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
