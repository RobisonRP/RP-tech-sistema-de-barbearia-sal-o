package com.example.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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

@Dao
interface SalonDao {
  // CLIENTS
  @Query("SELECT * FROM clients ORDER BY nome ASC")
  fun getAllClients(): Flow<List<ClientEntity>>

  @Query("SELECT * FROM clients WHERE isFavorite = 1 ORDER BY nome ASC")
  fun getFavoriteClients(): Flow<List<ClientEntity>>

  @Query("SELECT * FROM clients WHERE id = :clientId")
  suspend fun getClientById(clientId: Int): ClientEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertClient(client: ClientEntity): Long

  @Update
  suspend fun updateClient(client: ClientEntity)

  @Query("DELETE FROM clients WHERE id = :clientId")
  suspend fun deleteClient(clientId: Int)

  // EMPLOYEES
  @Query("SELECT * FROM employees ORDER BY nome ASC")
  fun getAllEmployees(): Flow<List<EmployeeEntity>>

  @Query("SELECT * FROM employees WHERE status = 'Ativo' ORDER BY nome ASC")
  fun getActiveEmployees(): Flow<List<EmployeeEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertEmployee(employee: EmployeeEntity): Long

  @Update
  suspend fun updateEmployee(employee: EmployeeEntity)

  @Query("DELETE FROM employees WHERE id = :empId")
  suspend fun deleteEmployee(empId: Int)

  // SERVICES
  @Query("SELECT * FROM services ORDER BY categoria ASC, nome ASC")
  fun getAllServices(): Flow<List<ServiceEntity>>

  @Query("SELECT * FROM services WHERE isAtivo = 1 ORDER BY categoria ASC, nome ASC")
  fun getActiveServices(): Flow<List<ServiceEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertService(service: ServiceEntity): Long

  @Update
  suspend fun updateService(service: ServiceEntity)

  @Query("DELETE FROM services WHERE id = :serviceId")
  suspend fun deleteService(serviceId: Int)

  // PRODUCTS
  @Query("SELECT * FROM products ORDER BY nome ASC")
  fun getAllProducts(): Flow<List<ProductEntity>>

  @Query("SELECT * FROM products WHERE quantidade <= estoqueMinimo ORDER BY quantidade ASC")
  fun getLowStockProducts(): Flow<List<ProductEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertProduct(product: ProductEntity): Long

  @Update
  suspend fun updateProduct(product: ProductEntity)

  @Query("DELETE FROM products WHERE id = :prodId")
  suspend fun deleteProduct(prodId: Int)

  // APPOINTMENTS
  @Query("SELECT * FROM appointments ORDER BY dataIso DESC, horarioInicio ASC")
  fun getAllAppointments(): Flow<List<AppointmentEntity>>

  @Query("SELECT * FROM appointments WHERE dataIso = :dateIso ORDER BY horarioInicio ASC")
  fun getAppointmentsByDate(dateIso: String): Flow<List<AppointmentEntity>>

  @Query("SELECT * FROM appointments WHERE clienteId = :clientId ORDER BY dataIso DESC")
  fun getAppointmentsByClient(clientId: Int): Flow<List<AppointmentEntity>>

  @Query("SELECT * FROM appointments WHERE dataIso = :dateIso AND (status = 'AGENDADO' OR status = 'CONFIRMADO' OR status = 'EM_ATENDIMENTO') ORDER BY horarioInicio ASC LIMIT 1")
  fun getNextAppointmentFlow(dateIso: String): Flow<AppointmentEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAppointment(appointment: AppointmentEntity): Long

  @Update
  suspend fun updateAppointment(appointment: AppointmentEntity)

  @Query("DELETE FROM appointments WHERE id = :appId")
  suspend fun deleteAppointment(appId: Int)

  // PROMOTIONS
  @Query("SELECT * FROM promotions ORDER BY id DESC")
  fun getAllPromotions(): Flow<List<PromotionEntity>>

  @Query("SELECT * FROM promotions WHERE status = 'Ativa' ORDER BY id DESC")
  fun getActivePromotions(): Flow<List<PromotionEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPromotion(promo: PromotionEntity): Long

  @Update
  suspend fun updatePromotion(promo: PromotionEntity)

  @Query("DELETE FROM promotions WHERE id = :promoId")
  suspend fun deletePromotion(promoId: Int)

  // FINANCIAL TRANSACTIONS
  @Query("SELECT * FROM financial_transactions ORDER BY dataIso DESC, id DESC")
  fun getAllTransactions(): Flow<List<FinancialTransactionEntity>>

  @Query("SELECT * FROM financial_transactions WHERE dataIso = :dateIso ORDER BY id DESC")
  fun getTransactionsByDate(dateIso: String): Flow<List<FinancialTransactionEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTransaction(transaction: FinancialTransactionEntity): Long

  @Query("DELETE FROM financial_transactions WHERE id = :transId")
  suspend fun deleteTransaction(transId: Int)

  // NOTIFICATIONS
  @Query("SELECT * FROM notifications ORDER BY id DESC")
  fun getAllNotifications(): Flow<List<NotificationEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertNotification(notification: NotificationEntity): Long

  @Query("UPDATE notifications SET isLida = 1")
  suspend fun markAllNotificationsRead()

  // SALON CONFIG
  @Query("SELECT * FROM salon_config WHERE id = 1")
  fun getSalonConfig(): Flow<SalonConfigEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveSalonConfig(config: SalonConfigEntity)

  // USER ACCOUNT
  @Query("SELECT * FROM user_account WHERE id = 1")
  fun getUserAccount(): Flow<UserAccountEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveUserAccount(user: UserAccountEntity)

  // LOGS
  @Query("SELECT * FROM log_actions ORDER BY id DESC LIMIT 100")
  fun getRecentLogs(): Flow<List<LogActionEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertLogAction(log: LogActionEntity)
}
