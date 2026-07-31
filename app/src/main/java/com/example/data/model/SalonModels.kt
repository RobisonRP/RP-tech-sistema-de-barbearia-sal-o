package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class AppointmentStatus(val label: String, val colorHex: Long) {
  AGENDADO("Agendado", 0xFF2B7CD3),
  CONFIRMADO("Confirmado", 0xFF2E8B57),
  EM_ATENDIMENTO("Em Atendimento", 0xFFD48806),
  FINALIZADO("Finalizado", 0xFF388E3C),
  CANCELADO("Cancelado", 0xFFD32F2F),
  NAO_COMPARECEU("Não Compareceu", 0xFF7B1FA2)
}

enum class UserRole(val label: String) {
  ADMIN("Administrador"),
  RECEPTION("Recepção"),
  PROFESSIONAL("Profissional")
}

@Entity(tableName = "clients")
data class ClientEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val nome: String,
  val telefone: String,
  val whatsapp: String = "",
  val email: String = "",
  val cpf: String = "",
  val nascimento: String = "",
  val endereco: String = "",
  val fotoUrl: String = "",
  val observacoes: String = "",
  val isFavorite: Boolean = false,
  val totalVisitas: Int = 0,
  val totalGasto: Double = 0.0,
  val ultimaVisita: String = ""
) {
  val ticketMedio: Double
    get() = if (totalVisitas > 0) totalGasto / totalVisitas else 0.0
}

@Entity(tableName = "employees")
data class EmployeeEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val nome: String,
  val fotoUrl: String = "",
  val funcao: String, // e.g. "Barbeiro", "Cabeleireiro", "Manicure", "Esteticista", etc.
  val especialidade: String = "",
  val especialidades: String = "",
  val telefone: String = "",
  val email: String = "",
  val comissaoPercentual: Double = 30.0,
  val comissao: Double = 30.0,
  val diasTrabalhados: String = "Seg - Sáb",
  val diasTrabalho: String = "Seg - Sáb",
  val horarioTrabalho: String = "09:00 - 19:00",
  val horariosTrabalho: String = "09:00 - 19:00",
  val status: String = "Ativo", // "Ativo", "Férias", "Folga", "Inativo"
  val avaliacao: String = "5.0 ★",
  val salarioBase: Double = 1800.0,
  val totalAtendimentos: Int = 0,
  val faturamentoGerado: Double = 0.0,
  val totalGerado: Double = 0.0
)

@Entity(tableName = "services")
data class ServiceEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val nome: String,
  val categoria: String, // "Barba", "Cabelo", "Coloração", "Progressiva", "Manicure", "Pedicure", "Estética", "Massagem", "Sobrancelha", "Depilação", "Outros"
  val descricao: String = "",
  val tempoEstimadoMinutos: Int = 45,
  val duracaoMin: Int = 45,
  val preco: Double = 50.0,
  val comissaoEspecífica: Double = 30.0,
  val funcionariosHabilitados: String = "Todos",
  val isAtivo: Boolean = true,
  val isFavorite: Boolean = false,
  val totalVendas: Int = 0
)

@Entity(tableName = "products")
data class ProductEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val nome: String,
  val categoria: String = "Profissional",
  val marca: String = "",
  val fornecedor: String = "",
  val codigo: String = "",
  val codigoBarras: String = "",
  val valorCompra: Double = 0.0,
  val precoCusto: Double = 0.0,
  val valorVenda: Double = 0.0,
  val precoVenda: Double = 0.0,
  val quantidade: Int = 0,
  val estoqueMinimo: Int = 5,
  val validade: String = "",
  val unidade: String = "unidade",
  val isAtivo: Boolean = true,
  val totalVendido: Int = 0
)

