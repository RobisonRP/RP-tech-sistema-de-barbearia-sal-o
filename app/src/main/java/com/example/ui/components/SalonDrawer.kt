package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.SalonConfigEntity
import com.example.data.model.UserAccountEntity
import com.example.ui.navigation.Screen

@Composable
fun SalonDrawer(
  currentRoute: String?,
  salonConfig: SalonConfigEntity,
  userAccount: UserAccountEntity,
  onNavigate: (String) -> Unit,
  onNewAppointmentClick: () -> Unit,
  onCloseDrawer: () -> Unit
) {
  ModalDrawerSheet(
    modifier = Modifier.width(300.dp).fillMaxHeight(),
    drawerContainerColor = MaterialTheme.colorScheme.surface
  ) {
    Column(
      modifier = Modifier.fillMaxHeight().verticalScroll(rememberScrollState())
    ) {
      // DRAWER HEADER
      Box(
        modifier =
          Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(20.dp)
      ) {
        Column {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            Box(
              modifier =
                Modifier.size(60.dp)
                  .clip(RoundedCornerShape(14.dp))
                  .background(Color.Black),
              contentAlignment = Alignment.Center
            ) {
              Image(
                painter = painterResource(id = R.drawable.img_salon_logo),
                contentDescription = "Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(60.dp).clip(RoundedCornerShape(14.dp))
              )
            }

            Box(
              modifier =
                Modifier.clip(RoundedCornerShape(20.dp))
                  .background(MaterialTheme.colorScheme.primary)
                  .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
              Text(
                text = userAccount.role,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))
          Text(
            text = salonConfig.nomeSalao,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = salonConfig.endereco,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // MENU LINKS AS REQUIRED
      DrawerSectionTitle("ATENDIMENTO")
      DrawerMenuItem(
        title = "Agendar Atendimento",
        icon = Icons.Default.AddCircle,
        isSelected = false,
        isHighlight = true,
        onClick = {
          onCloseDrawer()
          onNewAppointmentClick()
        }
      )
      DrawerMenuItem(
        title = "Agenda",
        icon = Icons.Default.CalendarMonth,
        isSelected = currentRoute == Screen.Agenda.route,
        onClick = {
          onNavigate(Screen.Agenda.route)
          onCloseDrawer()
        }
      )
      DrawerMenuItem(
        title = "Clientes",
        icon = Icons.Default.People,
        isSelected = currentRoute == Screen.Clients.route,
        onClick = {
          onNavigate(Screen.Clients.route)
          onCloseDrawer()
        }
      )
      DrawerMenuItem(
        title = "Funcionários",
        icon = Icons.Default.Person,
        isSelected = currentRoute == Screen.Employees.route,
        onClick = {
          onNavigate(Screen.Employees.route)
          onCloseDrawer()
        }
      )

      DrawerSectionTitle("GESTÃO & NEGÓCIOS")
      DrawerMenuItem(
        title = "Financeiro",
        icon = Icons.Default.MonetizationOn,
        isSelected = currentRoute == Screen.Financial.route,
        onClick = {
          onNavigate(Screen.Financial.route)
          onCloseDrawer()
        }
      )
      DrawerMenuItem(
        title = "Relatórios",
        icon = Icons.Default.Analytics,
        isSelected = currentRoute == Screen.Reports.route,
        onClick = {
          onNavigate(Screen.Reports.route)
          onCloseDrawer()
        }
      )
      DrawerMenuItem(
        title = "Promoções",
        icon = Icons.Default.CardGiftcard,
        isSelected = currentRoute == Screen.Promotions.route,
        onClick = {
          onNavigate(Screen.Promotions.route)
          onCloseDrawer()
        }
      )

      DrawerSectionTitle("CATÁLOGO & ESTOQUE")
      DrawerMenuItem(
        title = "Serviços",
        icon = Icons.Default.Shop,
        isSelected = currentRoute == Screen.Services.route,
        onClick = {
          onNavigate(Screen.Services.route)
          onCloseDrawer()
        }
      )
      DrawerMenuItem(
        title = "Produtos",
        icon = Icons.Default.ShoppingBag,
        isSelected = currentRoute == Screen.Products.route,
        onClick = {
          onNavigate(Screen.Products.route)
          onCloseDrawer()
        }
      )
      DrawerMenuItem(
        title = "Estoque",
        icon = Icons.Default.Inventory,
        isSelected = currentRoute == Screen.Stock.route,
        onClick = {
          onNavigate(Screen.Stock.route)
          onCloseDrawer()
        }
      )
      DrawerMenuItem(
        title = "Comprar Produtos",
        icon = Icons.Default.ShoppingBag,
        isSelected = false,
        onClick = {
          onNavigate(Screen.Stock.route)
          onCloseDrawer()
        }
      )

      Divider(
        modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
      )

      DrawerSectionTitle("SISTEMA")
      DrawerMenuItem(
        title = "Configurações",
        icon = Icons.Default.Settings,
        isSelected = currentRoute == Screen.Settings.route,
        onClick = {
          onNavigate(Screen.Settings.route)
          onCloseDrawer()
        }
      )
      DrawerMenuItem(
        title = "Ajuda",
        icon = Icons.Default.HelpOutline,
        isSelected = currentRoute == Screen.Help.route,
        onClick = {
          onNavigate(Screen.Help.route)
          onCloseDrawer()
        }
      )
      DrawerMenuItem(
        title = "Sobre",
        icon = Icons.Default.Info,
        isSelected = currentRoute == Screen.About.route,
        onClick = {
          onNavigate(Screen.About.route)
          onCloseDrawer()
        }
      )

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
fun DrawerSectionTitle(text: String) {
  Text(
    text = text,
    fontSize = 11.sp,
    fontWeight = FontWeight.Bold,
    color = MaterialTheme.colorScheme.primary,
    modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 6.dp)
  )
}

@Composable
fun DrawerMenuItem(
  title: String,
  icon: ImageVector,
  isSelected: Boolean,
  isHighlight: Boolean = false,
  onClick: () -> Unit
) {
  val bgColor =
    when {
      isHighlight -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
      isSelected -> MaterialTheme.colorScheme.surfaceVariant
      else -> Color.Transparent
    }
  val textColor =
    when {
      isHighlight -> MaterialTheme.colorScheme.primary
      isSelected -> MaterialTheme.colorScheme.primary
      else -> MaterialTheme.colorScheme.onSurface
    }

  Row(
    modifier =
      Modifier.fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 3.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(bgColor)
        .clickable { onClick() }
        .padding(horizontal = 14.dp, vertical = 12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = title,
      tint = textColor,
      modifier = Modifier.size(22.dp)
    )
    Spacer(modifier = Modifier.width(14.dp))
    Text(
      text = title,
      style = MaterialTheme.typography.bodyLarge,
      fontWeight = if (isSelected || isHighlight) FontWeight.Bold else FontWeight.Medium,
      color = textColor
    )
  }
}
