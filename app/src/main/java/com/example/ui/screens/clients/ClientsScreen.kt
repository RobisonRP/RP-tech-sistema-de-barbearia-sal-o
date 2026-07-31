package com.example.ui.screens.clients

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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AppointmentEntity
import com.example.data.model.ClientEntity
import com.example.ui.components.Masks
import com.example.ui.components.StatusBadge

@Composable
fun ClientsScreen(
  clients: List<ClientEntity>,
  allAppointments: List<AppointmentEntity>,
  searchQuery: String,
  onSearchQueryChange: (String) -> Unit,
  onSaveClient: (ClientEntity) -> Unit,
  onDeleteClient: (ClientEntity) -> Unit,
  onToggleFavorite: (ClientEntity) -> Unit,
  onScheduleForClient: (ClientEntity) -> Unit
) {
  var isAddModalOpen by remember { mutableStateOf(false) }
  var clientToEdit by remember { mutableStateOf<ClientEntity?>(null) }
  var selectedClientForDetail by remember { mutableStateOf<ClientEntity?>(null) }
  var clientToDelete by remember { mutableStateOf<ClientEntity?>(null) }

  // Instant filtering by Nome, Telefone or CPF
  val filteredClients =
    clients.filter {
      searchQuery.isBlank() ||
        it.nome.contains(searchQuery, ignoreCase = true) ||
        it.telefone.contains(searchQuery) ||
        it.cpf.contains(searchQuery)
    }

  Column(
    modifier =
      Modifier.fillMaxSize()
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    Spacer(modifier = Modifier.height(4.dp))

    // 1. SEARCH BAR & "+ NOVO CLIENTE" BUTTON
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        label = { Text("Pesquisar Nome, Telefone ou CPF...") },
        trailingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        modifier = Modifier.weight(1f),
        singleLine = true
      )

      Surface(
        onClick = {
          clientToEdit = null
          isAddModalOpen = true
        },
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.primary,
        tonalElevation = 2.dp,
        modifier = Modifier.height(56.dp)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
          Spacer(modifier = Modifier.width(6.dp))
          Text("Novo", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.ExtraBold)
        }
      }
    }

    // 2. SUMMARY METRICS
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
      Card(
        modifier = Modifier.weight(1f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(14.dp)
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Text("TOTAL CLIENTES", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("${clients.size}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
        }
      }

      Card(
        modifier = Modifier.weight(1f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(14.dp)
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Text("FAVORITOS VIP", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("${clients.count { it.isFavorite }}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
        }
      }
    }

    // 3. CLIENTS LIST
    if (filteredClients.isEmpty()) {
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
      ) {
        Column(
          modifier = Modifier.fillMaxWidth().padding(32.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
          Spacer(modifier = Modifier.height(12.dp))
          Text("Nenhum cliente encontrado", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
          Text("Verifique o termo de pesquisa ou cadastre um novo cliente.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        }
      }
    } else {
      filteredClients.forEach { cli ->
        ClientCard(
          client = cli,
          onCardClick = { selectedClientForDetail = cli },
          onScheduleClick = { onScheduleForClient(cli) },
          onEditClick = {
            clientToEdit = cli
            isAddModalOpen = true
          },
          onDeleteClick = { clientToDelete = cli },
          onToggleFavorite = { onToggleFavorite(cli) }
        )
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }

  // CREATE / EDIT CLIENT MODAL DIALOG
  if (isAddModalOpen) {
    ClientFormDialog(
      initialClient = clientToEdit,
      onDismiss = {
        isAddModalOpen = false
        clientToEdit = null
      },
      onSave = { savedClient ->
        onSaveClient(savedClient)
        isAddModalOpen = false
        clientToEdit = null
      }
    )
  }

  // CLIENT DETAIL HISTÓRICO COMPLETO MODAL
  val targetDetail = selectedClientForDetail
  if (targetDetail != null) {
    val clientAppts = allAppointments.filter { it.clienteId == targetDetail.id }
    ClientDetailDialog(
      client = targetDetail,
      appointments = clientAppts,
      onDismiss = { selectedClientForDetail = null },
      onScheduleClick = {
        selectedClientForDetail = null
        onScheduleForClient(targetDetail)
      }
    )
  }

  // DELETE CONFIRMATION
  if (clientToDelete != null) {
    AlertDialog(
      onDismissRequest = { clientToDelete = null },
      title = { Text("Excluir Cliente") },
      text = { Text("Deseja realmente excluir ${clientToDelete?.nome}? Todos os registros serão removidos do banco local.") },
      confirmButton = {
        Button(
          onClick = {
            clientToDelete?.let { onDeleteClient(it) }
            clientToDelete = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
          Text("Excluir", color = Color.White)
        }
      },
      dismissButton = {
        TextButton(onClick = { clientToDelete = null }) { Text("Cancelar") }
      }
    )
  }
}

@Composable
fun ClientCard(
  client: ClientEntity,
  onCardClick: () -> Unit,
  onScheduleClick: () -> Unit,
  onEditClick: () -> Unit,
  onDeleteClick: () -> Unit,
  onToggleFavorite: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth().clickable { onCardClick() },
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
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
              Modifier.size(46.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = client.nome.take(2).uppercase(),
              color = MaterialTheme.colorScheme.onPrimary,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 16.sp
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = client.nome,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = Masks.formatPhone(client.telefone),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = onToggleFavorite) {
            Icon(
              imageVector = if (client.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
              contentDescription = "Favoritar",
              tint = if (client.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          IconButton(onClick = onEditClick) {
            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.onSurfaceVariant)
          }
          IconButton(onClick = onDeleteClick) {
            Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = MaterialTheme.colorScheme.error)
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Visitas: ${client.totalVisitas} • Ticket Médio: ${Masks.formatCurrency(client.ticketMedio)}",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          if (client.ultimaVisita.isNotBlank()) {
            Text(
              text = "Última visita: ${Masks.formatDateBr(client.ultimaVisita)}",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Button(
          onClick = onScheduleClick,
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
          contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Agendar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun ClientFormDialog(
  initialClient: ClientEntity?,
  onDismiss: () -> Unit,
  onSave: (ClientEntity) -> Unit
) {
  var nome by remember { mutableStateOf(initialClient?.nome ?: "") }
  var telefone by remember { mutableStateOf(initialClient?.telefone ?: "") }
  var whatsapp by remember { mutableStateOf(initialClient?.whatsapp ?: "") }
  var email by remember { mutableStateOf(initialClient?.email ?: "") }
  var cpf by remember { mutableStateOf(initialClient?.cpf ?: "") }
  var nascimento by remember { mutableStateOf(initialClient?.nascimento ?: "") }
  var endereco by remember { mutableStateOf(initialClient?.endereco ?: "") }
  var observacoes by remember { mutableStateOf(initialClient?.observacoes ?: "") }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier.fillMaxWidth(0.92f).height(620.dp).clip(RoundedCornerShape(24.dp)),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 6.dp
    ) {
      Column(
        modifier = Modifier.padding(20.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Text(
          text = if (initialClient == null) "Novo Cliente" else "Editar Cliente",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.ExtraBold
        )

        OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome Completo *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = telefone, onValueChange = { telefone = Masks.formatPhone(it) }, label = { Text("Telefone / Celular *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = whatsapp, onValueChange = { whatsapp = Masks.formatPhone(it) }, label = { Text("WhatsApp") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("E-mail") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = cpf, onValueChange = { cpf = Masks.formatCpf(it) }, label = { Text("CPF") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = nascimento, onValueChange = { nascimento = it }, label = { Text("Data de Nascimento (AAAA-MM-DD)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = endereco, onValueChange = { endereco = it }, label = { Text("Endereço Completo") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = observacoes, onValueChange = { observacoes = it }, label = { Text("Observações Gerais") }, modifier = Modifier.fillMaxWidth(), singleLine = false)

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
          TextButton(onClick = onDismiss, modifier = Modifier.weight(1f).height(50.dp)) {
            Text("Cancelar")
          }
          Button(
            onClick = {
              if (nome.isNotBlank()) {
                val toSave =
                  initialClient?.copy(
                    nome = nome,
                    telefone = telefone,
                    whatsapp = whatsapp,
                    email = email,
                    cpf = cpf,
                    nascimento = nascimento,
                    endereco = endereco,
                    observacoes = observacoes
                  )
                    ?: ClientEntity(
                      nome = nome,
                      telefone = telefone.ifBlank { "(11) 99999-0000" },
                      whatsapp = whatsapp,
                      email = email,
                      cpf = cpf,
                      nascimento = nascimento,
                      endereco = endereco,
                      observacoes = observacoes
                    )
                onSave(toSave)
              }
            },
            modifier = Modifier.weight(2f).height(50.dp),
            shape = RoundedCornerShape(14.dp)
          ) {
            Text("SALVAR CLIENTE", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

@Composable
fun ClientDetailDialog(
  client: ClientEntity,
  appointments: List<AppointmentEntity>,
  onDismiss: () -> Unit,
  onScheduleClick: () -> Unit
) {
  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier.fillMaxWidth(0.94f).height(640.dp).clip(RoundedCornerShape(24.dp)),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 6.dp
    ) {
      Column(
        modifier = Modifier.padding(20.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Dossiê & Histórico", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
          TextButton(onClick = onDismiss) { Text("Fechar") }
        }

        // METRICS HEADER
        Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          shape = RoundedCornerShape(16.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(client.nome, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
            Text("Tel: ${Masks.formatPhone(client.telefone)} • CPF: ${client.cpf.ifBlank { "Não informado" }}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (client.endereco.isNotBlank()) {
              Text("Endereço: ${client.endereco}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Column {
                Text("TOTAL GASTO", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(Masks.formatCurrency(client.totalGasto), fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
              }
              Column {
                Text("TOTAL VISITAS", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text("${client.totalVisitas} visitas", fontWeight = FontWeight.ExtraBold)
              }
              Column {
                Text("TICKET MÉDIO", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(Masks.formatCurrency(client.ticketMedio), fontWeight = FontWeight.ExtraBold)
              }
            }
          }
        }

        Button(
          onClick = onScheduleClick,
          modifier = Modifier.fillMaxWidth().height(50.dp),
          shape = RoundedCornerShape(14.dp)
        ) {
          Icon(Icons.Default.CalendarMonth, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text("AGENDAR NOVO ATENDIMENTO PARA ESSE CLIENTE", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }

        Text("Histórico de Atendimentos (${appointments.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

        if (appointments.isEmpty()) {
          Text("Nenhum atendimento registrado no histórico ainda.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
          appointments.forEach { appt ->
            Card(
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
              shape = RoundedCornerShape(12.dp)
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                  Text(appt.servicosNomes, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                  StatusBadge(statusString = appt.status)
                }
                Text("Data: ${Masks.formatDateBr(appt.dataIso)} às ${appt.horarioInicio} • Profissional: ${appt.funcionarioNome}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Valor: ${Masks.formatCurrency(appt.valorTotal)} (${appt.formaPagamento})", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
              }
            }
          }
        }
      }
    }
  }
}
