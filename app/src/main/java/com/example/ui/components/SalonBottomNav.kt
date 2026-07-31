package com.example.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun SalonBottomNav(
  currentRoute: String?,
  onNavigate: (String) -> Unit,
  onOpenDrawer: () -> Unit
) {
  NavigationBar(
    containerColor = MaterialTheme.colorScheme.surface,
    modifier = Modifier.navigationBarsPadding()
  ) {
    NavigationBarItem(
      selected = currentRoute == "dashboard",
      onClick = { onNavigate("dashboard") },
      icon = {
        Icon(imageVector = Icons.Default.Dashboard, contentDescription = "Dashboard")
      },
      label = { Text("Dashboard", fontWeight = if (currentRoute == "dashboard") FontWeight.Bold else FontWeight.Normal) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.primary,
        selectedTextColor = MaterialTheme.colorScheme.primary,
        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
      )
    )

    NavigationBarItem(
      selected = currentRoute == "agenda",
      onClick = { onNavigate("agenda") },
      icon = {
        Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "Agenda")
      },
      label = { Text("Agenda", fontWeight = if (currentRoute == "agenda") FontWeight.Bold else FontWeight.Normal) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.primary,
        selectedTextColor = MaterialTheme.colorScheme.primary,
        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
      )
    )

    NavigationBarItem(
      selected = currentRoute == "clients",
      onClick = { onNavigate("clients") },
      icon = {
        Icon(imageVector = Icons.Default.People, contentDescription = "Clientes")
      },
      label = { Text("Clientes", fontWeight = if (currentRoute == "clients") FontWeight.Bold else FontWeight.Normal) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.primary,
        selectedTextColor = MaterialTheme.colorScheme.primary,
        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
      )
    )

    NavigationBarItem(
      selected = currentRoute == "financial",
      onClick = { onNavigate("financial") },
      icon = {
        Icon(imageVector = Icons.Default.MonetizationOn, contentDescription = "Financeiro")
      },
      label = { Text("Financeiro", fontWeight = if (currentRoute == "financial") FontWeight.Bold else FontWeight.Normal) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.primary,
        selectedTextColor = MaterialTheme.colorScheme.primary,
        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
      )
    )

    NavigationBarItem(
      selected = false,
      onClick = onOpenDrawer,
      icon = {
        Icon(imageVector = Icons.Default.Menu, contentDescription = "Mais")
      },
      label = { Text("Mais") },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.primary,
        selectedTextColor = MaterialTheme.colorScheme.primary
      )
    )
  }
}
