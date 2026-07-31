package com.example.ui.screens.catalog

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ServiceEntity
import com.example.ui.components.Masks

@Composable
fun ServicesScreen(
  services: List<ServiceEntity>,
  categoryFilter: String,
  onCategoryFilterChange: (String) -> Unit,
  onSaveService: (ServiceEntity) -> Unit,
  onDeleteService: (ServiceEntity) -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }
  var isModalOpen by remember { mutableStateOf(false) }
  var serviceToEdit by remember { mutableStateOf<ServiceEntity?>(null) }
  var serviceToDelete by remember { mutableStateOf<ServiceEntity?>(null) }

  val categories = listOf("Todos", "Cabelo", "Barba", "Estética", "Unhas", "Maquiagem")

  val filteredServices =
    services.filter {
      val catMatch = categoryFilter == "Todos" || it.categoria.equals(categoryFilter, ignoreCase = true)
      val searchMatch =
        searchQuery.isBlank() ||
          it.nome.contains(searchQuery, ignoreCase = true) ||
          it.descricao.contains(searchQuery, ignoreCase = true)
      catMatch && searchMatch
    }

  Column(
    modifier =
      Modifier.fillMaxSize()
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    Spacer(modifier = Modifier.height(4.dp))

    // 1. SEARCH BAR & "+ NOVO SERVIÇO" BUTTON
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        label = { Text("Pesquisar Serviços...") },
        trailingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        modifier = Modifier.weight(1f),
        singleLine = true
      )

      Surface(
        onClick = {
          serviceToEdit = null
          isModalOpen = true
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

    // 2. CATEGORIES HORIZONTAL PILLS
    Row(
      modifier = Modifier.horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      categories.forEach { cat ->
        FilterChip(
          selected = categoryFilter == cat,
          onClick = { onCategoryFilterChange(cat) },
          label = { Text(cat) }
        )
      }
    }

    // 3. SERVICES LIST
    filteredServices.forEach { svc ->
      ServiceCard(
        service = svc,
        onEditClick = {
          serviceToEdit = svc
          isModalOpen = true
        },
        onDeleteClick = { serviceToDelete = svc }
      )
    }

    Spacer(modifier = Modifier.height(24.dp))
  }

  // SERVICE FORM DIALOG
  if (isModalOpen) {
    ServiceFormDialog(
      initialService = serviceToEdit,
      onDismiss = {
        isModalOpen = false
        serviceToEdit = null
      },
      onSave = { svc ->
        onSaveService(svc)
        isModalOpen = false
        serviceToEdit = null
      }
    )
  }

  // DELETE CONFIRMATION
  if (serviceToDelete != null) {
    AlertDialog(
      onDismissRequest = { serviceToDelete = null },
      title = { Text("Excluir Serviço") },
      text = {
        Text("Deseja realmente remover '${serviceToDelete?.nome}' do catálogo? Ele não aparecerá em novos agendamentos.")
      },
      confirmButton = {
        Button(
          onClick = {
            serviceToDelete?.let { onDeleteService(it) }
            serviceToDelete = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
          Text("Excluir", color = Color.White)
        }
      },
      dismissButton = {
        TextButton(onClick = { serviceToDelete = null }) { Text("Cancelar") }
      }
    )
  }
}

@Composable
fun ServiceCard(
  service: ServiceEntity,
  onEditClick: () -> Unit,
  onDeleteClick: () -> Unit
) {
  val receitaGerada = service.preco * service.totalVendas

  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
    shape = RoundedCornerShape(16.dp),
    elevation = CardDefaults.cardElevation(2.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier =
              Modifier.size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.ContentCut, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = service.nome,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "${service.categoria} • ${service.duracaoMin} min",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = Masks.formatCurrency(service.preco),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.width(8.dp))
          IconButton(onClick = onEditClick) {
            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.onSurfaceVariant)
          }
          IconButton(onClick = onDeleteClick) {
            Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = MaterialTheme.colorScheme.error)
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))
      if (service.descricao.isNotBlank()) {
        Text(
          text = service.descricao,
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
      }

      // METRICS FOOTER FOR SERVICE
      Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(MaterialTheme.colorScheme.surface).padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "Realizado: ${service.totalVendas} vezes",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "Receita Gerada: ${Masks.formatCurrency(receitaGerada)}",
          fontSize = 11.sp,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.primary
        )
      }
    }
  }
}

@Composable
fun ServiceFormDialog(
  initialService: ServiceEntity?,
  onDismiss: () -> Unit,
  onSave: (ServiceEntity) -> Unit
) {
  var nome by remember { mutableStateOf(initialService?.nome ?: "") }
  var categoria by remember { mutableStateOf(initialService?.categoria ?: "Cabelo") }
  var precoStr by remember { mutableStateOf(initialService?.preco?.toString() ?: "50.0") }
  var duracaoStr by remember { mutableStateOf(initialService?.duracaoMin?.toString() ?: "45") }
  var comissaoStr by remember { mutableStateOf(initialService?.comissaoEspecífica?.toString() ?: "") }
  var descricao by remember { mutableStateOf(initialService?.descricao ?: "") }

  val categorias = listOf("Cabelo", "Barba", "Estética", "Unhas", "Maquiagem")

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier.fillMaxWidth(0.92f).height(600.dp).clip(RoundedCornerShape(24.dp)),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 6.dp
    ) {
      Column(
        modifier = Modifier.padding(20.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Text(
          text = if (initialService == null) "Novo Serviço" else "Editar Serviço",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.ExtraBold
        )

        OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome do Serviço *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = precoStr, onValueChange = { precoStr = it }, label = { Text("Preço (R$) *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = duracaoStr, onValueChange = { duracaoStr = it }, label = { Text("Duração (minutos) *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = comissaoStr, onValueChange = { comissaoStr = it }, label = { Text("Comissão Específica (% opcional)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = descricao, onValueChange = { descricao = it }, label = { Text("Descrição detalhada") }, modifier = Modifier.fillMaxWidth(), singleLine = false)

        Text("Categoria:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Row(
          modifier = Modifier.horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          categorias.forEach { cat ->
            FilterChip(
              selected = categoria == cat,
              onClick = { categoria = cat },
              label = { Text(cat) }
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
          TextButton(onClick = onDismiss, modifier = Modifier.weight(1f).height(50.dp)) {
            Text("Cancelar")
          }
          Button(
            onClick = {
              if (nome.isNotBlank()) {
                val p = precoStr.toDoubleOrNull() ?: 50.0
                val d = duracaoStr.toIntOrNull() ?: 45
                val c = comissaoStr.toDoubleOrNull() ?: 30.0
                val toSave =
                  initialService?.copy(
                    nome = nome,
                    categoria = categoria,
                    preco = p,
                    duracaoMin = d,
                    comissaoEspecífica = c,
                    descricao = descricao
                  )
                    ?: ServiceEntity(
                      nome = nome,
                      categoria = categoria,
                      preco = p,
                      duracaoMin = d,
                      comissaoEspecífica = c,
                      descricao = descricao
                    )
                onSave(toSave)
              }
            },
            modifier = Modifier.weight(2f).height(50.dp),
            shape = RoundedCornerShape(14.dp)
          ) {
            Text("SALVAR SERVIÇO", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
