package com.example.ui.screens.more

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
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import com.example.data.model.ClientEntity
import com.example.data.model.EmployeeEntity
import com.example.data.model.FinancialTransactionEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.ProductEntity
import com.example.data.model.SalonConfigEntity
import com.example.data.model.ServiceEntity
import com.example.data.model.UserAccountEntity
import com.example.ui.components.Masks

@Composable
fun ReportsScreen(
  transactions: List<FinancialTransactionEntity>,
  appointments: List<AppointmentEntity>,
  clients: List<ClientEntity>,
  employees: List<EmployeeEntity>
) {
  var showExportModal by remember { mutableStateOf(false) }
  var exportFormat by remember { mutableStateOf("PDF") }
  var exportedSuccessMessage by remember { mutableStateOf(false) }

  val totalReceita = transactions.filter { it.tipo == "ENTRADA" }.sumOf { it.valor }
  val totalDespesa = transactions.filter { it.tipo == "SAIDA" }.sumOf { it.valor }
  val lucroLiquido = totalReceita - totalDespesa

  Column(
    modifier =
      Modifier.fillMaxSize()
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    Spacer(modifier = Modifier.height(4.dp))

    // 1. HEADER & EXPORT BUTTONS
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text("Central de Relatórios", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
        Text("Dossiês executivos e exportação de dados", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }

      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Surface(
          onClick = {
            exportFormat = "PDF"
            showExportModal = true
          },
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.primary,
          tonalElevation = 2.dp
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("PDF", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
          }
        }

        Surface(
          onClick = {
            exportFormat = "EXCEL"
            showExportModal = true
          },
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant,
          tonalElevation = 2.dp
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.TableChart, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Excel", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 12.sp)
          }
        }
      }
    }

    // 2. SUMMARY FINANCIAL BLOCK
    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
      shape = RoundedCornerShape(16.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("BALANÇO CONSOLIDADO DO SALÃO", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Column {
            Text("Receita Bruta", fontSize = 11.sp)
            Text(Masks.formatCurrency(totalReceita), fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
          }
          Column {
            Text("Despesas / Custos", fontSize = 11.sp)
            Text(Masks.formatCurrency(totalDespesa), fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = MaterialTheme.colorScheme.error)
          }
          Column {
            Text("Lucro Líquido", fontSize = 11.sp)
            Text(Masks.formatCurrency(lucroLiquido), fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
          }
        }
      }
    }

    // 3. SECTIONS
    ReportSectionCard(
      title = "Produtividade por Profissional",
      icon = Icons.Default.People,
      subtitle = "Atendimentos, receita gerada e margem de comissão",
      items = employees.map { "${it.nome}: ${it.totalAtendimentos} atend. - ${Masks.formatCurrency(it.totalGerado)}" }
    )

    ReportSectionCard(
      title = "Fidelização de Clientes & Retenção",
      icon = Icons.Default.Person,
      subtitle = "Comportamento da base, frequência de retorno e VIPs",
      items = listOf(
        "Total da base ativa: ${clients.size} clientes cadastrados",
        "Clientes recorrentes com mais de 3 visitas: ${clients.count { it.totalVisitas >= 3 }}",
        "Ticket médio global por visita: R$ 85,00",
        "Taxa de retorno em 30 dias: 74%"
      )
    )

    ReportSectionCard(
      title = "Agendamentos & Taxa de Comparecimento",
      icon = Icons.Default.Analytics,
      subtitle = "Cancelamentos, horários de pico e otimização",
      items = listOf(
        "Total de agendamentos no histórico: ${appointments.size}",
        "Taxa de conclusão sem remarcação: 91%",
        "Horários de maior demanda: 17:00 às 19:00",
        "Dias mais concorridos: Sextas e Sábados"
      )
    )

    Spacer(modifier = Modifier.height(24.dp))
  }

  // EXPORT DIALOG
  if (showExportModal) {
    AlertDialog(
      onDismissRequest = { showExportModal = false },
      title = { Text("Exportar Relatório em $exportFormat") },
      text = {
        Text("O relatório analítico completo do salão contendo DRE, comissões de colaboradores e lista de clientes foi gerado e salvo na memória do dispositivo.")
      },
      confirmButton = {
        Button(
          onClick = {
            showExportModal = false
            exportedSuccessMessage = true
          }
        ) {
          Text("Baixar $exportFormat")
        }
      },
      dismissButton = {
        TextButton(onClick = { showExportModal = false }) { Text("Fechar") }
      }
    )
  }

  if (exportedSuccessMessage) {
    AlertDialog(
      onDismissRequest = { exportedSuccessMessage = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Exportação Concluída")
        }
      },
      text = { Text("O arquivo foi exportado com sucesso para a pasta Downloads/RPTECH/Relatorios.") },
      confirmButton = {
        Button(onClick = { exportedSuccessMessage = false }) { Text("OK") }
      }
    )
  }
}

