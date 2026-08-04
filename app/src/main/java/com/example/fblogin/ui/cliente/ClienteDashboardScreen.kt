package com.example.fblogin.ui.cliente

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fblogin.data.Venta
import com.example.fblogin.ui.components.SectionTitle
import com.example.fblogin.ui.components.StatCard
import com.example.fblogin.ui.theme.Carmesi
import com.example.fblogin.ui.theme.Crimson
import com.example.fblogin.ui.theme.GrayLight
import com.example.fblogin.ui.theme.Vino
import com.example.fblogin.viewmodel.AuthViewModel
import com.example.fblogin.viewmodel.ClienteDashboardViewModel
import com.example.fblogin.viewmodel.UserRole

@Composable
fun ClienteDashboardScreen(
    nav: NavController,
    authVm: AuthViewModel,
    dashboardVm: ClienteDashboardViewModel
) {
    // seguridad
    val role by authVm.role.collectAsState()
    if (role != UserRole.CLIENTE && role != UserRole.ADMIN) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Acceso no autorizado", color = Color.Gray)
        }
        return
    }

    val totalCompras by dashboardVm.totalCompras.collectAsState()
    val totalGastado by dashboardVm.totalGastado.collectAsState()
    val productosComprados by dashboardVm.productosComprados.collectAsState()
    val ultimaCompra by dashboardVm.ultimaCompra.collectAsState()
    val ventas by dashboardVm.ventas.collectAsState()
    val cargando by dashboardVm.cargando.collectAsState()

    LaunchedEffect(Unit) {
        dashboardVm.recargar()
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(Vino, Carmesi)))
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Text(
                text = "Mi Actividad",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Volver
            OutlinedButton(
                onClick = { nav.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("← Volver", fontSize = 16.sp, color = Carmesi)
            }

            if (cargando) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Carmesi)
                }
            } else {
                // KPIs reales
                SectionTitle("Resumen de Compras")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(totalCompras, "Total Compras", Modifier.weight(1f))
                    StatCard(totalGastado, "Total Gastado", Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(productosComprados, "Prod. Comprados", Modifier.weight(1f))
                    StatCard(ultimaCompra, "Última Compra", Modifier.weight(1f))
                }

                Spacer(Modifier.height(8.dp))

                // Historial de compras
                SectionTitle("Mis Compras")

                if (ventas.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No tienes compras registradas",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn {
                        items(ventas) { venta ->
                            VentaAccordion(venta)
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

// acordeon de venta
@Composable
private fun VentaAccordion(venta: Venta) {
    var expandido by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { expandido = !expandido },
        colors = CardDefaults.cardColors(containerColor = GrayLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // fila principal: fecha + total + flecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Pedido #${venta.id.take(8)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Vino
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = venta.fecha,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$${String.format("%.2f", venta.total)}",
                        color = Crimson,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (expandido) "▲" else "▼",
                        color = Carmesi,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // items desplegables
            AnimatedVisibility(visible = expandido) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(color = Color.LightGray)
                    Spacer(modifier = Modifier.height(8.dp))
                    venta.items.forEach { item ->
                        val nombre = item["nombre"] as? String ?: ""
                        val cantidad = (item["cantidad"] as? Number)?.toDouble() ?: 0.0
                        val precioKg = (item["precioKg"] as? Number)?.toDouble() ?: 0.0
                        val subtotal = cantidad * precioKg
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = nombre,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.DarkGray,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "${String.format("%.1f", cantidad)} kg",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Text(
                                text = "  $${String.format("%.2f", subtotal)}",
                                style = MaterialTheme.typography.bodySmall,
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
