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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ProductEntity
import com.example.ui.components.Masks

@Composable
fun StockScreen(
  products: List<ProductEntity>,
  lowStockProducts: List<ProductEntity>,
  onSaveProduct: (ProductEntity) -> Unit,
  onDeleteProduct: (ProductEntity) -> Unit,
  onAdjustStock: (ProductEntity, Int) -> Unit
) {
  var viewMode by remember { mutableStateOf(0) } // 0 = Todos, 1 = Estoque Baixo, 2 = Pedido Reposição
  var searchQuery by remember { mutableStateOf("") }
  var isModalOpen by remember { mutableStateOf(false) }
  var productToEdit by remember { mutableStateOf<ProductEntity?>(null) }
  var productToDelete by remember { mutableStateOf<ProductEntity?>(null) }
  var stockAdjustModal by remember { mutableStateOf<ProductEntity?>(null) }

  val filteredProducts =
    when (viewMode) {
      1 -> lowStockProducts
      2 -> products.filter { it.quantidade <= it.estoqueMinimo + 2 } // Reposição sugerida
      else -> products
    }.filter {
      searchQuery.isBlank() ||
        it.nome.contains(searchQuery, ignoreCase = true) ||
        it.fornecedor.contains(searchQuery, ignoreCase = true)
    }

  Column(
    modifier =
      Modifier.fillMaxSize()
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    Spacer(modifier = Modifier.height(4.dp))

    // 1. SEARCH BAR & "+ NOVO PRODUTO" BUTTON
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        label = { Text("Pesquisar Produto ou Fornecedor...") },
        trailingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        modifier = Modifier.weight(1f),
        singleLine = true
      )

      Surface(
        onClick = {
          productToEdit = null
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

    // 2. VIEW MODE SELECTOR (Todos, Estoque Baixo, Reposição)
    Row(
      modifier = Modifier.horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      val modes = listOf("Todos os Produtos (${products.size})", "Estoque Baixo (${lowStockProducts.size})", "Reposição Sugerida")
      modes.forEachIndexed { idx, label ->
        FilterChip(
          selected = viewMode == idx,
          onClick = { viewMode = idx },
          label = { Text(label, fontWeight = FontWeight.Bold) },
          leadingIcon =
            if (idx == 1 && lowStockProducts.isNotEmpty()) {
              { Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp)) }
            } else null
        )
      }
    }

    // 3. SPECIAL REPONER / BUY RECOMMENDATION BANNER IF IN REPONER MODE
    if (viewMode == 2) {
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(16.dp)
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp))
          Spacer(modifier = Modifier.width(14.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text("Comprar & Repor Estoque", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
            Text("Lista gerada automaticamente com base no consumo e estoque mínimo de segurança.", fontSize = 12.sp)
          }
        }
      }
    }

    // 4. PRODUCTS LIST
    if (filteredProducts.isEmpty()) {
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
      ) {
        Column(
          modifier = Modifier.fillMaxWidth().padding(32.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Icon(Icons.Default.Inventory, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
          Spacer(modifier = Modifier.height(12.dp))
          Text("Nenhum produto listado nesta visão", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
          Text("Seu estoque está em dia ou sem itens correspondentes.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        }
      }
    } else {
      filteredProducts.forEach { prod ->
        ProductCard(
          product = prod,
          onEditClick = {
            productToEdit = prod
            isModalOpen = true
          },
          onDeleteClick = { productToDelete = prod },
          onAdjustStockClick = { stockAdjustModal = prod }
        )
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }

  // PRODUCT FORM DIALOG
  if (isModalOpen) {
    ProductFormDialog(
      initialProduct = productToEdit,
      onDismiss = {
        isModalOpen = false
        productToEdit = null
      },
      onSave = { prod ->
        onSaveProduct(prod)
        isModalOpen = false
        productToEdit = null
      }
    )
  }

  // STOCK ADJUST DIALOG
  val adjustTarget = stockAdjustModal
  if (adjustTarget != null) {
    StockAdjustDialog(
      product = adjustTarget,
      onDismiss = { stockAdjustModal = null },
      onConfirmAdjust = { delta ->
        onAdjustStock(adjustTarget, delta)
        stockAdjustModal = null
      }
    )
  }

  // DELETE CONFIRMATION
  if (productToDelete != null) {
    AlertDialog(
      onDismissRequest = { productToDelete = null },
      title = { Text("Excluir Produto") },
      text = {
        Text("Deseja realmente remover '${productToDelete?.nome}' do estoque? Essa ação apagará a ficha do produto.")
      },
      confirmButton = {
        Button(
          onClick = {
            productToDelete?.let { onDeleteProduct(it) }
            productToDelete = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
          Text("Excluir", color = Color.White)
        }
      },
      dismissButton = {
        TextButton(onClick = { productToDelete = null }) { Text("Cancelar") }
      }
    )
  }
}

@Composable
fun ProductCard(
  product: ProductEntity,
  onEditClick: () -> Unit,
  onDeleteClick: () -> Unit,
  onAdjustStockClick: () -> Unit
) {
  val margemLucroPercent =
    if (product.precoCusto > 0) {
      ((product.precoVenda - product.precoCusto) / product.precoCusto * 100.0)
    } else 0.0

  val isLowStock = product.quantidade <= product.estoqueMinimo

  Card(
    modifier = Modifier.fillMaxWidth(),
    colors =
      CardDefaults.cardColors(
        containerColor =
          if (isLowStock) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f)
          else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
      ),
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
                .background(
                  if (isLowStock) MaterialTheme.colorScheme.error.copy(alpha = 0.15f)
                  else MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              if (isLowStock) Icons.Default.Warning else Icons.Default.Inventory,
              contentDescription = null,
              tint = if (isLowStock) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = product.nome,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.ExtraBold,
              color = if (isLowStock) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "${product.categoria} • Forn: ${product.fornecedor}",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = onEditClick) {
            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.onSurfaceVariant)
          }
          IconButton(onClick = onDeleteClick) {
            Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = MaterialTheme.colorScheme.error)
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // FINANCIALS AND STOCK METRICS
      Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(MaterialTheme.colorScheme.surface).padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text("ESTOQUE ATUAL", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(
            "${product.quantidade} ${product.unidade}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = if (isLowStock) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
          )
          if (isLowStock) {
            Text("Mín: ${product.estoqueMinimo} un", fontSize = 10.sp, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
          }
        }

        Column {
          Text("PREÇO VENDA", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(Masks.formatCurrency(product.precoVenda), fontWeight = FontWeight.Bold, fontSize = 13.sp)
          Text("Custo: ${Masks.formatCurrency(product.precoCusto)}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Column {
          Text("LUCRO", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("+${margemLucroPercent.toInt()}%", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
          Button(
            onClick = onAdjustStockClick,
            shape = RoundedCornerShape(8.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
            modifier = Modifier.height(32.dp)
          ) {
            Text("Ajustar +/-", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

@Composable
fun ProductFormDialog(
  initialProduct: ProductEntity?,
  onDismiss: () -> Unit,
  onSave: (ProductEntity) -> Unit
) {
  var nome by remember { mutableStateOf(initialProduct?.nome ?: "") }
  var categoria by remember { mutableStateOf(initialProduct?.categoria ?: "Cosméticos") }
  var precoCustoStr by remember { mutableStateOf(initialProduct?.precoCusto?.toString() ?: "30.0") }
  var precoVendaStr by remember { mutableStateOf(initialProduct?.precoVenda?.toString() ?: "75.0") }
  var qtdStr by remember { mutableStateOf(initialProduct?.quantidade?.toString() ?: "10") }
  var minStr by remember { mutableStateOf(initialProduct?.estoqueMinimo?.toString() ?: "5") }
  var unidade by remember { mutableStateOf(initialProduct?.unidade ?: "unidade") }
  var fornecedor by remember { mutableStateOf(initialProduct?.fornecedor ?: "Distribuidora Master Beauty") }

  val categorias = listOf("Shampoo", "Condicionador", "Tintura", "Óleo", "Gel", "Cosméticos")

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
          text = if (initialProduct == null) "Novo Produto no Estoque" else "Editar Produto",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.ExtraBold
        )

        OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome do Produto *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = precoCustoStr, onValueChange = { precoCustoStr = it }, label = { Text("Preço de Custo (R$) *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = precoVendaStr, onValueChange = { precoVendaStr = it }, label = { Text("Preço de Venda (R$) *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = qtdStr, onValueChange = { qtdStr = it }, label = { Text("Quantidade em Estoque *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = minStr, onValueChange = { minStr = it }, label = { Text("Estoque Mínimo (alerta) *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = unidade, onValueChange = { unidade = it }, label = { Text("Unidade (ml, g, unidade)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = fornecedor, onValueChange = { fornecedor = it }, label = { Text("Fornecedor / Distribuidor") }, modifier = Modifier.fillMaxWidth(), singleLine = true)

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
                val c = precoCustoStr.toDoubleOrNull() ?: 30.0
                val v = precoVendaStr.toDoubleOrNull() ?: 75.0
                val q = qtdStr.toIntOrNull() ?: 10
                val m = minStr.toIntOrNull() ?: 5
                val toSave =
                  initialProduct?.copy(
                    nome = nome,
                    categoria = categoria,
                    precoCusto = c,
                    precoVenda = v,
                    quantidade = q,
                    estoqueMinimo = m,
                    unidade = unidade,
                    fornecedor = fornecedor
                  )
                    ?: ProductEntity(
                      nome = nome,
                      categoria = categoria,
                      precoCusto = c,
                      precoVenda = v,
                      quantidade = q,
                      estoqueMinimo = m,
                      unidade = unidade,
                      fornecedor = fornecedor
                    )
                onSave(toSave)
              }
            },
            modifier = Modifier.weight(2f).height(50.dp),
            shape = RoundedCornerShape(14.dp)
          ) {
            Text("SALVAR PRODUTO", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

@Composable
fun StockAdjustDialog(
  product: ProductEntity,
  onDismiss: () -> Unit,
  onConfirmAdjust: (delta: Int) -> Unit
) {
  var amount by remember { mutableStateOf(1) }
  var isAddition by remember { mutableStateOf(true) }
  var motivo by remember { mutableStateOf("Reposição de Fornecedor") }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier.fillMaxWidth(0.88f).height(440.dp).clip(RoundedCornerShape(24.dp)),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 6.dp
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text("Ajuste de Estoque", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
        Text(product.nome, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text("Estoque atual: ${product.quantidade} ${product.unidade}", fontSize = 13.sp)

        // Type toggle (+ ou -)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
          FilterChip(
            selected = isAddition,
            onClick = {
              isAddition = true
              motivo = "Reposição de Fornecedor"
            },
            label = { Text("+ Entrada (Reposição)") }
          )
          FilterChip(
            selected = !isAddition,
            onClick = {
              isAddition = false
              motivo = "Consumo Interno / Perda"
            },
            label = { Text("- Saída / Uso") }
          )
        }

        // Amount buttons
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          IconButton(onClick = { if (amount > 1) amount-- }) {
            Icon(Icons.Default.Remove, contentDescription = "Menos")
          }
          Text(text = "$amount", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
          IconButton(onClick = { amount++ }) {
            Icon(Icons.Default.Add, contentDescription = "Mais")
          }
        }

        OutlinedTextField(
          value = motivo,
          onValueChange = { motivo = it },
          label = { Text("Motivo do Ajuste") },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
          TextButton(onClick = onDismiss, modifier = Modifier.weight(1f).height(50.dp)) {
            Text("Cancelar")
          }
          Button(
            onClick = {
              val delta = if (isAddition) amount else -amount
              onConfirmAdjust(delta)
            },
            modifier = Modifier.weight(1.5f).height(50.dp),
            shape = RoundedCornerShape(14.dp)
          ) {
            Text("APLICAR AJUSTE", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
