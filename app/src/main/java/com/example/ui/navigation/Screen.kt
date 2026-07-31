package com.example.ui.navigation

sealed class Screen(val route: String, val label: String) {
  object Welcome : Screen("welcome", "Boas-Vindas")
  object Login : Screen("login", "Entrar")
  object Register : Screen("register", "Criar Conta")
  object ForgotPassword : Screen("forgot_password", "Recuperar Senha")

  object Dashboard : Screen("dashboard", "Dashboard")
  object Agenda : Screen("agenda", "Agenda")
  object Clients : Screen("clients", "Clientes")
  object ClientDetail : Screen("client_detail/{clientId}", "Detalhes do Cliente") {
    fun createRoute(clientId: Int) = "client_detail/$clientId"
  }
  object Employees : Screen("employees", "Funcionários")
  object Services : Screen("services", "Serviços")
  object Products : Screen("products", "Produtos")
  object Stock : Screen("stock", "Estoque")
  object Financial : Screen("financial", "Financeiro")
  object Promotions : Screen("promotions", "Promoções")
  object Reports : Screen("reports", "Relatórios")
  object GlobalSearch : Screen("global_search", "Pesquisar")
  object Notifications : Screen("notifications", "Notificações")
  object UserProfile : Screen("user_profile", "Meu Perfil")
  object Settings : Screen("settings", "Configurações")
  object Help : Screen("help", "Ajuda")
  object About : Screen("about", "Sobre")
}
