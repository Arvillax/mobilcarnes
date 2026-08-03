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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.fblogin.viewmodel.GestorDashboardViewModel

private val Vino = Color(0xFF610000)
private val Carmesi = Color(0xFF9C0720)
private val Crimson = Color(0xFFDC143C)
private val Rosa = Color(0xFFFF9EA2)
private val White = Color(0xFFFFFFFF)
private val GrayLight = Color(0xFFF5F5F5)

// dashboard gestor
@Composable
fun GestorDashboard(nav: NavController, vm: AuthViewModel, gestorDashboardVm: GestorDashboardViewModel) {
    val ventasHoy by gestorDashboardVm.ventasHoy.collectAsState()
    val stockCritico by gestorDashboardVm.stockCritico.collectAsState()
    val pedidosHoy by gestorDashboardVm.pedidosHoy.collectAsState()

    LaunchedEffect(Unit) {
        gestorDashboardVm.recargar()
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Gestor",
                    style = MaterialTheme.typography.headlineLarge,
                    color = White,
                    fontWeight = FontWeight.Bold
                )
                // Boton acceso rapido reportes
                TextButton(onClick = { nav.navigate("gestor/reportes") }) {
                    Text("\uD83D\uDCCA Reportes", color = Rosa, fontWeight = FontWeight.Bold)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // cards estadisticas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    value = ventasHoy,
                    label = "Ventas hoy",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = stockCritico,
                    label = "Stock bajo",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = pedidosHoy,
                    label = "Pedidos",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // boton gestionar stock
            OutlinedButton(
                onClick = { nav.navigate("gestor/stock") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Carmesi
                )
            ) {
                Text(
                    text = "Gestionar Stock",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            // boton ver ventas
            OutlinedButton(
                onClick = { nav.navigate("gestor/ventas") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Carmesi
                )
            ) {
                Text(
                    text = "Ver Ventas",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            // boton reportes operativos
            androidx.compose.material3.Button(
                onClick = { nav.navigate("gestor/reportes") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Carmesi)
            ) {
                Text(
                    text = "Ver Reportes Operativos",
                    style = MaterialTheme.typography.titleMedium,
                    color = White,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // cerrar sesion
            TextButton(
                onClick = {
                    vm.logout()
                    nav.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Cerrar sesión",
                    color = Crimson,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

// card estadistica
@Composable
private fun StatCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Rosa)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Vino
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = Carmesi
            )
        }
    }
}
