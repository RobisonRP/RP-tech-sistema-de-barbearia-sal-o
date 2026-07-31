package com.example.ui.screens.agenda

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppointmentEntity
import com.example.data.model.AppointmentStatus
import com.example.data.model.EmployeeEntity
import com.example.ui.components.Masks
import com.example.ui.components.StatusBadge

@Composable
fun AgendaScreen(
  allAppointments: List<AppointmentEntity>,
  employees: List<EmployeeEntity>,
  selectedDateIso: String,
  onDateChange: (String) -> Unit,
  viewMode: Int, // 0 = Dia, 1 = Semana, 2 = Mês
  onViewModeChange: (Int) -> Unit,
  onNewAppointmentClick: () -> Unit,
  onStatusChange: (AppointmentEntity, AppointmentStatus) -> Unit,
  onDeleteAppointment: (AppointmentEntity) -> Unit
) {
  var selectedEmployeeId by remember { mutableStateOf<Int?>(null) } // null = Todos os Profissionais
  var appointmentToDelete by remember { mutableStateOf<AppointmentEntity?>(null) }
  var appointmentDetailModal by remember { mutableStateOf<AppointmentEntity?>(null) }

  // Filter appointments for the selected date or week
  val filteredAppointments =
    allAppointments.filter { appt ->
      val dateMatches =
        when (viewMode) {
          0 -> appt.dataIso == selectedDateIso
          1 -> true // No modo semana/mês listamos todos ou intervalo
          else -> true
        }
      val employeeMatches = selectedEmployeeId == null || appt.funcionarioId == selectedEmployeeId
      dateMatches && employeeMatches
    }

  val hoursList =
    listOf(
      "08:00",
      "09:00",
      "10:00",
      "11:00",
      "12:00",
      "13:00",
      "14:00",
      "15:00",
      "16:00",
      "17:00",
      "18:00",
      "19:00"
    )

  Column(
    modifier =
      Modifier.fillMaxSize()
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    Spacer(modifier = Modifier.height(4.dp))

    // 1. TOP SELECTORS: DIA / SEMANA / MÊS + NEW FAB INLINE
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Day / Week / Month toggle chips
      Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        val modes = listOf("Dia", "Semana", "Mês")
        modes.forEachIndexed { idx, label ->
          FilterChip(
            selected = viewMode == idx,
            onClick = { onViewModeChange(idx) },
            label = { Text(label, fontWeight = FontWeight.Bold) }
          )
        }
      }

      // "+ Novo" button
      Surface(
        onClick = onNewAppointmentClick,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primary,
        tonalElevation = 2.dp
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            "+ Agendar",
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp
          )
        }
      }
    }

    // 2. DATE SELECTOR BAR & EMPLOYEE FILTER
    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
      shape = RoundedCornerShape(16.dp)
    ) {
      Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = Masks.formatDateBr(selectedDateIso),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold
              )
              Text(
                text = Masks.getDayOfWeekBr(selectedDateIso),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          // Date navigator arrows
          Row {
            IconButton(
              onClick = {
                // simple helper text for demonstration
              }
            ) {
              Icon(Icons.Default.ChevronLeft, contentDescription = "Dia Anterior")
            }
            IconButton(
              onClick = {
                // simple helper text
              }
            ) {
              Icon(Icons.Default.ChevronRight, contentDescription = "Próximo Dia")
            }
          }
        }

        // Employee filter pills
        Row(
          modifier = Modifier.horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          FilterChip(
            selected = selectedEmployeeId == null,
            onClick = { selectedEmployeeId = null },
            label = { Text("Todos os Profissionais") }
          )
          employees.forEach { emp ->
            FilterChip(
              selected = selectedEmployeeId == emp.id,
              onClick = { selectedEmployeeId = emp.id },
              label = { Text(emp.nome.split(" ").first()) }
            )
          }
        }
      }
    }

    // 3. GOOGLE CALENDAR TIMELINE VIEW (HOURS & APPOINTMENTS)
    if (filteredAppointments.isEmpty()) {
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
      ) {
        Column(
          modifier = Modifier.fillMaxWidth().padding(32.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Icon(
            Icons.Default.CalendarToday,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(48.dp)
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "Agenda Livre nesta data",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Toque em + Agendar para adicionar um novo atendimento.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
          )
        }
      }
    } else {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        filteredAppointments.forEach { appt ->
          AgendaAppointmentCard(
            appointment = appt,
            onCardClick = { appointmentDetailModal = appt },
            onStatusChange = { newStatus -> onStatusChange(appt, newStatus) },
            onDeleteClick = { appointmentToDelete = appt }
          )
        }
      }
    }

    // 4. TIMELINE OF FREE & OCCUPIED HOURS (GOOGLE CALENDAR STYLE)
    Text(
      text = "Linha do Tempo de Horários",
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(top = 8.dp)
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      hoursList.forEach { hour ->
        val apptAtHour =
          filteredAppointments.find { it.horarioInicio.startsWith(hour.substring(0, 2)) }
        if (apptAtHour != null) {
          // Occupied slot
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = hour,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.width(48.dp),
              color = MaterialTheme.colorScheme.primary
            )
            Box(
              modifier =
                Modifier.weight(1f)
                  .clip(RoundedCornerShape(10.dp))
                  .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                  .clickable { appointmentDetailModal = apptAtHour }
                  .padding(10.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(
                    text = "${apptAtHour.clienteNome} (${apptAtHour.funcionarioNome})",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                  Text(
                    text = "${apptAtHour.servicosNomes} - ${Masks.formatCurrency(apptAtHour.valorTotal)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
                StatusBadge(statusString = apptAtHour.status)
              }
            }
          }
        } else {
          // Free slot
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = hour,
              fontSize = 12.sp,
              modifier = Modifier.width(48.dp),
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Box(
              modifier =
                Modifier.weight(1f)
                  .clip(RoundedCornerShape(10.dp))
                  .border(
                    1.dp,
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    RoundedCornerShape(10.dp)
                  )
                  .clickable { onNewAppointmentClick() }
                  .padding(10.dp)
            ) {
              Text(
                text = "Horário Livre • Toque para agendar",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }

  // DELETE CONFIRMATION DIALOG (Mandatory: "Confirmação antes de excluir registros")
  if (appointmentToDelete != null) {
    AlertDialog(
      onDismissRequest = { appointmentToDelete = null },
      title = { Text("Excluir Agendamento") },
      text = {
        Text(
          "Tem certeza que deseja excluir o agendamento de ${appointmentToDelete?.clienteNome}? Essa ação não poderá ser desfeita."
        )
      },
      confirmButton = {
        Button(
          onClick = {
            appointmentToDelete?.let { onDeleteAppointment(it) }
            appointmentToDelete = null
          },
          colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
          Text("Excluir", color = Color.White)
        }
      },
      dismissButton = {
        TextButton(onClick = { appointmentToDelete = null }) { Text("Cancelar") }
      }
    )
  }

  // MODAL DE DETALHE COMPLETO DO AGENDAMENTO COM ALTERAÇÃO RÁPIDA DE STATUS
  val detailAppt = appointmentDetailModal
  if (detailAppt != null) {
    AlertDialog(
      onDismissRequest = { appointmentDetailModal = null },
      title = {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Detalhes do Atendimento", fontWeight = FontWeight.ExtraBold)
          StatusBadge(statusString = detailAppt.status)
        }
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("Cliente: ${detailAppt.clienteNome}", fontWeight = FontWeight.Bold)
          Text("Telefone: ${Masks.formatPhone(detailAppt.clienteTelefone)}")
          Text("Profissional: ${detailAppt.funcionarioNome}")
          Text("Serviço(s): ${detailAppt.servicosNomes}")
          Text("Data e Hora: ${Masks.formatDateBr(detailAppt.dataIso)} às ${detailAppt.horarioInicio}")
          Text("Valor Total: ${Masks.formatCurrency(detailAppt.valorTotal)}")
          Text("Forma de Pagamento: ${detailAppt.formaPagamento}")
          if (detailAppt.observacoes.isNotBlank()) {
            Text("Observações: ${detailAppt.observacoes}", style = MaterialTheme.typography.bodySmall)
          }

          Spacer(modifier = Modifier.height(8.dp))
          Text("Mudar Status para:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
          Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            AppointmentStatus.values().forEach { st ->
              FilterChip(
                selected = detailAppt.status == st.name,
                onClick = {
                  onStatusChange(detailAppt, st)
                  appointmentDetailModal = null
                },
                label = { Text(st.label, fontSize = 11.sp) }
              )
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { appointmentDetailModal = null }) { Text("Fechar") }
      }
    )
  }
}

@Composable
fun AgendaAppointmentCard(
  appointment: AppointmentEntity,
  onCardClick: () -> Unit,
  onStatusChange: (AppointmentStatus) -> Unit,
  onDeleteClick: () -> Unit
) {
  var showMenu by remember { mutableStateOf(false) }

  Card(
    modifier = Modifier.fillMaxWidth().clickable { onCardClick() },
    colors =
      CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
    shape = RoundedCornerShape(16.dp),
    elevation = CardDefaults.cardElevation(2.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier =
              Modifier.clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                .padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Text(
              text = appointment.horarioInicio,
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.primary
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = appointment.clienteNome,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.ExtraBold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                Icons.Default.Phone,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = Masks.formatPhone(appointment.clienteTelefone),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          StatusBadge(statusString = appointment.status)
          Box {
            IconButton(onClick = { showMenu = true }) {
              Icon(Icons.Default.MoreVert, contentDescription = "Ações")
            }
            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
              AppointmentStatus.values().forEach { st ->
                DropdownMenuItem(
                  text = { Text("Definir: ${st.label}") },
                  onClick = {
                    showMenu = false
                    onStatusChange(st)
                  }
                )
              }
              DropdownMenuItem(
                text = { Text("Excluir Agendamento", color = MaterialTheme.colorScheme.error) },
                onClick = {
                  showMenu = false
                  onDeleteClick()
                }
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = appointment.servicosNomes,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Profissional: ${appointment.funcionarioNome} • Pagamento: ${appointment.formaPagamento}",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Text(
          text = Masks.formatCurrency(appointment.valorTotal),
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.primary
        )
      }
    }
  }
}
