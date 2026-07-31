package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ClientEntity
import com.example.data.model.EmployeeEntity
import com.example.data.model.ServiceEntity

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewAppointmentModal(
  clients: List<ClientEntity>,
  employees: List<EmployeeEntity>,
  services: List<ServiceEntity>,
  initialDateIso: String,
  onDismiss: () -> Unit,
  onConfirm: (
    client: ClientEntity,
    employee: EmployeeEntity,
    selectedServices: List<ServiceEntity>,
    dataIso: String,
    horario: String,
    formaPagamento: String,
    observacoes: String,
    valorTotal: Double
  ) -> Unit
) {
  var searchClientText by remember { mutableStateOf("") }
  var isNewClientMode by remember { mutableStateOf(false) }
  var newClientNome by remember { mutableStateOf("") }
  var newClientTelefone by remember { mutableStateOf("") }

  var selectedClient by remember { mutableStateOf<ClientEntity?>(clients.firstOrNull()) }
  var selectedEmployee by remember { mutableStateOf<EmployeeEntity?>(employees.firstOrNull()) }
  val selectedServices = remember { mutableStateListOf<ServiceEntity>() }
  if (selectedServices.isEmpty() && services.isNotEmpty()) {
    selectedServices.add(services.first())
  }

  var selectedDateIso by remember { mutableStateOf(initialDateIso) }
  var selectedHorario by remember { mutableStateOf("14:00") }
  var selectedPagamento by remember { mutableStateOf("PIX") }
  var observacoes by remember { mutableStateOf("") }

  val computedTotal = selectedServices.sumOf { it.preco }
  var customTotalText by remember(computedTotal) { mutableStateOf(computedTotal.toString()) }

  val timeSlots =
    listOf(
      "08:00",
      "08:45",
      "09:30",
      "10:15",
      "11:00",
      "13:00",
      "14:00",
      "14:45",
      "15:30",
      "16:15",
      "17:00",
      "18:00",
      "18:45"
    )
  val paymentMethods =
    listOf("PIX", "Dinheiro", "Cartão Crédito", "Cartão Débito", "Transferência", "Voucher")

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier =
        Modifier.fillMaxWidth(0.95f)
          .height(680.dp)
          .clip(RoundedCornerShape(24.dp))
          .background(MaterialTheme.colorScheme.surface),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 6.dp
    ) {
      Column(
        modifier =
          Modifier.padding(20.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // HEADER
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "+ Novo Agendamento",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
          )
          TextButton(onClick = onDismiss) {
            Text("Fechar", color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }

        // 1. CLIENT SELECTION / NEW CLIENT
        Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          shape = RoundedCornerShape(16.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "1. Cliente",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
              )
              TextButton(onClick = { isNewClientMode = !isNewClientMode }) {
                Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = if (isNewClientMode) "Selecionar Existente" else "Cadastrar Novo",
                  fontSize = 12.sp
                )
              }
            }

            if (isNewClientMode) {
              OutlinedTextField(
                value = newClientNome,
                onValueChange = { newClientNome = it },
                label = { Text("Nome do Novo Cliente") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
              )
              OutlinedTextField(
                value = newClientTelefone,
                onValueChange = { newClientTelefone = Masks.formatPhone(it) },
                label = { Text("Telefone / WhatsApp") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
              )
            } else {
              OutlinedTextField(
                value = searchClientText,
                onValueChange = { searchClientText = it },
                label = { Text("Pesquisar Cliente Existente...") },
                trailingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
              )

              // Quick Client list chips
              val filteredClients =
                clients.filter {
                  searchClientText.isBlank() ||
                    it.nome.contains(searchClientText, ignoreCase = true) ||
                    it.telefone.contains(searchClientText)
                }
              FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                filteredClients.take(6).forEach { cli ->
                  FilterChip(
                    selected = selectedClient?.id == cli.id,
                    onClick = { selectedClient = cli },
                    label = { Text("${cli.nome} (${cli.telefone})") }
                  )
                }
              }
            }
          }
        }

        // 2. EMPLOYEE SELECTION
        Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          shape = RoundedCornerShape(16.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
              text = "2. Selecionar Profissional",
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )
            Row(
              modifier = Modifier.horizontalScroll(rememberScrollState()),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              employees.forEach { emp ->
                val isSelected = selectedEmployee?.id == emp.id
                Box(
                  modifier =
                    Modifier.clip(RoundedCornerShape(12.dp))
                      .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surface
                      )
                      .clickable { selectedEmployee = emp }
                      .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                  Text(
                    text = "${emp.nome} (${emp.funcao})",
                    color =
                      if (isSelected) MaterialTheme.colorScheme.onPrimary
                      else MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                  )
                }
              }
            }
          }
        }

        // 3. SERVICE SELECTION (MULTI-SELECT)
        Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          shape = RoundedCornerShape(16.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
              text = "3. Selecionar Serviços (Múltiplos)",
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )
            FlowRow(
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              services.forEach { svc ->
                val isSelected = selectedServices.any { it.id == svc.id }
                FilterChip(
                  selected = isSelected,
                  onClick = {
                    if (isSelected) {
                      if (selectedServices.size > 1) {
                        selectedServices.removeAll { it.id == svc.id }
                      }
                    } else {
                      selectedServices.add(svc)
                    }
                  },
                  label = {
                    Text("${svc.nome} - ${Masks.formatCurrency(svc.preco)}")
                  },
                  leadingIcon =
                    if (isSelected) {
                      { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    } else null
                )
              }
            }
          }
        }

        // 4. DATE & TIME
        Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          shape = RoundedCornerShape(16.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
              text = "4. Data & Horário",
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )
            OutlinedTextField(
              value = selectedDateIso,
              onValueChange = { selectedDateIso = it },
              label = { Text("Data (AAAA-MM-DD)") },
              modifier = Modifier.fillMaxWidth(),
              singleLine = true
            )
            Text(
              text = "Horários Livres do Dia:",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            FlowRow(
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              timeSlots.forEach { slot ->
                val isSelected = selectedHorario == slot
                FilterChip(
                  selected = isSelected,
                  onClick = { selectedHorario = slot },
                  label = { Text(slot) }
                )
              }
            }
          }
        }

        // 5. PAYMENT METHOD & OBSERVATIONS & TOTAL
        Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          shape = RoundedCornerShape(16.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
              text = "5. Pagamento & Valor",
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )

            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              paymentMethods.forEach { method ->
                FilterChip(
                  selected = selectedPagamento == method,
                  onClick = { selectedPagamento = method },
                  label = { Text(method) }
                )
              }
            }

            OutlinedTextField(
              value = observacoes,
              onValueChange = { observacoes = it },
              label = { Text("Observações do Agendamento") },
              modifier = Modifier.fillMaxWidth(),
              singleLine = true
            )

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Valor Total Calculado:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = Masks.formatCurrency(computedTotal),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }
        }

        // BOTTOM ACTION BUTTONS
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          TextButton(
            onClick = onDismiss,
            modifier = Modifier.weight(1f).height(50.dp)
          ) {
            Text("Cancelar")
          }

          Button(
            onClick = {
              val targetClient =
                if (isNewClientMode && newClientNome.isNotBlank()) {
                  ClientEntity(
                    id = 0,
                    nome = newClientNome,
                    telefone = newClientTelefone.ifBlank { "(11) 99999-0000" }
                  )
                } else {
                  selectedClient
                    ?: ClientEntity(
                      id = 0,
                      nome = "Cliente Avulso",
                      telefone = "(11) 99999-0000"
                    )
                }

              val targetEmployee =
                selectedEmployee
                  ?: EmployeeEntity(id = 1, nome = "Lucas Andrade", funcao = "Barbeiro")

              onConfirm(
                targetClient,
                targetEmployee,
                selectedServices.ifEmpty {
                  listOf(
                    ServiceEntity(
                      id = 1,
                      nome = "Atendimento Padrão",
                      categoria = "Geral",
                      preco = 50.0
                    )
                  )
                },
                selectedDateIso,
                selectedHorario,
                selectedPagamento,
                observacoes,
                computedTotal
              )
            },
            modifier = Modifier.weight(2f).height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors =
              ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
              )
          ) {
            Text(
              text = "CONFIRMAR AGENDAMENTO",
              fontWeight = FontWeight.ExtraBold,
              fontSize = 13.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}
