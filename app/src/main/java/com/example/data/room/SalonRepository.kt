package com.example.data.room

import com.example.data.model.AppointmentEntity
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
import kotlinx.coroutines.flow.Flow

class SalonRepository(private val dao: SalonDao) {

  // Clients
  val allClients: Flow<List<ClientEntity>> = dao.getAllClients()
  val favoriteClients: Flow<List<ClientEntity>> = dao.getFavoriteClients()
  suspend fun getClientById(id: Int): ClientEntity? = dao.getClientById(id)
  suspend fun saveClient(client: ClientEntity): Long = dao.insertClient(client)
  suspend fun updateClient(client: ClientEntity) = dao.updateClient(client)
  suspend fun deleteClient(id: Int) = dao.deleteClient(id)

  // Employees
  val allEmployees: Flow<List<EmployeeEntity>> = dao.getAllEmployees()
  val activeEmployees: Flow<List<EmployeeEntity>> = dao.getActiveEmployees()
  suspend fun saveEmployee(emp: EmployeeEntity): Long = dao.insertEmployee(emp)
  suspend fun updateEmployee(emp: EmployeeEntity) = dao.updateEmployee(emp)
  suspend fun deleteEmployee(id: Int) = dao.deleteEmployee(id)

  // Services
  val allServices: Flow<List<ServiceEntity>> = dao.getAllServices()
  val activeServices: Flow<List<ServiceEntity>> = dao.getActiveServices()
  suspend fun saveService(svc: ServiceEntity): Long = dao.insertService(svc)
  suspend fun updateService(svc: ServiceEntity) = dao.updateService(svc)
  suspend fun deleteService(id: Int) = dao.deleteService(id)

  // Products
  val allProducts: Flow<List<ProductEntity>> = dao.getAllProducts()
  val lowStockProducts: Flow<List<ProductEntity>> = dao.getLowStockProducts()
  suspend fun saveProduct(prod: ProductEntity): Long = dao.insertProduct(prod)
  suspend fun updateProduct(prod: ProductEntity) = dao.updateProduct(prod)
  suspend fun deleteProduct(id: Int) = dao.deleteProduct(id)

  // Appointments
  val allAppointments: Flow<List<AppointmentEntity>> = dao.getAllAppointments()
  fun getAppointmentsByDate(dateIso: String): Flow<List<AppointmentEntity>> =
    dao.getAppointmentsByDate(dateIso)
  fun getAppointmentsByClient(clientId: Int): Flow<List<AppointmentEntity>> =
    dao.getAppointmentsByClient(clientId)
  fun getNextAppointment(dateIso: String): Flow<AppointmentEntity?> =
    dao.getNextAppointmentFlow(dateIso)
  suspend fun saveAppointment(app: AppointmentEntity): Long = dao.insertAppointment(app)
  suspend fun updateAppointment(app: AppointmentEntity) = dao.updateAppointment(app)
  suspend fun deleteAppointment(id: Int) = dao.deleteAppointment(id)

  // Promotions
  val allPromotions: Flow<List<PromotionEntity>> = dao.getAllPromotions()
  val activePromotions: Flow<List<PromotionEntity>> = dao.getActivePromotions()
  suspend fun savePromotion(promo: PromotionEntity): Long = dao.insertPromotion(promo)
  suspend fun updatePromotion(promo: PromotionEntity) = dao.updatePromotion(promo)
  suspend fun deletePromotion(id: Int) = dao.deletePromotion(id)

  // Financial Transactions
  val allTransactions: Flow<List<FinancialTransactionEntity>> = dao.getAllTransactions()
  fun getTransactionsByDate(dateIso: String): Flow<List<FinancialTransactionEntity>> =
    dao.getTransactionsByDate(dateIso)
  suspend fun saveTransaction(tx: FinancialTransactionEntity): Long = dao.insertTransaction(tx)
  suspend fun deleteTransaction(id: Int) = dao.deleteTransaction(id)

  // Notifications
  val allNotifications: Flow<List<NotificationEntity>> = dao.getAllNotifications()
  suspend fun saveNotification(notif: NotificationEntity): Long = dao.insertNotification(notif)
  suspend fun markAllNotificationsRead() = dao.markAllNotificationsRead()

  // Salon Config & User Account
  val salonConfig: Flow<SalonConfigEntity?> = dao.getSalonConfig()
  suspend fun saveSalonConfig(config: SalonConfigEntity) = dao.saveSalonConfig(config)

  val userAccount: Flow<UserAccountEntity?> = dao.getUserAccount()
  suspend fun saveUserAccount(user: UserAccountEntity) = dao.saveUserAccount(user)

  // Action Logs
  val recentLogs: Flow<List<LogActionEntity>> = dao.getRecentLogs()
  suspend fun logAction(user: String, action: String, detail: String) {
    val timestamp = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault()).format(java.util.Calendar.getInstance().time)
    dao.insertLogAction(LogActionEntity(usuario = user, acao = action, detalhe = detail, dataHoraIso = timestamp))
  }

  suspend fun seedIfEmpty() {
    DatabaseInitializer.seedIfEmpty(dao)
  }
}
