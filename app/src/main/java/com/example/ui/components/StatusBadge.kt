package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppointmentStatus
import com.example.ui.theme.StatusCanceled
import com.example.ui.theme.StatusCompleted
import com.example.ui.theme.StatusConfirmed
import com.example.ui.theme.StatusInProgress
import com.example.ui.theme.StatusNoShow
import com.example.ui.theme.StatusScheduled

@Composable
fun StatusBadge(statusString: String, modifier: Modifier = Modifier) {
  val statusEnum =
    try {
      AppointmentStatus.valueOf(statusString)
    } catch (e: Exception) {
      AppointmentStatus.AGENDADO
    }

  val color =
    when (statusEnum) {
      AppointmentStatus.AGENDADO -> StatusScheduled
      AppointmentStatus.CONFIRMADO -> StatusConfirmed
      AppointmentStatus.EM_ATENDIMENTO -> StatusInProgress
      AppointmentStatus.FINALIZADO -> StatusCompleted
      AppointmentStatus.CANCELADO -> StatusCanceled
      AppointmentStatus.NAO_COMPARECEU -> StatusNoShow
    }

  Text(
    text = statusEnum.label,
    color = Color.White,
    fontSize = 11.sp,
    fontWeight = FontWeight.Bold,
    modifier =
      modifier
        .background(color = color, shape = RoundedCornerShape(12.dp))
        .padding(horizontal = 8.dp, vertical = 3.dp)
  )
}
