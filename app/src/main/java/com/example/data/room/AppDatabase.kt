package com.example.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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

@Database(
  entities = [
    ClientEntity::class,
    EmployeeEntity::class,
    ServiceEntity::class,
    ProductEntity::class,
    AppointmentEntity::class,
    PromotionEntity::class,
    FinancialTransactionEntity::class,
    NotificationEntity::class,
    SalonConfigEntity::class,
    UserAccountEntity::class,
    LogActionEntity::class
  ],
  version = 1,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

  abstract fun salonDao(): SalonDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
      return INSTANCE
        ?: synchronized(this) {
          val instance =
            Room.databaseBuilder(
              context.applicationContext,
              AppDatabase::class.java,
              "bellus_gestao_database.db"
            )
              .fallbackToDestructiveMigration()
              .build()
          INSTANCE = instance
          instance
        }
    }
  }
}
