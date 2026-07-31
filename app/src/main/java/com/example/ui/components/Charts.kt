package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary

@Composable
fun RevenueBarChart(
  dataPoints: List<Pair<String, Double>>,
  title: String,
  modifier: Modifier = Modifier
) {
  var animated by remember { mutableStateOf(false) }
  LaunchedEffect(dataPoints) { animated = true }
  val progress by
    animateFloatAsState(targetValue = if (animated) 1f else 0f, animationSpec = tween(800))

  val maxVal = dataPoints.maxOfOrNull { it.second } ?: 1.0
  val primaryColor = MaterialTheme.colorScheme.primary

  Surface(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
    tonalElevation = 2.dp
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(16.dp))

      Canvas(modifier = Modifier.fillMaxWidth().height(140.dp)) {
        val barWidth = (size.width / (dataPoints.size * 2f)).coerceAtLeast(16f)
        val gap = (size.width - (barWidth * dataPoints.size)) / (dataPoints.size + 1)

        dataPoints.forEachIndexed { i, pair ->
          val valNorm = (pair.second / maxVal.coerceAtLeast(1.0)).toFloat()
          val barHeight = (size.height - 40f) * valNorm * progress
          val x = gap + i * (barWidth + gap)
          val y = size.height - barHeight - 24f

          // Draw rounded bar
          drawRoundRect(
            brush =
              Brush.verticalGradient(
                colors = listOf(primaryColor, primaryColor.copy(alpha = 0.6f))
              ),
            topLeft = Offset(x, y),
            size = Size(barWidth, barHeight),
            cornerRadius = CornerRadius(12f, 12f)
          )
        }
      }

      // X Axis labels
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        dataPoints.forEach { pair ->
          Text(
            text = pair.first,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
          )
        }
      }
    }
  }
}

@Composable
fun OccupancyRateGauge(
  ratePercent: Float, // e.g. 78f
  title: String = "Taxa de Ocupação da Agenda",
  modifier: Modifier = Modifier
) {
  var animated by remember { mutableStateOf(false) }
  LaunchedEffect(ratePercent) { animated = true }
  val progress by
    animateFloatAsState(
      targetValue = if (animated) ratePercent / 100f else 0f,
      animationSpec = tween(1000)
    )

  val primaryColor = MaterialTheme.colorScheme.primary
  val trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)

  Surface(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
    tonalElevation = 2.dp
  ) {
    Row(
      modifier = Modifier.padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Agenda otimizada para máximo faturamento com alertas em tempo real.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Spacer(modifier = Modifier.width(16.dp))

      Box(contentAlignment = Alignment.Center, modifier = Modifier.size(80.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
          drawArc(
            color = trackColor,
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = 20f, cap = StrokeCap.Round)
          )
          drawArc(
            color = primaryColor,
            startAngle = -90f,
            sweepAngle = 360f * progress,
            useCenter = false,
            style = Stroke(width = 20f, cap = StrokeCap.Round)
          )
        }

        Text(
          text = "${(ratePercent).toInt()}%",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.onSurface
        )
      }
    }
  }
}

@Composable
fun TopServicesRankingCard(
  services: List<Pair<String, Int>>, // Service name to sales count
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
    tonalElevation = 2.dp
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(
        text = "Serviços Mais Vendidos",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(12.dp))

      val maxCount = services.maxOfOrNull { it.second }?.coerceAtLeast(1) ?: 1

      services.take(4).forEachIndexed { idx, pair ->
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "${idx + 1}. ${pair.first}",
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.Medium,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "${pair.second} atend.",
              style = MaterialTheme.typography.bodySmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          // Progress bar
          val fraction = pair.second.toFloat() / maxCount
          Box(
            modifier =
              Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)).background(
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
              )
          ) {
            Box(
              modifier =
                Modifier.fillMaxWidth(fraction)
                  .height(8.dp)
                  .clip(RoundedCornerShape(4.dp))
                  .background(MaterialTheme.colorScheme.primary)
            )
          }
        }
      }
    }
  }
}
