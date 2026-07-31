package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AppointmentEntity
import com.example.data.model.AppointmentStatus
import com.example.data.model.ClientEntity
import com.example.data.model.EmployeeEntity
import com.example.data.model.FinancialTransactionEntity
import com.example.data.model.LogActionEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.ProductEntity
import com.example.data.model.PromotionEntity
import com.example.data.model.SalonConfigEntity
import com.example.data.model.ServiceEntity
import com.example.data.model.UserAccountEntity
import com.example.data.room.AppDatabase
import com.example.data.room.SalonRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class GlobalSearchResults(
  val clients: List<ClientEntity> = emptyList(),
  val services: List<ServiceEntity> = emptyList(),
  val products: List<ProductEntity> = emptyList(),
  val employees: List<EmployeeEntity> = emptyList(),
  val appointments: List<AppointmentEntity> = emptyList(),
  val promotions: List<PromotionEntity> = emptyList()
)

class SalonViewModel(application: Application) : AndroidViewModel(application) {

  private val repository: SalonRepository

  init {
    val dao = AppDatabase.getDatabase(application).salonDao()
    repository = SalonRepository(dao)
    // Seed initial data if database is empty
    viewModelScope.launch {
      val existingClients = repository.allClients.firstOrNull()
      if (existingClients.isNullOrEmpty()) {
        repository.seedIfEmpty()
      }
    }
  }

  // Current Date ISO string helper
  private fun getTodayIso(): String {
    val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return fmt.format(Calendar.getInstance().time)
  }

  val todayIsoString: String = getTodayIso()

  // Selected Date for Agenda
  private val _selectedDateIso = MutableStateFlow(getTodayIso())
  val selectedDateIso: StateFlow<String> = _selectedDateIso.asStateFlow()

  fun setSelectedDateIso(dateIso: String) {
    _selectedDateIso.value = dateIso
  }

  // Agenda View Mode (0 = Dia, 1 = Semana, 2 = Mês)
  private val _agendaViewMode = MutableStateFlow(0)
  val agendaViewMode: StateFlow<Int> = _agendaViewMode.asStateFlow()

  fun setAgendaViewMode(mode: Int) {
    _agendaViewMode.value = mode
  }

  // FAB Modal State (+ Novo Agendamento)
  private val _isNewAppointmentModalOpen = MutableStateFlow(false)
  val isNewAppointmentModalOpen: StateFlow<Boolean> = _isNewAppointmentModalOpen.asStateFlow()

  fun openNewAppointmentModal() {
    _isNewAppointmentModalOpen.value = true
  }

  fun closeNewAppointmentModal() {
    _isNewAppointmentModalOpen.value = false
  }

