package com.example.fblogin.ui.cliente

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fblogin.ui.admin.charts.AreaLineChart
import com.example.fblogin.ui.admin.charts.DonutChart
import com.example.fblogin.ui.admin.charts.VerticalBarChart
import com.example.fblogin.ui.components.SectionTitle
import com.example.fblogin.ui.components.StatCard
import com.example.fblogin.ui.theme.Carmesi
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

    val historialGastos by dashboardVm.historialGastos.collectAsState()
    val categoriasFavoritas by dashboardVm.categoriasFavoritas.collectAsState()
    val productosFavoritos by dashboardVm.productosFavoritos.collectAsState()
    val estadoPedidos by dashboardVm.estadoMisPedidos.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
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
            modifier = Modifier
                .padding(16.dp),
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

            SectionTitle("Resumen de Compras")

            // KPIs
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

            AnimatedContent(
                targetState = true,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "charts_cliente"
            ) { _ ->
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    // 1. Historial de compras
                    Column {
                        SectionTitle("Mis compras (monto por mes)")
                        AreaLineChart(historialGastos)
                    }

                    // 2. Categorías más compradas
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        SectionTitle("Mis categorías favoritas", Modifier.fillMaxWidth())
                        DonutChart(categoriasFavoritas, Modifier.height(200.dp))
                    }

                    // 3. Productos favoritos
                    Column {
                        SectionTitle("Productos favoritos (Top 5)")
                        VerticalBarChart(productosFavoritos)
                    }

                    // 4. Estado de pedidos
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        SectionTitle("Estado de mis pedidos", Modifier.fillMaxWidth())
                        DonutChart(estadoPedidos, Modifier.height(200.dp))
                    }
                }
            }
            
            Spacer(Modifier.height(24.dp))
        }
    }
}