@Entity(tableName = "appointments")
data class AppointmentEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val clienteId: Int,
  val clienteNome: String,
  val clienteTelefone: String,
  val funcionarioId: Int,
  val funcionarioNome: String,
  val servicosNomes: String, // ex: "Corte + Barba"
  val servicosIds: String = "",
  val valorTotal: Double = 0.0,
  val dataIso: String, // YYYY-MM-DD
  val horarioInicio: String, // HH:mm
  val horarioFim: String = "10:00",
  val status: String = AppointmentStatus.AGENDADO.name,
  val formaPagamento: String = "PIX",
  val observacoes: String = "",
  val isBloqueado: Boolean = false
)

@Entity(tableName = "promotions")
data class PromotionEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val nome: String,
  val descricao: String,
  val tipo: String = "Desconto",
  val percentualDesconto: Double = 15.0,
  val descontoPercent: Double = 15.0,
  val valorFixoDesconto: Double = 0.0,
  val valorPacote: Double = 0.0,
  val cupom: String,
  val codigoCupom: String = "",
  val validadeIso: String = "2026-12-31",
  val quantidadeMaxima: Int = 100,
  val quantidadeUsada: Int = 12,
  val usosAtuais: Int = 0,
  val categoriasParticipantes: String = "Todos os Serviços",
  val status: String = "Ativa" // "Ativa", "Pausada", "Encerrada"
)

@Entity(tableName = "financial_transactions")
data class FinancialTransactionEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val dataIso: String, // YYYY-MM-DD
  val hora: String = "14:30",
  val tipo: String, // "ENTRADA" ou "SAIDA"
  val categoria: String, // "Serviço", "Produto", "Despesa", "Salário", "Outro"
  val formaPagamento: String, // "PIX", "Dinheiro", "Cartão Débito", "Cartão Crédito", "Transferência", "Voucher", "Vale Presente"
  val valor: Double,
  val descricao: String,
  val funcionarioNome: String = "",
  val clienteNome: String = ""
)

@Entity(tableName = "notifications")
data class NotificationEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val titulo: String,
  val mensagem: String,
  val timestampIso: String,
  val isLida: Boolean = false,
  val categoria: String = "CHEGADA" // "CHEGADA", "PROXIMO", "ESTOQUE", "ANIVERSARIO", "PROMO", "CONTA"
)

@Entity(tableName = "salon_config")
data class SalonConfigEntity(
  @PrimaryKey val id: Int = 1,
  val nomeSalao: String = "RP TECH Gestão",
  val logoUrl: String = "",
  val endereco: String = "Av. Paulista, 1000 - São Paulo, SP",
  val telefone: String = "(11) 3344-5566",
  val whatsapp: String = "(11) 99888-7766",
  val whatsappContato: String = "(11) 99888-7766",
  val instagram: String = "@rptech_gestao",
  val facebook: String = "/rptechgestao",
  val site: String = "www.rptechgestao.com.br",
  val horarioFuncionamento: String = "08:00 às 20:00",
  val diasAtendimento: String = "Segunda a Sábado",
  val tempoPadraoServicoMinutos: Int = 45,
  val agendamentoMaximoDias: Int = 30,
  val lembreteAutomaticoWhatsApp: Boolean = true,
  val moeda: String = "R$",
  val fusoHorario: String = "America/Sao_Paulo",
  val isDarkTheme: Boolean = true
)

@Entity(tableName = "user_account")
data class UserAccountEntity(
  @PrimaryKey val id: Int = 1,
  val nome: String = "Ricardo Polidoro",
  val email: String = "Polidoro.RP@gmail.com",
  val telefone: String = "(11) 98765-4321",
  val fotoUrl: String = "",
  val role: String = UserRole.ADMIN.name,
  val loginProvider: String = "GOOGLE", // GOOGLE, FACEBOOK, EMAIL
  val isLoggedIn: Boolean = true,
  val pushNotificationsEnabled: Boolean = true,
  val soundEnabled: Boolean = true
)

@Entity(tableName = "log_actions")
data class LogActionEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val usuario: String,
  val acao: String,
  val detalhe: String,
  val dataHoraIso: String
)
