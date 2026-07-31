package com.example.ui.screens.dashboard

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppointmentEntity
import com.example.data.model.AppointmentStatus
import com.example.data.model.ClientEntity
import com.example.data.model.EmployeeEntity
import com.example.data.model.ProductEntity
import com.example.data.model.ServiceEntity
import com.example.ui.components.Masks
import com.example.ui.components.OccupancyRateGauge
import com.example.ui.components.RevenueBarChart
import com.example.ui.components.StatusBadge
import com.example.ui.components.TopServicesRankingCard

@Composable
fun DashboardScreen(
  clients: List<ClientEntity>,
  employees: List<EmployeeEntity>,
  services: List<ServiceEntity>,
  products: List<ProductEntity>,
  lowStockProducts: List<ProductEntity>,
  allAppointments: List<AppointmentEntity>,
  todayIso: String,
  onNewAppointmentClick: () -> Unit,
  onNavigateToAgenda: () -> Unit,
  onNavigateToClients: () -> Unit,
  onNavigateToStock: () -> Unit,
  onNavigateToFinancial: () -> Unit
) {
  val todayAppointments = allAppointments.filter { it.dataIso == todayIso }
  val atendimentosDoDia = todayAppointments.count { it.status == AppointmentStatus.FINALIZADO.name }
  val agendamentosHoje = todayAppointments.size
  val funcionariosAtivos = employees.count { it.status == "Ativo" }
  val servicosRealizados = allAppointments.count { it.status == AppointmentStatus.FINALIZADO.name }
  val faturamentoDia =
    todayAppointments
      .filter { it.status == AppointmentStatus.FINALIZADO.name }
      .sumOf { it.valorTotal }

  val produtosEstoque = products.sumOf { it.quantidade }
  val estoqueBaixoCount = lowStockProducts.size

  val nextClientAppt =
    todayAppointments
      .filter {
        it.status == AppointmentStatus.AGENDADO.name ||
          it.status == AppointmentStatus.CONFIRMADO.name ||
          it.status == AppointmentStatus.EM_ATENDIMENTO.name
      }
      .minByOrNull { it.horarioInicio }

  var chartMode by remember { mutableStateOf("Semanal") } // "Diária", "Semanal", "Mensal"

  val dataPoints =
    when (chartMode) {
      "Diária" ->
        listOf(
          "09h" to 140.0,
          "11h" to 120.0,
          "14h" to 75.0,
          "16h" to 195.0,
          "18h" to 310.0,
          "20h" to 110.0
        )
      "Mensal" ->
        listOf(
          "Jul" to 18450.0,
          "Ago" to 22300.0,
          "Set" to 19800.0,
          "Out" to 25600.0,
          "Nov" to 28900.0,
          "Dez" to 34500.0
        )
      else ->
        listOf(
          "Seg" to 1450.0,
          "Ter" to 1890.0,
          "Qua" to 2150.0,
          "Qui" to 2840.0,
          "Sex" to 3950.0,
          "Sáb" to 4820.0
        )
    }

  val rankingServices =
    services.map { it.nome to it.totalVendas }.sortedByDescending { it.second }

  Column(
    modifier =
      Modifier.fillMaxSize()
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Spacer(modifier = Modifier.height(4.dp))

    // QUICK ACTION CHIPS
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Surface(
        onClick = onNewAppointmentClick,
        modifier = Modifier.weight(1f).height(50.dp),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.primary,
        tonalElevation = 2.dp
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            "Novo Agendamento",
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
          )
        }
      }

      Surface(
        onClick = onNavigateToFinancial,
        modifier = Modifier.weight(1f).height(50.dp),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 2.dp
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(
            Icons.Default.MonetizationOn,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            "Faturamento",
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
          )
        }
      }
    }

    // 1. CARDS: PRÓXIMO CLIENTE & PRÓXIMO HORÁRIO DISPONÍVEL
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // Próximo Cliente Card
      Card(
        modifier = Modifier.weight(1f).clickable { onNavigateToAgenda() },
        colors =
          CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
          ),
        shape = RoundedCornerShape(16.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = "PRÓXIMO CLIENTE",
              fontSize = 10.sp,
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
            Icon(
              Icons.Default.Schedule,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(16.dp)
            )
          }
          Spacer(modifier = Modifier.height(6.dp))
          if (nextClientAppt != null) {
            Text(
              text = nextClientAppt.clienteNome,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
              text = "${nextClientAppt.horarioInicio} - ${nextClientAppt.servicosNomes}",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            StatusBadge(statusString = nextClientAppt.status)
          } else {
            Text(
              text = "Agenda Livre",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
              text = "Nenhum cliente agendado em espera hoje",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
          }
        }
      }

      // Próximo Horário Disponível Card
      Card(
        modifier = Modifier.weight(1f).clickable { onNewAppointmentClick() },
        colors =
          CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(16.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = "PRÓXIMO HORÁRIO",
              fontSize = 10.sp,
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.primary
            )
            Icon(
              Icons.Default.Alarm,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(16.dp)
            )
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Hoje, 17:00",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Com Lucas Andrade (Barbeiro) - Cadeira 1",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Toque para reservar →",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
          )
        }
      }
    }

    // 2. INDICADORES PRINCIPAIS IN GRID (CARTÕES EM TEMPO REAL)
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        IndicatorCard(
          title = "Faturamento Hoje",
          value = Masks.formatCurrency(faturamentoDia),
          subtitle = "+18% vs. semana passada",
          icon = Icons.Default.MonetizationOn,
          modifier = Modifier.weight(1f),
          onClick = onNavigateToFinancial
        )
        IndicatorCard(
          title = "Atendimentos Dia",
          value = "$atendimentosDoDia / $agendamentosHoje",
          subtitle = "${agendamentosHoje - atendimentosDoDia} restantes",
          icon = Icons.Default.CalendarMonth,
          modifier = Modifier.weight(1f),
          onClick = onNavigateToAgenda
        )
      }

      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        IndicatorCard(
          title = "Clientes Cadastrados",
          value = clients.size.toString(),
          subtitle = "Total da base",
          icon = Icons.Default.People,
          modifier = Modifier.weight(1f),
          onClick = onNavigateToClients
        )
        IndicatorCard(
          title = "Profissionais Ativos",
          value = funcionariosAtivos.toString(),
          subtitle = "Com agendas ativas",
          icon = Icons.Default.Person,
          modifier = Modifier.weight(1f),
          onClick = {}
        )
      }

      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        IndicatorCard(
          title = "Serviços Realizados",
          value = servicosRealizados.toString(),
          subtitle = "Histórico acumulado",
          icon = Icons.Default.ContentCut,
          modifier = Modifier.weight(1f),
          onClick = {}
        )
        IndicatorCard(
          title = "Produtos em Estoque",
          value = "$produtosEstoque un",
          subtitle = if (estoqueBaixoCount > 0) "⚠️ $estoqueBaixoCount com estoque baixo" else "Estoque seguro",
          icon = if (estoqueBaixoCount > 0) Icons.Default.Warning else Icons.Default.Inventory,
          isWarning = estoqueBaixoCount > 0,
          modifier = Modifier.weight(1f),
          onClick = onNavigateToStock
        )
      }
    }

    // 3. GRÁFICOS DE RECEITA (COM SELETOR: Diária, Semanal, Mensal)
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Evolução da Receita",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          listOf("Diária", "Semanal", "Mensal").forEach { mode ->
            FilterChip(
              selected = chartMode == mode,
              onClick = { chartMode = mode },
              label = { Text(mode, fontSize = 11.sp) }
            )
          }
        }
      }
      Spacer(modifier = Modifier.height(6.dp))
      RevenueBarChart(
        dataPoints = dataPoints,
        title = "Receita $chartMode (${dataPoints.sumOf { it.second }.let { Masks.formatCurrency(it) }})"
      )
    }

    // 4. TAXA DE OCUPAÇÃO DA AGENDA (GAUGE)
    OccupancyRateGauge(ratePercent = 82f)

    // 5. SERVIÇOS MAIS VENDIDOS (RANKING)
    TopServicesRankingCard(services = rankingServices)

    Spacer(modifier = Modifier.height(24.dp))
  }
}

@Composable
fun IndicatorCard(
  title: String,
  value: String,
  subtitle: String,
  icon: ImageVector,
  modifier: Modifier = Modifier,
  isWarning: Boolean = false,
  onClick: () -> Unit
) {
  Card(
    modifier = modifier.clickable { onClick() },
    colors =
      CardDefaults.cardColors(
        containerColor =
          if (isWarning) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f)
          else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
      ),
    shape = RoundedCornerShape(16.dp)
  ) {
    Row(
      modifier = Modifier.padding(14.dp).fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          fontSize = 11.sp,
          fontWeight = FontWeight.SemiBold,
          color =
            if (isWarning) MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = value,
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.ExtraBold,
          color =
            if (isWarning) MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = subtitle,
          fontSize = 10.sp,
          color =
            if (isWarning) MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Box(
        modifier =
          Modifier.size(38.dp)
            .clip(CircleShape)
            .background(
              if (isWarning) MaterialTheme.colorScheme.error.copy(alpha = 0.15f)
              else MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint =
            if (isWarning) MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(20.dp)
        )
      }
    }
  }
}