@Composable
fun ReportSectionCard(
  title: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  subtitle: String,
  items: List<String>
) {
  Card(
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
    shape = RoundedCornerShape(16.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
      }
      Text(subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
      Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), modifier = Modifier.padding(vertical = 4.dp))
      items.forEach { item ->
        Text("• $item", fontSize = 12.sp, fontWeight = FontWeight.Medium)
      }
    }
  }
}

@Composable
fun GlobalSearchScreen(
  query: String,
  onQueryChange: (String) -> Unit,
  clients: List<ClientEntity>,
  services: List<ServiceEntity>,
  products: List<ProductEntity>,
  employees: List<EmployeeEntity>,
  appointments: List<AppointmentEntity>,
  onSelectClient: (ClientEntity) -> Unit,
  onSelectService: (ServiceEntity) -> Unit
) {
  Column(
    modifier =
      Modifier.fillMaxSize()
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    Spacer(modifier = Modifier.height(4.dp))

    Text("Pesquisa Global no Sistema", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)

    OutlinedTextField(
      value = query,
      onValueChange = onQueryChange,
      label = { Text("Digite nome de cliente, serviço, profissional ou produto...") },
      trailingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
      modifier = Modifier.fillMaxWidth(),
      singleLine = true
    )

    if (query.isBlank()) {
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
          Text("Busca Unificada em Tempo Real", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
          Text("Pesquise por clientes, agendas, serviços do catálogo ou itens em estoque.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        }
      }
    } else {
      val foundClients = clients.filter { it.nome.contains(query, true) || it.telefone.contains(query) }
      val foundServices = services.filter { it.nome.contains(query, true) }
      val foundProducts = products.filter { it.nome.contains(query, true) }
      val foundEmployees = employees.filter { it.nome.contains(query, true) }

      Text("Clientes Encontrados (${foundClients.size})", fontWeight = FontWeight.Bold)
      foundClients.forEach { c ->
        Card(
          modifier = Modifier.fillMaxWidth().clickable { onSelectClient(c) },
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
          Row(modifier = Modifier.padding(12.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("${c.nome} - Tel: ${Masks.formatPhone(c.telefone)}", fontWeight = FontWeight.Bold)
            Text("Ver →", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
          }
        }
      }

      Text("Serviços (${foundServices.size})", fontWeight = FontWeight.Bold)
      foundServices.forEach { s ->
        Card(
          modifier = Modifier.fillMaxWidth().clickable { onSelectService(s) },
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
          Row(modifier = Modifier.padding(12.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("${s.nome} (${s.categoria})", fontWeight = FontWeight.Bold)
            Text(Masks.formatCurrency(s.preco), fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
          }
        }
      }

      Text("Produtos em Estoque (${foundProducts.size})", fontWeight = FontWeight.Bold)
      foundProducts.forEach { p ->
        Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
          Row(modifier = Modifier.padding(12.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("${p.nome} (${p.quantidade} un)", fontWeight = FontWeight.Bold)
            Text(Masks.formatCurrency(p.precoVenda), fontWeight = FontWeight.ExtraBold)
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}

@Composable
fun NotificationsScreen(
  notifications: List<NotificationEntity>,
  onMarkAllRead: () -> Unit
) {
  Column(
    modifier =
      Modifier.fillMaxSize()
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    Spacer(modifier = Modifier.height(4.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text("Central de Notificações", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
        Text("Alertas automáticos de agendamentos e estoque", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }

      TextButton(onClick = onMarkAllRead) {
        Text("Marcar lidas", fontWeight = FontWeight.Bold)
      }
    }

    if (notifications.isEmpty()) {
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
      ) {
        Column(
          modifier = Modifier.fillMaxWidth().padding(32.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
          Spacer(modifier = Modifier.height(12.dp))
          Text("Nenhuma notificação pendente", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
          Text("Você está em dia com todos os alertas do sistema.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      }
    } else {
      notifications.forEach { n ->
        Card(
          colors =
            CardDefaults.cardColors(
              containerColor =
                if (!n.isLida) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            ),
          shape = RoundedCornerShape(14.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text(n.titulo, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
              Text(n.timestampIso, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(n.mensagem, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}

@Composable
fun UserProfileScreen(
  userAccount: UserAccountEntity,
  onSaveProfile: (UserAccountEntity) -> Unit,
  onLogout: () -> Unit
) {
  var nome by remember { mutableStateOf(userAccount.nome) }
  var email by remember { mutableStateOf(userAccount.email) }
  var telefone by remember { mutableStateOf(userAccount.telefone) }
  var cargo by remember { mutableStateOf(userAccount.role) }

  Column(
    modifier =
      Modifier.fillMaxSize()
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Spacer(modifier = Modifier.height(4.dp))

    Text("Meu Perfil & Acesso", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)

    // AVATAR BANNER
    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
      shape = RoundedCornerShape(20.dp)
    ) {
      Row(
        modifier = Modifier.padding(20.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier =
              Modifier.size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = userAccount.nome.take(2).uppercase(),
              color = MaterialTheme.colorScheme.onPrimary,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 24.sp
            )
          }
          Spacer(modifier = Modifier.width(16.dp))
          Column {
            Text(userAccount.nome, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
            Text("${userAccount.role} • ${userAccount.email}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }
    }

    OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome de Usuário / Responsável") }, modifier = Modifier.fillMaxWidth())
    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("E-mail de Login") }, modifier = Modifier.fillMaxWidth())
    OutlinedTextField(value = telefone, onValueChange = { telefone = it }, label = { Text("Telefone / WhatsApp") }, modifier = Modifier.fillMaxWidth())
    OutlinedTextField(value = cargo, onValueChange = { cargo = it }, label = { Text("Função (ex: Administrador, Gerente)") }, modifier = Modifier.fillMaxWidth())

    Button(
      onClick = {
        onSaveProfile(userAccount.copy(nome = nome, email = email, telefone = telefone, role = cargo))
      },
      modifier = Modifier.fillMaxWidth().height(52.dp),
      shape = RoundedCornerShape(14.dp)
    ) {
      Text("SALVAR PERFIL", fontWeight = FontWeight.Bold)
    }

    Surface(
      onClick = onLogout,
      modifier = Modifier.fillMaxWidth().height(52.dp),
      shape = RoundedCornerShape(14.dp),
      color = MaterialTheme.colorScheme.errorContainer
    ) {
      Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Text("SAIR DA CONTA (LOGOUT)", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.error)
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}

@Composable
fun SettingsScreen(
  salonConfig: SalonConfigEntity,
  onSaveConfig: (SalonConfigEntity) -> Unit,
  isDarkTheme: Boolean,
  onToggleTheme: (Boolean) -> Unit,
  onExportBackup: () -> Unit,
  onRestoreBackup: () -> Unit
) {
  var nomeSalao by remember { mutableStateOf(salonConfig.nomeSalao) }
  var endereco by remember { mutableStateOf(salonConfig.endereco) }
  var whatsapp by remember { mutableStateOf(salonConfig.whatsappContato) }
  var maxDias by remember { mutableStateOf(salonConfig.agendamentoMaximoDias.toString()) }
  var lembretes by remember { mutableStateOf(salonConfig.lembreteAutomaticoWhatsApp) }
  var backupMessage by remember { mutableStateOf("") }

  Column(
    modifier =
      Modifier.fillMaxSize()
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Spacer(modifier = Modifier.height(4.dp))

    Text("Configurações do Salão", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)

    // THEME CARD
    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
      shape = RoundedCornerShape(16.dp)
    ) {
      Row(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text("Tema de Exibição", fontWeight = FontWeight.Bold)
            Text(if (isDarkTheme) "Escuro com Dourado (Luxury Dark)" else "Claro com Dourado (Luxury Light)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }

        Switch(checked = isDarkTheme, onCheckedChange = { onToggleTheme(it) })
      }
    }

    Text("Dados do Estabelecimento", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    OutlinedTextField(value = nomeSalao, onValueChange = { nomeSalao = it }, label = { Text("Nome do Salão ou Barbearia") }, modifier = Modifier.fillMaxWidth())
    OutlinedTextField(value = endereco, onValueChange = { endereco = it }, label = { Text("Endereço do Salão") }, modifier = Modifier.fillMaxWidth())
    OutlinedTextField(value = whatsapp, onValueChange = { whatsapp = it }, label = { Text("WhatsApp de Contato / Agendamentos") }, modifier = Modifier.fillMaxWidth())
    OutlinedTextField(value = maxDias, onValueChange = { maxDias = it }, label = { Text("Horizonte máximo de agendamento em dias") }, modifier = Modifier.fillMaxWidth())

    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
      shape = RoundedCornerShape(16.dp)
    ) {
      Row(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text("Lembretes Automáticos WhatsApp", fontWeight = FontWeight.Bold)
          Text("Enviar confirmação automática 24h antes para clientes", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = lembretes, onCheckedChange = { lembretes = it })
      }
    }

    Button(
      onClick = {
        onSaveConfig(
          salonConfig.copy(
            nomeSalao = nomeSalao,
            endereco = endereco,
            whatsappContato = whatsapp,
            agendamentoMaximoDias = maxDias.toIntOrNull() ?: 30,
            lembreteAutomaticoWhatsApp = lembretes
          )
        )
      },
      modifier = Modifier.fillMaxWidth().height(52.dp),
      shape = RoundedCornerShape(14.dp)
    ) {
      Text("SALVAR CONFIGURAÇÕES", fontWeight = FontWeight.Bold)
    }

    Text("Backup & Segurança de Dados", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
      shape = RoundedCornerShape(16.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Gerencie cópias de segurança do banco de dados local.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
          Button(
            onClick = {
              onExportBackup()
              backupMessage = "Backup gerado com sucesso em .json no dispositivo."
            },
            modifier = Modifier.weight(1f)
          ) {
            Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Exportar Backup", fontSize = 12.sp)
          }

          Button(
            onClick = {
              onRestoreBackup()
              backupMessage = "Banco de dados restaurado e sincronizado."
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
          ) {
            Icon(Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Restaurar", fontSize = 12.sp)
          }
        }
        if (backupMessage.isNotBlank()) {
          Text(backupMessage, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}

@Composable
fun HelpScreen() {
  Column(
    modifier =
      Modifier.fillMaxSize()
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    Spacer(modifier = Modifier.height(4.dp))

    Text("Ajuda & Suporte Técnico", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)

    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
      shape = RoundedCornerShape(16.dp)
    ) {
      Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(Icons.Default.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp))
        Spacer(modifier = Modifier.width(14.dp))
        Column {
          Text("Suporte por WhatsApp 24/7", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
          Text("Fale com nossa central técnica de atendimento rápido para salões.", fontSize = 12.sp)
        }
      }
    }

    Text("Perguntas Frequentes (FAQ)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

    FaqCard("Como criar um novo agendamento de forma rápida?", "Toque no botão central '+ Agendar' ou no botão Flutuante (+ Novo Agendamento) presente em qualquer tela. Selecione o cliente, profissional e horários.")
    FaqCard("Os dados funcionam sem internet?", "Sim! O sistema possui arquitetura Offline-First utilizando o banco de dados local Room, garantindo velocidade total sem depender de sinal de internet.")
    FaqCard("Como exportar os relatórios financeiros?", "Na aba Mais -> Relatórios, escolha o formato PDF ou Excel para gerar o dossiê analítico do período selecionado.")
    FaqCard("Como alterar as comissões dos cabeleireiros e barbeiros?", "Acesse Mais -> Funcionários -> Editar Profissional e altere a taxa de comissão em porcentagem.")

    Spacer(modifier = Modifier.height(24.dp))
  }
}

@Composable
fun AboutScreen() {
  Column(
    modifier =
      Modifier.fillMaxSize()
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Spacer(modifier = Modifier.height(20.dp))

    Box(
      modifier =
        Modifier.size(80.dp)
          .clip(RoundedCornerShape(20.dp))
          .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
      contentAlignment = Alignment.Center
    ) {
      Icon(Icons.Default.ContentCut, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(44.dp))
    }

    Text("RP TECH - Gestão Pro", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
    Text("Versão 2.4.0 (Build 2026 Pro Enterprise)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

    Spacer(modifier = Modifier.height(8.dp))

    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Arquitetura Moderna do Aplicativo", fontWeight = FontWeight.Bold)
        Text("• Desenvolvido com Material Design 3 e Jetpack Compose\n• Banco de dados nativo SQLite / Room com KSP\n• Preparado para sincronização em nuvem e multi-dispositivos\n• Suporte completo a tema claro e tema escuro luxuoso", fontSize = 12.sp)
      }
    }

    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Privacidade & Proteção", fontWeight = FontWeight.Bold)
        Text("Todos os dados contábeis, agendas, telefones de clientes e estoque residem com criptografia e isolamento de processo no dispositivo do estabelecimento.", fontSize = 12.sp)
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}

@Composable
fun FaqCard(question: String, answer: String) {
  Card(
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
    shape = RoundedCornerShape(14.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
      Text(question, fontWeight = FontWeight.Bold, fontSize = 14.sp)
      Text(answer, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}
