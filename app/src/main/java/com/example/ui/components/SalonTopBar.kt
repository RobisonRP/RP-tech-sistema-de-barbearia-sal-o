package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalonTopBar(
  title: String,
  unreadNotificationsCount: Int,
  isDarkTheme: Boolean,
  onOpenDrawer: () -> Unit,
  onSearchClick: () -> Unit,
  onNotificationsClick: () -> Unit,
  onThemeToggle: () -> Unit,
  onProfileClick: () -> Unit
) {
  TopAppBar(
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier =
            Modifier.size(34.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.ContentCut,
            contentDescription = "Salon Logo",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = title,
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.onBackground
        )
      }
    },
    navigationIcon = {
      IconButton(onClick = onOpenDrawer) {
        Icon(
          imageVector = Icons.Default.Menu,
          contentDescription = "Menu Lateral",
          tint = MaterialTheme.colorScheme.onBackground
        )
      }
    },
    actions = {
      // Search button
      IconButton(onClick = onSearchClick) {
        Icon(
          imageVector = Icons.Default.Search,
          contentDescription = "Pesquisar",
          tint = MaterialTheme.colorScheme.onBackground
        )
      }

      // Theme toggle
      IconButton(onClick = onThemeToggle) {
        Icon(
          imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
          contentDescription = "Alternar Tema",
          tint = MaterialTheme.colorScheme.primary
        )
      }

      // Notifications badge
      IconButton(onClick = onNotificationsClick) {
        BadgedBox(
          badge = {
            if (unreadNotificationsCount > 0) {
              Badge { Text(text = unreadNotificationsCount.toString()) }
            }
          }
        ) {
          Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notificações",
            tint = MaterialTheme.colorScheme.onBackground
          )
        }
      }

      // Profile avatar button
      Box(
        modifier =
          Modifier.padding(end = 8.dp)
            .size(36.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .clickable { onProfileClick() }
            .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "RP",
          color = MaterialTheme.colorScheme.onPrimary,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold
        )
      }
    },
    colors =
      TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface
      )
  )
}
