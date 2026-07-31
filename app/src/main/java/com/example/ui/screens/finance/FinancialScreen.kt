package com.example.ui.screens.finance

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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MonetizationOn
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
import com.example.data.model.FinancialTransactionEntity
import com.example.data.model.PromotionEntity
import com.example.ui.components.Masks
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun FinancialScreen(
  transactions: List<FinancialTransactionEntity>,
  employees: List<EmployeeEntity>,
  onSaveTransaction: (FinancialTransactionEntity) -> Unit,
  onDeleteTransaction: (FinancialTransactionEntity) -> Unit
) {
  var filterPeriod by remember { mutableStateOf("Mês") } // "Dia", "Semana", "Mês"
  var filterType by remember { mutableStateOf("Todos") } // "Todos", "ENTRADA", "SAIDA"
  var isModalOpen by remember { mutableStateOf(false) }
  var txToDelete by remember { mutableStateOf<FinancialTransactionEntity?>(null) }

  val filteredTx =
    transactions.filter {
      filterType == "Todos" || it.tipo == filterType
    }

  val totalEntradas = filteredTx.filter { it.tipo == "ENTRADA" }.sumOf { it.valor }
  val totalSaidas = filteredTx.filter { it.tipo == "SAIDA" }.sumOf { it.valor }
  val lucroLiquido = totalEntradas - totalSaidas

  Column(
    modifier =
      Modifier.fillMaxSize()
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    Spacer(modifier = Modifier.height(4.dp))

    // 1. TOP BAR WITH PERIOD SELECTOR & "+ NOVA TRANSAÇÃO"
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        listOf("Dia", "Semana", "Mês").forEach { period ->
          FilterChip(
            selected = filterPeriod == period,
            onClick = { filterPeriod = period },
            label = { Text(period, fontWeight = FontWeight.Bold) }
          )
        }
      }

      Surface(
        onClick = { isModalOpen = true },
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
          Text("Nova Transação", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
        }
      }
    }

    // 2. FINANCIAL BANNER METRICS
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
      Card(
        modifier = Modifier.weight(1f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(16.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text("TOTAL RECEITAS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Spacer(modifier = Modifier.height(4.dp))
          Text(Masks.formatCurrency(totalEntradas), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
        }
      }

      Card(
        modifier = Modifier.weight(1f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(16.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text("TOTAL DESPESAS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Spacer(modifier = Modifier.height(4.dp))
          Text(Masks.formatCurrency(totalSaidas), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.error)
        }
      }
    }

    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
      shape = RoundedCornerShape(16.dp)
    ) {
      Row(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text("LUCRO LÍQUIDO NO PERÍODO", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
          Text(
            Masks.formatCurrency(lucroLiquido),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
          )
        }
        Box(
          modifier =
            Modifier.size(46.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
      }
    }

    // 3. COMPARATIVO & COMISSÕES A PAGAR POR FUNCIONÁRIO
    Text("Lucro & Comissões por Profissional", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
      shape = RoundedCornerShape(16.dp)
    ) {
      Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        employees.forEach { emp ->
          val comissao = (emp.totalGerado * (emp.comissao / 100.0))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(emp.nome, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
              Text("Gerou: ${Masks.formatCurrency(emp.totalGerado)}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("Comissão: ${Masks.formatCurrency(comissao)}", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
            }
          }
        }
      }
    }

    // 4. TRANSACTION TYPE FILTER
    Row(
      modifier = Modifier.horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      listOf("Todos", "ENTRADA", "SAIDA").forEach { label ->
        FilterChip(
          selected = filterType == label,
          onClick = { filterType = label },
          label = { Text(if (label == "Todos") "Todos os Movimentos" else if (label == "ENTRADA") "Entradas" else "Saídas") }
        )
      }
    }

    // 5. TRANSACTIONS LIST
    if (filteredTx.isEmpty()) {
      Text("Nenhuma transação registrada.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    } else {
      filteredTx.forEach { tx ->
        val isEntrada = tx.tipo == "ENTRADA"
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
          shape = RoundedCornerShape(14.dp)
        ) {
          Row(
            modifier = Modifier.padding(14.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier =
                  Modifier.size(38.dp)
                    .clip(CircleShape)
                    .background(
                      if (isEntrada) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                      else MaterialTheme.colorScheme.error.copy(alpha = 0.15f)
                    ),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = if (isEntrada) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                  contentDescription = null,
                  tint = if (isEntrada) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                  modifier = Modifier.size(18.dp)
                )
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(tx.descricao, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(
                  "${tx.categoria} • ${Masks.formatDateBr(tx.dataIso)} às ${tx.hora} • ${tx.formaPagamento}",
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = if (isEntrada) "+ ${Masks.formatCurrency(tx.valor)}" else "- ${Masks.formatCurrency(tx.valor)}",
                fontWeight = FontWeight.ExtraBold,
                color = if (isEntrada) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                fontSize = 14.sp
              )
              IconButton(onClick = { txToDelete = tx }) {
                Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f))
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }

  // MODAL FOR NEW TRANSACTION
  if (isModalOpen) {
    TransactionFormDialog(
      onDismiss = { isModalOpen = false },
      onSave = { tx ->
        onSaveTransaction(tx)
        isModalOpen = false
      }
    )
  }

  // DELETE CONFIRMATION
  if (txToDelete != null) {
    AlertDialog(
      onDismissRequest = { txToDelete = null },
      title = { Text("Excluir Lançamento Financeiro") },
      text = { Text("Deseja realmente excluir esta transação de ${Masks.formatCurrency(txToDelete?.valor ?: 0.0)}?") },
      confirmButton = {
        Button(
          onClick = {
            txToDelete?.let { onDeleteTransaction(it) }
            txToDelete = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
          Text("Excluir", color = Color.White)
        }
      },
      dismissButton = {
        TextButton(onClick = { txToDelete = null }) { Text("Cancelar") }
      }
    )
  }
}

@Composable
fun TransactionFormDialog(
  onDismiss: () -> Unit,
  onSave: (FinancialTransactionEntity) -> Unit
) {
  var isEntrada by remember { mutableStateOf(true) }
  var descricao by remember { mutableStateOf("") }
  var valorStr by remember { mutableStateOf("") }
  var categoria by remember { mutableStateOf("Serviço") }
  var formaPagamento by remember { mutableStateOf("PIX") }

  val categorias = listOf("Serviço", "Vendas", "Comissões", "Produtos", "Salários", "Contas fixas", "Outros")
  val formas = listOf("PIX", "Dinheiro", "Cartão Crédito", "Cartão Débito", "Transferência")

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier.fillMaxWidth(0.92f).height(560.dp).clip(RoundedCornerShape(24.dp)),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 6.dp
    ) {
      Column(
        modifier = Modifier.padding(20.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Text("Nova Transação Financeira", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
          FilterChip(
            selected = isEntrada,
            onClick = { isEntrada = true },
            label = { Text("+ Entrada (Receita)") }
          )
          FilterChip(
            selected = !isEntrada,
            onClick = { isEntrada = false },
            label = { Text("- Saída (Despesa)") }
          )
        }

        OutlinedTextField(value = descricao, onValueChange = { descricao = it }, label = { Text("Descrição (ex: Pagamento de Luz, Venda avulsa)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = valorStr, onValueChange = { valorStr = it }, label = { Text("Valor (R$) *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)

        Text("Categoria:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          categorias.forEach { cat ->
            FilterChip(
              selected = categoria == cat,
              onClick = { categoria = cat },
              label = { Text(cat) }
            )
          }
        }

        Text("Forma de Pagamento:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          formas.forEach { fp ->
            FilterChip(
              selected = formaPagamento == fp,
              onClick = { formaPagamento = fp },
              label = { Text(fp) }
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
              val valDouble = valorStr.toDoubleOrNull() ?: 100.0
              val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
              val nowHour = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Calendar.getInstance().time)
              val tx =
                FinancialTransactionEntity(
                  dataIso = today,
                  hora = nowHour,
                  tipo = if (isEntrada) "ENTRADA" else "SAIDA",
                  categoria = categoria,
                  formaPagamento = formaPagamento,
                  valor = valDouble,
                  descricao = descricao.ifBlank { if (isEntrada) "Receita avulsa" else "Despesa avulsa" }
                )
              onSave(tx)
            },
            modifier = Modifier.weight(2f).height(50.dp),
            shape = RoundedCornerShape(14.dp)
          ) {
            Text("LANÇAR TRANSAÇÃO", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

@Composable
fun PromotionsScreen(
  promotions: List<PromotionEntity>,
  onSavePromotion: (PromotionEntity) -> Unit,
  onUpdateStatus: (PromotionEntity, String) -> Unit
) {
  var isModalOpen by remember { mutableStateOf(false) }

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
        Text("Promoções & Fidelidade", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
        Text("Pacotes promocionais, cupons e retenção VIP", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }

      Surface(
        onClick = { isModalOpen = true },
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
          Text("Criar Promoção", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
        }
      }
    }

    promotions.forEach { promo ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(16.dp)
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
                Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(promo.nome, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                Text("Tipo: ${promo.tipo} • Código: ${promo.codigoCupom}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }

            Text(
              text = if (promo.descontoPercent > 0) "${promo.descontoPercent}% OFF" else Masks.formatCurrency(promo.valorPacote),
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.primary
            )
          }

          Spacer(modifier = Modifier.height(8.dp))
          Text(promo.descricao, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

          Spacer(modifier = Modifier.height(10.dp))
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Utilizado: ${promo.usosAtuais} vezes • Status: ${promo.status}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            TextButton(onClick = {
              val newStatus = if (promo.status == "Ativa") "Pausada" else "Ativa"
              onUpdateStatus(promo, newStatus)
            }) {
              Text(if (promo.status == "Ativa") "Pausar" else "Ativar", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }

  if (isModalOpen) {
    var nome by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("Pacote") }
    var desconto by remember { mutableStateOf("20") }
    var codigo by remember { mutableStateOf("VERAO2026") }
    var desc by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { isModalOpen = false }) {
      Surface(
        modifier = Modifier.fillMaxWidth(0.9f).clip(RoundedCornerShape(20.dp)),
        color = MaterialTheme.colorScheme.surface
      ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("Criar Nova Promoção", fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.titleLarge)
          OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Título da Promoção") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = desconto, onValueChange = { desconto = it }, label = { Text("Desconto (%)") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = codigo, onValueChange = { codigo = it }, label = { Text("Código de Cupom") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Descrição detalhada") }, modifier = Modifier.fillMaxWidth())

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            TextButton(onClick = { isModalOpen = false }, modifier = Modifier.weight(1f)) { Text("Cancelar") }
            Button(
              onClick = {
                if (nome.isNotBlank()) {
                  val newPromo =
                    PromotionEntity(
                      nome = nome,
                      descricao = desc.ifBlank { "Desconto especial de fidelidade" },
                      descontoPercent = desconto.toDoubleOrNull() ?: 20.0,
                      cupom = codigo,
                      codigoCupom = codigo
                    )
                  onSavePromotion(newPromo)
                  isModalOpen = false
                }
              },
              modifier = Modifier.weight(1f)
            ) { Text("CRIAR") }
          }
        }
      }
    }
  }
}
