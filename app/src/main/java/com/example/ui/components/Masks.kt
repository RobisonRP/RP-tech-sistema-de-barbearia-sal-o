package com.example.ui.components

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Masks {

  fun formatCurrency(value: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    return format.format(value)
  }

  fun formatPhone(phone: String): String {
    val digits = phone.filter { it.isDigit() }
    return when {
      digits.length == 11 -> "(${digits.substring(0, 2)}) ${digits.substring(2, 7)}-${digits.substring(7)}"
      digits.length == 10 -> "(${digits.substring(0, 2)}) ${digits.substring(2, 6)}-${digits.substring(6)}"
      else -> phone
    }
  }

  fun formatCpf(cpf: String): String {
    val digits = cpf.filter { it.isDigit() }
    return if (digits.length == 11) {
      "${digits.substring(0, 3)}.${digits.substring(3, 6)}.${digits.substring(6, 9)}-${digits.substring(9)}"
    } else cpf
  }

  fun formatDateBr(dateIso: String): String {
    return try {
      val inFmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
      val outFmt = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
      val d = inFmt.parse(dateIso) ?: return dateIso
      outFmt.format(d)
    } catch (e: Exception) {
      dateIso
    }
  }

  fun getDayOfWeekBr(dateIso: String): String {
    return try {
      val inFmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
      val d = inFmt.parse(dateIso) ?: return ""
      val cal = java.util.Calendar.getInstance().apply { time = d }
      when (cal.get(java.util.Calendar.DAY_OF_WEEK)) {
        java.util.Calendar.MONDAY -> "Segunda-feira"
        java.util.Calendar.TUESDAY -> "Terça-feira"
        java.util.Calendar.WEDNESDAY -> "Quarta-feira"
        java.util.Calendar.THURSDAY -> "Quinta-feira"
        java.util.Calendar.FRIDAY -> "Sexta-feira"
        java.util.Calendar.SATURDAY -> "Sábado"
        java.util.Calendar.SUNDAY -> "Domingo"
        else -> ""
      }
    } catch (e: Exception) {
      ""
    }
  }
}