  // ROOM DATA FLOWS
  val allClients: StateFlow<List<ClientEntity>> =
    repository.allClients.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  val allEmployees: StateFlow<List<EmployeeEntity>> =
    repository.allEmployees.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  val allServices: StateFlow<List<ServiceEntity>> =
    repository.allServices.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  val allProducts: StateFlow<List<ProductEntity>> =
    repository.allProducts.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  val lowStockProducts: StateFlow<List<ProductEntity>> =
    repository.lowStockProducts.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  val allAppointments: StateFlow<List<AppointmentEntity>> =
    repository.allAppointments.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  val allPromotions: StateFlow<List<PromotionEntity>> =
    repository.allPromotions.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  val allTransactions: StateFlow<List<FinancialTransactionEntity>> =
    repository.allTransactions.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  val allNotifications: StateFlow<List<NotificationEntity>> =
    repository.allNotifications.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  val salonConfig: StateFlow<SalonConfigEntity> =
    repository.salonConfig
      .map { it ?: SalonConfigEntity() }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SalonConfigEntity()
      )

  val userAccount: StateFlow<UserAccountEntity> =
    repository.userAccount
      .map { it ?: UserAccountEntity() }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserAccountEntity()
      )

  val recentLogs: StateFlow<List<LogActionEntity>> =
    repository.recentLogs.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  // Filters & Search States
  private val _clientSearchQuery = MutableStateFlow("")
  val clientSearchQuery: StateFlow<String> = _clientSearchQuery.asStateFlow()
  fun setClientSearchQuery(query: String) {
    _clientSearchQuery.value = query
  }

  private val _serviceCategoryFilter = MutableStateFlow("Todos")
  val serviceCategoryFilter: StateFlow<String> = _serviceCategoryFilter.asStateFlow()
  fun setServiceCategoryFilter(category: String) {
    _serviceCategoryFilter.value = category
  }

  private val _employeeRoleFilter = MutableStateFlow("Todos")
  val employeeRoleFilter: StateFlow<String> = _employeeRoleFilter.asStateFlow()
  fun setEmployeeRoleFilter(role: String) {
    _employeeRoleFilter.value = role
  }

  // GLOBAL SEARCH
  private val _globalSearchQuery = MutableStateFlow("")
  val globalSearchQuery: StateFlow<String> = _globalSearchQuery.asStateFlow()
  fun setGlobalSearchQuery(query: String) {
    _globalSearchQuery.value = query
  }

  // CRUD ACTIONS - APPOINTMENTS
  fun saveNewAppointment(
    client: ClientEntity,
    employee: EmployeeEntity,
    services: List<ServiceEntity>,
    dataIso: String,
    horarioInicio: String,
    formaPagamento: String,
    observacoes: String,
    valorTotalCustom: Double? = null
  ) {
    viewModelScope.launch {
      val somaPreco = valorTotalCustom ?: services.sumOf { it.preco }
      val servicosStr = services.joinToString(" + ") { it.nome }
      val servicosIds = services.joinToString(",") { it.id.toString() }

      val appt =
        AppointmentEntity(
          clienteId = client.id,
          clienteNome = client.nome,
          clienteTelefone = client.telefone,
          funcionarioId = employee.id,
          funcionarioNome = employee.nome,
          servicosNomes = servicosStr,
          servicosIds = servicosIds,
          valorTotal = somaPreco,
          dataIso = dataIso,
          horarioInicio = horarioInicio,
          status = AppointmentStatus.AGENDADO.name,
          formaPagamento = formaPagamento,
          observacoes = observacoes
        )
      repository.saveAppointment(appt)

      // Also create notification
      repository.saveNotification(
        NotificationEntity(
          titulo = "Novo Agendamento Criado",
          mensagem = "${client.nome} agendou $servicosStr com ${employee.nome} em $dataIso às $horarioInicio.",
          timestampIso = "$dataIso $horarioInicio",
          categoria = "PROXIMO"
        )
      )

      // Log action
      repository.logAction(
        user = userAccount.value.nome,
        action = "NOVO_AGENDAMENTO",
        detail = "Cliente: ${client.nome}, Profissional: ${employee.nome}, Total: R$ $somaPreco"
      )

      closeNewAppointmentModal()
    }
  }

  fun updateAppointmentStatus(appointment: AppointmentEntity, newStatus: AppointmentStatus) {
    viewModelScope.launch {
      val updated = appointment.copy(status = newStatus.name)
      repository.updateAppointment(updated)

      // If finished, record financial transaction automatically!
      if (newStatus == AppointmentStatus.FINALIZADO) {
        val tx =
          FinancialTransactionEntity(
            dataIso = appointment.dataIso,
            hora = appointment.horarioInicio,
            tipo = "ENTRADA",
            categoria = "Serviço",
            formaPagamento = appointment.formaPagamento,
            valor = appointment.valorTotal,
            descricao = "${appointment.servicosNomes} - ${appointment.clienteNome}",
            funcionarioNome = appointment.funcionarioNome,
            clienteNome = appointment.clienteNome
          )
        repository.saveTransaction(tx)

        // Update client metrics
        val client = repository.getClientById(appointment.clienteId)
        if (client != null) {
          val newVisits = client.totalVisitas + 1
          val newSpent = client.totalGasto + appointment.valorTotal
          repository.updateClient(
            client.copy(
              totalVisitas = newVisits,
              totalGasto = newSpent,
              ultimaVisita = appointment.dataIso
            )
          )
        }
      }

      repository.logAction(
        user = userAccount.value.nome,
        action = "ATUALIZAR_AGENDAMENTO",
        detail = "Status alterado para ${newStatus.label} (${appointment.clienteNome})"
      )
    }
  }

  fun deleteAppointment(appointment: AppointmentEntity) {
    viewModelScope.launch {
      repository.deleteAppointment(appointment.id)
      repository.logAction(
        user = userAccount.value.nome,
        action = "EXCLUIR_AGENDAMENTO",
        detail = "Agendamento com ${appointment.clienteNome} foi excluído."
      )
    }
  }

  // CRUD CLIENTS
  fun saveClient(client: ClientEntity) {
    viewModelScope.launch {
      repository.saveClient(client)
      repository.logAction(user = userAccount.value.nome, action = "SALVAR_CLIENTE", detail = client.nome)
    }
  }

  fun updateClient(client: ClientEntity) {
    viewModelScope.launch {
      repository.updateClient(client)
      repository.logAction(user = userAccount.value.nome, action = "ATUALIZAR_CLIENTE", detail = client.nome)
    }
  }

  fun deleteClient(client: ClientEntity) {
    viewModelScope.launch {
      repository.deleteClient(client.id)
      repository.logAction(user = userAccount.value.nome, action = "EXCLUIR_CLIENTE", detail = client.nome)
    }
  }

  fun toggleClientFavorite(client: ClientEntity) {
    viewModelScope.launch {
      repository.updateClient(client.copy(isFavorite = !client.isFavorite))
    }
  }

  // CRUD EMPLOYEES
  fun saveEmployee(employee: EmployeeEntity) {
    viewModelScope.launch {
      repository.saveEmployee(employee)
      repository.logAction(user = userAccount.value.nome, action = "SALVAR_FUNCIONARIO", detail = employee.nome)
    }
  }

  fun updateEmployee(employee: EmployeeEntity) {
    viewModelScope.launch {
      repository.updateEmployee(employee)
      repository.logAction(user = userAccount.value.nome, action = "ATUALIZAR_FUNCIONARIO", detail = employee.nome)
    }
  }

  fun deleteEmployee(employee: EmployeeEntity) {
    viewModelScope.launch {
      repository.deleteEmployee(employee.id)
      repository.logAction(user = userAccount.value.nome, action = "EXCLUIR_FUNCIONARIO", detail = employee.nome)
    }
  }

  // CRUD SERVICES
  fun saveService(service: ServiceEntity) {
    viewModelScope.launch {
      repository.saveService(service)
      repository.logAction(user = userAccount.value.nome, action = "SALVAR_SERVICO", detail = service.nome)
    }
  }

  fun updateService(service: ServiceEntity) {
    viewModelScope.launch {
      repository.updateService(service)
      repository.logAction(user = userAccount.value.nome, action = "ATUALIZAR_SERVICO", detail = service.nome)
    }
  }

  fun deleteService(service: ServiceEntity) {
    viewModelScope.launch {
      repository.deleteService(service.id)
      repository.logAction(user = userAccount.value.nome, action = "EXCLUIR_SERVICO", detail = service.nome)
    }
  }

  // CRUD PRODUCTS
  fun saveProduct(product: ProductEntity) {
    viewModelScope.launch {
      repository.saveProduct(product)
      repository.logAction(user = userAccount.value.nome, action = "SALVAR_PRODUTO", detail = product.nome)
    }
  }

  fun updateProduct(product: ProductEntity) {
    viewModelScope.launch {
      repository.updateProduct(product)
      repository.logAction(user = userAccount.value.nome, action = "ATUALIZAR_PRODUTO", detail = product.nome)
    }
  }

  fun deleteProduct(product: ProductEntity) {
    viewModelScope.launch {
      repository.deleteProduct(product.id)
      repository.logAction(user = userAccount.value.nome, action = "EXCLUIR_PRODUTO", detail = product.nome)
    }
  }

  fun adjustStock(product: ProductEntity, delta: Int) {
    viewModelScope.launch {
      val newQty = (product.quantidade + delta).coerceAtLeast(0)
      repository.updateProduct(product.copy(quantidade = newQty))
      repository.logAction(
        user = userAccount.value.nome,
        action = "AJUSTE_ESTOQUE",
        detail = "${product.nome}: ${product.quantidade} -> $newQty"
      )
    }
  }

  // CRUD PROMOTIONS
  fun savePromotion(promo: PromotionEntity) {
    viewModelScope.launch {
      repository.savePromotion(promo)
      repository.logAction(user = userAccount.value.nome, action = "SALVAR_PROMOCAO", detail = promo.nome)
    }
  }

  fun updatePromotionStatus(promo: PromotionEntity, newStatus: String) {
    viewModelScope.launch {
      repository.updatePromotion(promo.copy(status = newStatus))
      repository.logAction(user = userAccount.value.nome, action = "STATUS_PROMOCAO", detail = "${promo.nome} ($newStatus)")
    }
  }

  // FINANCIAL TRANSACTIONS
  fun saveTransaction(tx: FinancialTransactionEntity) {
    viewModelScope.launch {
      repository.saveTransaction(tx)
      repository.logAction(user = userAccount.value.nome, action = "NOVA_TRANSAÇÃO", detail = "${tx.tipo} - R$ ${tx.valor}")
    }
  }

  fun deleteTransaction(tx: FinancialTransactionEntity) {
    viewModelScope.launch {
      repository.deleteTransaction(tx.id)
    }
  }

  // NOTIFICATIONS
  fun markAllNotificationsAsRead() {
    viewModelScope.launch {
      repository.markAllNotificationsRead()
    }
  }

  // USER & CONFIG
  fun updateSalonConfig(config: SalonConfigEntity) {
    viewModelScope.launch {
      repository.saveSalonConfig(config)
      repository.logAction(user = userAccount.value.nome, action = "CONFIG_ATUALIZADA", detail = config.nomeSalao)
    }
  }

  fun toggleDarkTheme(isDark: Boolean) {
    viewModelScope.launch {
      val current = salonConfig.value
      repository.saveSalonConfig(current.copy(isDarkTheme = isDark))
    }
  }

  fun updateUserAccount(user: UserAccountEntity) {
    viewModelScope.launch {
      repository.saveUserAccount(user)
      repository.logAction(user = user.nome, action = "PERFIL_ATUALIZADO", detail = user.email)
    }
  }

  fun login(provider: String, email: String, name: String = "Usuário Conectado") {
    viewModelScope.launch {
      val current = userAccount.value
      repository.saveUserAccount(
        current.copy(
          nome = name,
          email = email,
          loginProvider = provider,
          isLoggedIn = true
        )
      )
    }
  }

  fun logout() {
    viewModelScope.launch {
      val current = userAccount.value
      repository.saveUserAccount(current.copy(isLoggedIn = false))
    }
  }
}
