package com.example.ui.screens.employees

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
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
import com.example.data.model.EmployeeEntity
import com.example.ui.components.Masks

@Composable
fun EmployeesScreen(
  employees: List<EmployeeEntity>,
  roleFilter: String,
  onRoleFilterChange: (String) -> Unit,
  onSaveEmployee: (EmployeeEntity) -> Unit,
  onDeleteEmployee: (EmployeeEntity) -> Unit
) {
  var isModalOpen by remember { mutableStateOf(false) }
  var employeeToEdit by remember { mutableStateOf<EmployeeEntity?>(null) }
  var employeeToDelete by remember { mutableStateOf<EmployeeEntity?>(null) }

  val filteredEmployees =
    if (roleFilter == "Todos") employees
    else employees.filter { it.funcao.equals(roleFilter, ignoreCase = true) }

  val roles = listOf("Todos", "Cabeleireiro", "Barbeiro", "Esteticista", "Nail Designer")

  Column(
    modifier =
      Modifier.fillMaxSize()
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    Spacer(modifier = Modifier.height(4.dp))

    // 1. FILTER PILLS & NEW BUTTON
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        modifier = Modifier.weight(1f).horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        roles.forEach { role ->
          FilterChip(
            selected = roleFilter == role,
            onClick = { onRoleFilterChange(role) },
            label = { Text(role) }
          )
        }
      }

      Spacer(modifier = Modifier.width(8.dp))

      Surface(
        onClick = {
          employeeToEdit = null
          isModalOpen = true
        },
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.primary,
        tonalElevation = 2.dp,
        modifier = Modifier.height(44.dp)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Novo", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
        }
      }
    }

    // 2. EMPLOYEES LIST
    filteredEmployees.forEach { emp ->
      EmployeeCard(
        employee = emp,
        onEditClick = {
          employeeToEdit = emp
          isModalOpen = true
        },
        onDeleteClick = { employeeToDelete = emp }
      )
    }

    Spacer(modifier = Modifier.height(24.dp))
  }

  // EMPLOYEE FORM DIALOG
  if (isModalOpen) {
    EmployeeFormDialog(
      initialEmployee = employeeToEdit,
      onDismiss = {
        isModalOpen = false
        employeeToEdit = null
      },
      onSave = { emp ->
        onSaveEmployee(emp)
        isModalOpen = false
        employeeToEdit = null
      }
    )
  }

  // DELETE CONFIRMATION
  if (employeeToDelete != null) {
    AlertDialog(
      onDismissRequest = { employeeToDelete = null },
      title = { Text("Excluir Profissional") },
      text = {
        Text("Deseja realmente remover ${employeeToDelete?.nome}? Seus agendamentos passados serão preservados no histórico.")
      },
      confirmButton = {
        Button(
          onClick = {
            employeeToDelete?.let { onDeleteEmployee(it) }
            employeeToDelete = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
          Text("Excluir", color = Color.White)
        }
      },
      dismissButton = {
        TextButton(onClick = { employeeToDelete = null }) { Text("Cancelar") }
      }
    )
  }
}

