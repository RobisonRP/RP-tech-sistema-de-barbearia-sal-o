package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.components.NewAppointmentModal
import com.example.ui.components.SalonBottomNav
import com.example.ui.components.SalonDrawer
import com.example.ui.components.SalonTopBar
import com.example.ui.navigation.Screen
import com.example.ui.screens.agenda.AgendaScreen
import com.example.ui.screens.auth.ForgotPasswordScreen
import com.example.ui.screens.auth.LoginScreen
import com.example.ui.screens.auth.RegisterScreen
import com.example.ui.screens.auth.WelcomeScreen
import com.example.ui.screens.catalog.ServicesScreen
import com.example.ui.screens.catalog.StockScreen
import com.example.ui.screens.clients.ClientsScreen
import com.example.ui.screens.dashboard.DashboardScreen
import com.example.ui.screens.employees.EmployeesScreen
import com.example.ui.screens.finance.FinancialScreen
import com.example.ui.screens.finance.PromotionsScreen
import com.example.ui.screens.more.AboutScreen
import com.example.ui.screens.more.GlobalSearchScreen
import com.example.ui.screens.more.HelpScreen
import com.example.ui.screens.more.NotificationsScreen
import com.example.ui.screens.more.ReportsScreen
import com.example.ui.screens.more.SettingsScreen
import com.example.ui.screens.more.UserProfileScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.SalonViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val salonViewModel: SalonViewModel = viewModel()
      val salonConfig by salonViewModel.salonConfig.collectAsState()

      MyApplicationTheme(
        darkTheme = salonConfig.isDarkTheme,
        dynamicColor = false
      ) {
        SalonAppRoot(viewModel = salonViewModel)
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalonAppRoot(viewModel: SalonViewModel) {
  val navController = rememberNavController()
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val scope = rememberCoroutineScope()

  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route

  // Collect states from ViewModel
  val clients by viewModel.allClients.collectAsState()
  val employees by viewModel.allEmployees.collectAsState()
  val services by viewModel.allServices.collectAsState()
  val products by viewModel.allProducts.collectAsState()
  val lowStockProducts by viewModel.lowStockProducts.collectAsState()
  val allAppointments by viewModel.allAppointments.collectAsState()
  val promotions by viewModel.allPromotions.collectAsState()
  val transactions by viewModel.allTransactions.collectAsState()
  val notifications by viewModel.allNotifications.collectAsState()
  val salonConfig by viewModel.salonConfig.collectAsState()
  val userAccount by viewModel.userAccount.collectAsState()

  val unreadNotificationsCount = notifications.count { !it.isLida }

  val isNewAppointmentModalOpen by viewModel.isNewAppointmentModalOpen.collectAsState()

  // Selected date for Agenda
  val selectedDateIso by viewModel.selectedDateIso.collectAsState()
  val agendaViewMode by viewModel.agendaViewMode.collectAsState()

  // Client filtering
  val clientSearchQuery by viewModel.clientSearchQuery.collectAsState()
  val serviceCategoryFilter by viewModel.serviceCategoryFilter.collectAsState()
  val employeeRoleFilter by viewModel.employeeRoleFilter.collectAsState()
  val globalSearchQuery by viewModel.globalSearchQuery.collectAsState()

  val isAuthScreen =
    currentRoute == Screen.Welcome.route ||
      currentRoute == Screen.Login.route ||
      currentRoute == Screen.Register.route ||
      currentRoute == Screen.ForgotPassword.route

  val topBarTitle =
    when (currentRoute) {
      Screen.Dashboard.route -> "Painel de Gestão"
      Screen.Agenda.route -> "Agenda Inteligente"
      Screen.Clients.route -> "Meus Clientes"
      Screen.Employees.route -> "Profissionais"
      Screen.Services.route -> "Catálogo de Serviços"
      Screen.Products.route, Screen.Stock.route -> "Controle de Estoque"
      Screen.Financial.route -> "Financeiro & Caixa"
      Screen.Promotions.route -> "Promoções"
      Screen.Reports.route -> "Relatórios Executivos"
      Screen.GlobalSearch.route -> "Pesquisar"
      Screen.Notifications.route -> "Notificações"
      Screen.UserProfile.route -> "Meu Perfil"
      Screen.Settings.route -> "Configurações"
      Screen.Help.route -> "Ajuda & Suporte"
      Screen.About.route -> "Sobre o App"
      else -> salonConfig.nomeSalao
    }

  ModalNavigationDrawer(
    drawerState = drawerState,
    gesturesEnabled = !isAuthScreen,
    drawerContent = {
      if (!isAuthScreen) {
        SalonDrawer(
          currentRoute = currentRoute,
          salonConfig = salonConfig,
          userAccount = userAccount,
          onNavigate = { route ->
            navController.navigate(route) {
              launchSingleTop = true
            }
          },
          onNewAppointmentClick = { viewModel.openNewAppointmentModal() },
          onCloseDrawer = {
            scope.launch { drawerState.close() }
          }
        )
      }
    }
  ) {
    Scaffold(
      topBar = {
        if (!isAuthScreen) {
          SalonTopBar(
            title = topBarTitle,
            unreadNotificationsCount = unreadNotificationsCount,
            isDarkTheme = salonConfig.isDarkTheme,
            onOpenDrawer = {
              scope.launch { drawerState.open() }
            },
            onSearchClick = {
              navController.navigate(Screen.GlobalSearch.route) {
                launchSingleTop = true
              }
            },
            onNotificationsClick = {
              navController.navigate(Screen.Notifications.route) {
                launchSingleTop = true
              }
            },
            onThemeToggle = {
              viewModel.toggleDarkTheme(!salonConfig.isDarkTheme)
            },
            onProfileClick = {
              navController.navigate(Screen.UserProfile.route) {
                launchSingleTop = true
              }
            }
          )
        }
      },
      bottomBar = {
        if (!isAuthScreen) {
          SalonBottomNav(
            currentRoute = currentRoute,
            onNavigate = { route ->
              navController.navigate(route) {
                launchSingleTop = true
                popUpTo(Screen.Dashboard.route) { saveState = true }
              }
            },
            onOpenDrawer = {
              scope.launch { drawerState.open() }
            }
          )
        }
      },
      floatingActionButton = {
        if (!isAuthScreen) {
          FloatingActionButton(
            onClick = { viewModel.openNewAppointmentModal() },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
          ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Novo Agendamento")
          }
        }
      }
    ) { innerPadding ->
      Box(
        modifier =
          Modifier.fillMaxSize()
            .padding(innerPadding)
      ) {
        NavHost(
          navController = navController,
          startDestination = Screen.Dashboard.route
        ) {
          // AUTH SCREENS
          composable(Screen.Welcome.route) {
            WelcomeScreen(
              onNavigateToLogin = { navController.navigate(Screen.Login.route) },
              onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
          }

          composable(Screen.Login.route) {
            LoginScreen(
              onLoginSuccess = { provider, email ->
                viewModel.login(provider, email)
                navController.navigate(Screen.Dashboard.route) {
                  popUpTo(Screen.Welcome.route) { inclusive = true }
                }
              },
              onNavigateToRegister = { navController.navigate(Screen.Register.route) },
              onNavigateToForgot = { navController.navigate(Screen.ForgotPassword.route) },
              onBack = { navController.popBackStack() }
            )
          }

          composable(Screen.Register.route) {
            RegisterScreen(
              onRegisterSuccess = { email ->
                viewModel.login("EMAIL", email)
                navController.navigate(Screen.Dashboard.route) {
                  popUpTo(Screen.Welcome.route) { inclusive = true }
                }
              },
              onNavigateToLogin = { navController.navigate(Screen.Login.route) },
              onBack = { navController.popBackStack() }
            )
          }

          composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
              onSendRecovery = { navController.popBackStack() },
              onBack = { navController.popBackStack() }
            )
          }

          // DASHBOARD
          composable(Screen.Dashboard.route) {
            DashboardScreen(
              clients = clients,
              employees = employees,
              services = services,
              products = products,
              lowStockProducts = lowStockProducts,
              allAppointments = allAppointments,
              todayIso = viewModel.todayIsoString,
              onNewAppointmentClick = { viewModel.openNewAppointmentModal() },
              onNavigateToAgenda = { navController.navigate(Screen.Agenda.route) },
              onNavigateToClients = { navController.navigate(Screen.Clients.route) },
              onNavigateToStock = { navController.navigate(Screen.Stock.route) },
              onNavigateToFinancial = { navController.navigate(Screen.Financial.route) }
            )
          }

          // AGENDA
          composable(Screen.Agenda.route) {
            AgendaScreen(
              allAppointments = allAppointments,
              employees = employees,
              selectedDateIso = selectedDateIso,
              onDateChange = { viewModel.setSelectedDateIso(it) },
              viewMode = agendaViewMode,
              onViewModeChange = { viewModel.setAgendaViewMode(it) },
              onNewAppointmentClick = { viewModel.openNewAppointmentModal() },
              onStatusChange = { appt, newSt -> viewModel.updateAppointmentStatus(appt, newSt) },
              onDeleteAppointment = { viewModel.deleteAppointment(it) }
            )
          }

          // CLIENTS
          composable(Screen.Clients.route) {
            ClientsScreen(
              clients = clients,
              allAppointments = allAppointments,
              searchQuery = clientSearchQuery,
              onSearchQueryChange = { viewModel.setClientSearchQuery(it) },
              onSaveClient = { viewModel.saveClient(it) },
              onDeleteClient = { viewModel.deleteClient(it) },
              onToggleFavorite = { viewModel.toggleClientFavorite(it) },
              onScheduleForClient = { cli ->
                viewModel.openNewAppointmentModal()
              }
            )
          }

          // EMPLOYEES
          composable(Screen.Employees.route) {
            EmployeesScreen(
              employees = employees,
              roleFilter = employeeRoleFilter,
              onRoleFilterChange = { viewModel.setEmployeeRoleFilter(it) },
              onSaveEmployee = { viewModel.saveEmployee(it) },
              onDeleteEmployee = { viewModel.deleteEmployee(it) }
            )
          }

          // SERVICES
          composable(Screen.Services.route) {
            ServicesScreen(
              services = services,
              categoryFilter = serviceCategoryFilter,
              onCategoryFilterChange = { viewModel.setServiceCategoryFilter(it) },
              onSaveService = { viewModel.saveService(it) },
              onDeleteService = { viewModel.deleteService(it) }
            )
          }

          // PRODUCTS & STOCK
          composable(Screen.Products.route) {
            StockScreen(
              products = products,
              lowStockProducts = lowStockProducts,
              onSaveProduct = { viewModel.saveProduct(it) },
              onDeleteProduct = { viewModel.deleteProduct(it) },
              onAdjustStock = { prod, delta -> viewModel.adjustStock(prod, delta) }
            )
          }

          composable(Screen.Stock.route) {
            StockScreen(
              products = products,
              lowStockProducts = lowStockProducts,
              onSaveProduct = { viewModel.saveProduct(it) },
              onDeleteProduct = { viewModel.deleteProduct(it) },
              onAdjustStock = { prod, delta -> viewModel.adjustStock(prod, delta) }
            )
          }

          // FINANCIAL
          composable(Screen.Financial.route) {
            FinancialScreen(
              transactions = transactions,
              employees = employees,
              onSaveTransaction = { viewModel.saveTransaction(it) },
              onDeleteTransaction = { viewModel.deleteTransaction(it) }
            )
          }

          // PROMOTIONS
          composable(Screen.Promotions.route) {
            PromotionsScreen(
              promotions = promotions,
              onSavePromotion = { viewModel.savePromotion(it) },
              onUpdateStatus = { promo, st -> viewModel.updatePromotionStatus(promo, st) }
            )
          }

          // REPORTS
          composable(Screen.Reports.route) {
            ReportsScreen(
              transactions = transactions,
              appointments = allAppointments,
              clients = clients,
              employees = employees
            )
          }

          // GLOBAL SEARCH
          composable(Screen.GlobalSearch.route) {
            GlobalSearchScreen(
              query = globalSearchQuery,
              onQueryChange = { viewModel.setGlobalSearchQuery(it) },
              clients = clients,
              services = services,
              products = products,
              employees = employees,
              appointments = allAppointments,
              onSelectClient = {
                navController.navigate(Screen.Clients.route)
              },
              onSelectService = {
                navController.navigate(Screen.Services.route)
              }
            )
          }

          // NOTIFICATIONS
          composable(Screen.Notifications.route) {
            NotificationsScreen(
              notifications = notifications,
              onMarkAllRead = { viewModel.markAllNotificationsAsRead() }
            )
          }

          // USER PROFILE
          composable(Screen.UserProfile.route) {
            UserProfileScreen(
              userAccount = userAccount,
              onSaveProfile = { viewModel.updateUserAccount(it) },
              onLogout = {
                viewModel.logout()
                navController.navigate(Screen.Welcome.route) {
                  popUpTo(Screen.Dashboard.route) { inclusive = true }
                }
              }
            )
          }

          // SETTINGS
          composable(Screen.Settings.route) {
            SettingsScreen(
              salonConfig = salonConfig,
              onSaveConfig = { viewModel.updateSalonConfig(it) },
              isDarkTheme = salonConfig.isDarkTheme,
              onToggleTheme = { viewModel.toggleDarkTheme(it) },
              onExportBackup = {
                // backup triggered
              },
              onRestoreBackup = {
                // restore triggered
              }
            )
          }

          // HELP
          composable(Screen.Help.route) {
            HelpScreen()
          }

          // ABOUT
          composable(Screen.About.route) {
            AboutScreen()
          }
        }

        // NEW APPOINTMENT FLOATING MODAL DIALOG
        if (isNewAppointmentModalOpen) {
          NewAppointmentModal(
            clients = clients,
            employees = employees,
            services = services,
            initialDateIso = viewModel.todayIsoString,
            onDismiss = { viewModel.closeNewAppointmentModal() },
            onConfirm = { client, emp, selServices, dataIso, hora, formPag, obs, valTot ->
              viewModel.saveNewAppointment(
                client = client,
                employee = emp,
                services = selServices,
                dataIso = dataIso,
                horarioInicio = hora,
                formaPagamento = formPag,
                observacoes = obs,
                valorTotalCustom = valTot
              )
            }
          )
        }
      }
    }
  }
}