@Composable
fun EmployeeCard(
  employee: EmployeeEntity,
  onEditClick: () -> Unit,
  onDeleteClick: () -> Unit
) {
  val comissaoTotal = (employee.totalGerado * (employee.comissao / 100.0))

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
              Modifier.size(50.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = employee.nome.take(2).uppercase(),
              color = MaterialTheme.colorScheme.onPrimary,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 18.sp
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = employee.nome,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.ExtraBold
            )
            Text(
              text = "${employee.funcao} • Comissão ${employee.comissao.toInt()}%",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(11.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = Masks.formatPhone(employee.telefone),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(3.dp))
            Text("${employee.avaliacao}", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
          }
          Spacer(modifier = Modifier.width(8.dp))
          IconButton(onClick = onEditClick) {
            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.onSurfaceVariant)
          }
          IconButton(onClick = onDeleteClick) {
            Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = MaterialTheme.colorScheme.error)
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // METRICS BANNER FOR EMPLOYEE
      Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surface).padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text("TOTAL GERADO", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(Masks.formatCurrency(employee.totalGerado), fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
        }
        Column {
          Text("COMISSÃO A PAGAR", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(Masks.formatCurrency(comissaoTotal), fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
        }
        Column {
          Text("ATENDIMENTOS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("${employee.totalAtendimentos} serv.", fontWeight = FontWeight.ExtraBold)
        }
      }

      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "Especialidades: ${employee.especialidades}",
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      Text(
        text = "Trabalho: ${employee.diasTrabalho} (${employee.horariosTrabalho})",
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Composable
fun EmployeeFormDialog(
  initialEmployee: EmployeeEntity?,
  onDismiss: () -> Unit,
  onSave: (EmployeeEntity) -> Unit
) {
  var nome by remember { mutableStateOf(initialEmployee?.nome ?: "") }
  var funcao by remember { mutableStateOf(initialEmployee?.funcao ?: "Cabeleireiro") }
  var telefone by remember { mutableStateOf(initialEmployee?.telefone ?: "") }
  var email by remember { mutableStateOf(initialEmployee?.email ?: "") }
  var comissaoStr by remember { mutableStateOf(initialEmployee?.comissao?.toString() ?: "40") }
  var especialidades by remember { mutableStateOf(initialEmployee?.especialidades ?: "Cortes, Coloração, Barba") }
  var diasTrabalho by remember { mutableStateOf(initialEmployee?.diasTrabalho ?: "Seg a Sáb") }
  var horariosTrabalho by remember { mutableStateOf(initialEmployee?.horariosTrabalho ?: "09:00 - 19:00") }
  var salarioBaseStr by remember { mutableStateOf(initialEmployee?.salarioBase?.toString() ?: "1800.0") }

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
          text = if (initialEmployee == null) "Novo Profissional" else "Editar Profissional",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.ExtraBold
        )

        OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome Completo *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = funcao, onValueChange = { funcao = it }, label = { Text("Função / Cargo (ex: Barbeiro, Cabeleireira)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = telefone, onValueChange = { telefone = Masks.formatPhone(it) }, label = { Text("Telefone / WhatsApp") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("E-mail") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = comissaoStr, onValueChange = { comissaoStr = it }, label = { Text("Comissão (%) *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = salarioBaseStr, onValueChange = { salarioBaseStr = it }, label = { Text("Salário Base Fixo (R$)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = especialidades, onValueChange = { especialidades = it }, label = { Text("Especialidades") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = diasTrabalho, onValueChange = { diasTrabalho = it }, label = { Text("Dias de Trabalho (ex: Ter a Sáb)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = horariosTrabalho, onValueChange = { horariosTrabalho = it }, label = { Text("Horários (ex: 09:00 - 18:00)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
          TextButton(onClick = onDismiss, modifier = Modifier.weight(1f).height(50.dp)) {
            Text("Cancelar")
          }
          Button(
            onClick = {
              if (nome.isNotBlank()) {
                val comiss = comissaoStr.toDoubleOrNull() ?: 40.0
                val sal = salarioBaseStr.toDoubleOrNull() ?: 0.0
                val toSave =
                  initialEmployee?.copy(
                    nome = nome,
                    funcao = funcao,
                    telefone = telefone,
                    email = email,
                    comissao = comiss,
                    especialidades = especialidades,
                    diasTrabalho = diasTrabalho,
                    horariosTrabalho = horariosTrabalho,
                    salarioBase = sal
                  )
                    ?: EmployeeEntity(
                      nome = nome,
                      funcao = funcao,
                      telefone = telefone.ifBlank { "(11) 98888-0000" },
                      email = email,
                      comissao = comiss,
                      especialidades = especialidades,
                      diasTrabalho = diasTrabalho,
                      horariosTrabalho = horariosTrabalho,
                      salarioBase = sal
                    )
                onSave(toSave)
              }
            },
            modifier = Modifier.weight(2f).height(50.dp),
            shape = RoundedCornerShape(14.dp)
          ) {
            Text("SALVAR PROFISSIONAL", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
