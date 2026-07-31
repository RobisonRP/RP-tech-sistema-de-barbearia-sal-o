package com.example.data.room

import com.example.data.model.AppointmentEntity
import com.example.data.model.AppointmentStatus
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
import com.example.data.model.UserRole
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DatabaseInitializer {

  suspend fun seedIfEmpty(dao: SalonDao) {
    val dateFmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val todayIso = dateFmt.format(Calendar.getInstance().time)

    // Save initial SalonConfig and UserAccount
    dao.saveSalonConfig(
      SalonConfigEntity(
        id = 1,
        nomeSalao = "RP TECH Gestão",
        endereco = "Av. Paulista, 1400 - Cerqueira César, SP",
        telefone = "(11) 3344-5566",
        whatsapp = "(11) 99888-7766",
        instagram = "@rptech_gestao",
        facebook = "/rptechgestao",
        site = "www.rptechgestao.com.br",
        horarioFuncionamento = "08:00 às 20:00",
        diasAtendimento = "Segunda a Sábado",
        tempoPadraoServicoMinutos = 45,
        moeda = "R$",
        fusoHorario = "America/Sao_Paulo",
        isDarkTheme = true
      )
    )

    dao.saveUserAccount(
      UserAccountEntity(
        id = 1,
        nome = "Ricardo Polidoro",
        email = "Polidoro.RP@gmail.com",
        telefone = "(11) 98765-4321",
        role = UserRole.ADMIN.name,
        loginProvider = "GOOGLE",
        isLoggedIn = true,
        pushNotificationsEnabled = true,
        soundEnabled = true
      )
    )

    // Seed Services
    val s1 =
      ServiceEntity(
        nome = "Corte Moderno Fade + Lavagem",
        categoria = "Barba",
        descricao = "Corte masculino com degradê na navalha e finalização com pomada matte.",
        tempoEstimadoMinutos = 40,
        preco = 65.0,
        funcionariosHabilitados = "Lucas, Marco",
        isAtivo = true,
        isFavorite = true,
        totalVendas = 142
      )
    val s2 =
      ServiceEntity(
        nome = "Barba Terapia com Toalha Quente",
        categoria = "Barba",
        descricao = "Esfoliação facial, hidratação profunda da barba e design na navalha.",
        tempoEstimadoMinutos = 35,
        preco = 55.0,
        funcionariosHabilitados = "Lucas, Marco",
        isAtivo = true,
        isFavorite = true,
        totalVendas = 98
      )
    val s3 =
      ServiceEntity(
        nome = "Corte Feminino + Escova Modeladora",
        categoria = "Cabelo",
        descricao = "Corte estilizado em camadas ou reto, hidratação premium e escova.",
        tempoEstimadoMinutos = 60,
        preco = 140.0,
        funcionariosHabilitados = "Fernanda, Sofia",
        isAtivo = true,
        isFavorite = true,
        totalVendas = 210
      )
    val s4 =
      ServiceEntity(
        nome = "Coloração Global & Mechas",
        categoria = "Coloração",
        descricao = "Coloração sem amônia, mechas iluminadas e tratamento de reconstrução.",
        tempoEstimadoMinutos = 120,
        preco = 380.0,
        funcionariosHabilitados = "Fernanda",
        isAtivo = true,
        isFavorite = false,
        totalVendas = 64
      )
    val s5 =
      ServiceEntity(
        nome = "Manicure Completa + Esmaltação em Gel",
        categoria = "Manicure",
        descricao = "Cutilagem de alta precisão, hidratação e esmalte em gel de longa duração.",
        tempoEstimadoMinutos = 50,
        preco = 75.0,
        funcionariosHabilitados = "Camila, Mariana",
        isAtivo = true,
        isFavorite = true,
        totalVendas = 320
      )
    val s6 =
      ServiceEntity(
        nome = "Design de Sobrancelha com Henna",
        categoria = "Sobrancelha",
        descricao = "Alinhamento perfeito, mapeamento facial e aplicação de henna natural.",
        tempoEstimadoMinutos = 30,
        preco = 45.0,
        funcionariosHabilitados = "Sofia, Camila",
        isAtivo = true,
        isFavorite = true,
        totalVendas = 185
      )
    val s7 =
      ServiceEntity(
        nome = "Limpeza de Pele Profunda + Peeling",
        categoria = "Estética",
        descricao = "Extração indolor, alta frequência e máscara calmante revitalizante.",
        tempoEstimadoMinutos = 75,
        preco = 190.0,
        funcionariosHabilitados = "Sofia",
        isAtivo = true,
        isFavorite = false,
        totalVendas = 53
      )

    dao.insertService(s1)
    dao.insertService(s2)
    dao.insertService(s3)
    dao.insertService(s4)
    dao.insertService(s5)
    dao.insertService(s6)
    dao.insertService(s7)

    // Seed Employees
    val e1 =
      EmployeeEntity(
        nome = "Lucas Andrade",
        funcao = "Barbeiro",
        especialidade = "Cortes Fade & Barba Terapia",
        telefone = "(11) 97111-2233",
        email = "lucas.barber@bellus.com",
        comissaoPercentual = 35.0,
        diasTrabalhados = "Seg - Sáb",
        horarioTrabalho = "09:00 - 19:00",
        status = "Ativo",
        totalAtendimentos = 184,
        faturamentoGerado = 11960.0
      )
    val e2 =
      EmployeeEntity(
        nome = "Fernanda Sampaio",
        funcao = "Cabeleireiro",
        especialidade = "Mechas, Coloração e Cortes",
        telefone = "(11) 97222-3344",
        email = "fernanda.hair@bellus.com",
        comissaoPercentual = 40.0,
        diasTrabalhados = "Ter - Sáb",
        horarioTrabalho = "10:00 - 20:00",
        status = "Ativo",
        totalAtendimentos = 120,
        faturamentoGerado = 24600.0
      )
    val e3 =
      EmployeeEntity(
        nome = "Camila Ribeiro",
        funcao = "Manicure",
        especialidade = "Nail Designer & Esmaltação em Gel",
        telefone = "(11) 97333-4455",
        email = "camila.nails@bellus.com",
        comissaoPercentual = 35.0,
        diasTrabalhados = "Seg - Sáb",
        horarioTrabalho = "09:00 - 18:00",
        status = "Ativo",
        totalAtendimentos = 210,
        faturamentoGerado = 15750.0
      )
    val e4 =
      EmployeeEntity(
        nome = "Sofia Mendes",
        funcao = "Esteticista",
        especialidade = "Estética Facial, Sobrancelha & Peeling",
        telefone = "(11) 97444-5566",
        email = "sofia.estetica@bellus.com",
        comissaoPercentual = 30.0,
        diasTrabalhados = "Seg - Sex",
        horarioTrabalho = "08:00 - 17:00",
        status = "Ativo",
        totalAtendimentos = 95,
        faturamentoGerado = 13800.0
      )

    val emp1Id = dao.insertEmployee(e1).toInt()
    val emp2Id = dao.insertEmployee(e2).toInt()
    val emp3Id = dao.insertEmployee(e3).toInt()
    val emp4Id = dao.insertEmployee(e4).toInt()

    // Seed Clients
    val c1 =
      ClientEntity(
        nome = "Ana Paula Duarte",
        telefone = "(11) 98111-0011",
        whatsapp = "(11) 98111-0011",
        email = "anapaula.duarte@gmail.com",
        cpf = "342.123.456-78",
        nascimento = "1992-05-14",
        endereco = "Rua Augusta, 450 - SP",
        observacoes = "Prefere café sem açúcar. Cliente VIP há 3 anos.",
        isFavorite = true,
        totalVisitas = 18,
        totalGasto = 2850.0,
        ultimaVisita = todayIso
      )
    val c2 =
      ClientEntity(
        nome = "Guilherme Santos",
        telefone = "(11) 98222-0022",
        whatsapp = "(11) 98222-0022",
        email = "guilherme.santos@outlook.com",
        cpf = "412.333.555-88",
        nascimento = "1988-11-23",
        endereco = "Alameda Santos, 1200 - SP",
        observacoes = "Gosta do corte com degradê na máquina zero.",
        isFavorite = true,
        totalVisitas = 24,
        totalGasto = 1560.0,
        ultimaVisita = todayIso
      )
    val c3 =
      ClientEntity(
        nome = "Beatriz Oliveira",
        telefone = "(11) 98333-0033",
        whatsapp = "(11) 98333-0033",
        email = "beatriz.oliveira@uol.com.br",
        cpf = "289.444.666-11",
        nascimento = "1995-08-09",
        endereco = "Rua Haddock Lobo, 890 - SP",
        observacoes = "Alérgica a esmaltes com tolueno.",
        isFavorite = false,
        totalVisitas = 7,
        totalGasto = 680.0,
        ultimaVisita = todayIso
      )
    val c4 =
      ClientEntity(
        nome = "Carlos Eduardo Lima",
        telefone = "(11) 98444-0044",
        whatsapp = "(11) 98444-0044",
        email = "ceduardo.lima@gmail.com",
        cpf = "155.777.888-00",
        nascimento = "1985-02-17",
        endereco = "Av. Brasil, 2100 - SP",
        observacoes = "Barba Terapia e hidratação.",
        isFavorite = false,
        totalVisitas = 5,
        totalGasto = 325.0,
        ultimaVisita = todayIso
      )

    val cli1Id = dao.insertClient(c1).toInt()
    val cli2Id = dao.insertClient(c2).toInt()
    val cli3Id = dao.insertClient(c3).toInt()
    val cli4Id = dao.insertClient(c4).toInt()

    // Seed Today's Appointments (for realistic schedule & dashboard)
    dao.insertAppointment(
      AppointmentEntity(
        clienteId = cli1Id,
        clienteNome = "Ana Paula Duarte",
        clienteTelefone = "(11) 98111-0011",
        funcionarioId = emp2Id,
        funcionarioNome = "Fernanda Sampaio",
        servicosNomes = "Corte Feminino + Escova Modeladora",
        valorTotal = 140.0,
        dataIso = todayIso,
        horarioInicio = "09:00",
        horarioFim = "10:00",
        status = AppointmentStatus.FINALIZADO.name,
        formaPagamento = "PIX",
        observacoes = "Cliente chegou no horário. Atendimento perfeito."
      )
    )

    dao.insertAppointment(
      AppointmentEntity(
        clienteId = cli2Id,
        clienteNome = "Guilherme Santos",
        clienteTelefone = "(11) 98222-0022",
        funcionarioId = emp1Id,
        funcionarioNome = "Lucas Andrade",
        servicosNomes = "Corte Moderno Fade + Barba Terapia",
        valorTotal = 120.0,
        dataIso = todayIso,
        horarioInicio = "10:30",
        horarioFim = "11:45",
        status = AppointmentStatus.FINALIZADO.name,
        formaPagamento = "Cartão Crédito",
        observacoes = "Agendamento regular quinzenal."
      )
    )

    dao.insertAppointment(
      AppointmentEntity(
        clienteId = cli3Id,
        clienteNome = "Beatriz Oliveira",
        clienteTelefone = "(11) 98333-0033",
        funcionarioId = emp3Id,
        funcionarioNome = "Camila Ribeiro",
        servicosNomes = "Manicure Completa + Esmaltação em Gel",
        valorTotal = 75.0,
        dataIso = todayIso,
        horarioInicio = "14:00",
        horarioFim = "14:50",
        status = AppointmentStatus.EM_ATENDIMENTO.name,
        formaPagamento = "PIX",
        observacoes = "Esmalte cor Cereja Imperial."
      )
    )

    dao.insertAppointment(
      AppointmentEntity(
        clienteId = cli4Id,
        clienteNome = "Carlos Eduardo Lima",
        clienteTelefone = "(11) 98444-0044",
        funcionarioId = emp1Id,
        funcionarioNome = "Lucas Andrade",
        servicosNomes = "Barba Terapia com Toalha Quente",
        valorTotal = 55.0,
        dataIso = todayIso,
        horarioInicio = "16:00",
        horarioFim = "16:35",
        status = AppointmentStatus.CONFIRMADO.name,
        formaPagamento = "Dinheiro",
        observacoes = "Confirmado via WhatsApp automático."
      )
    )

    dao.insertAppointment(
      AppointmentEntity(
        clienteId = cli1Id,
        clienteNome = "Ana Paula Duarte",
        clienteTelefone = "(11) 98111-0011",
        funcionarioId = emp4Id,
        funcionarioNome = "Sofia Mendes",
        servicosNomes = "Design de Sobrancelha com Henna",
        valorTotal = 45.0,
        dataIso = todayIso,
        horarioInicio = "18:00",
        horarioFim = "18:30",
        status = AppointmentStatus.AGENDADO.name,
        formaPagamento = "PIX",
        observacoes = "Agendado para o final da tarde."
      )
    )

    // Seed Products
    dao.insertProduct(
      ProductEntity(
        nome = "Pomada Matte Urban Styling 100g",
        categoria = "Barba",
        marca = "Brave Men",
        fornecedor = "Cosméticos SP",
        codigo = "PRD-01",
        codigoBarras = "78910001001",
        valorCompra = 22.0,
        valorVenda = 48.0,
        quantidade = 18,
        estoqueMinimo = 5,
        validade = "2027-08-30",
        totalVendido = 42
      )
    )

    dao.insertProduct(
      ProductEntity(
        nome = "Shampoo Repair Ouro Argan 500ml",
        categoria = "Cabelo",
        marca = "Luminous Pro",
        fornecedor = "DermoHair Brasil",
        codigo = "PRD-02",
        codigoBarras = "78910001002",
        valorCompra = 45.0,
        valorVenda = 98.0,
        quantidade = 4, // Estoque Baixo alarme!
        estoqueMinimo = 6,
        validade = "2026-11-15",
        totalVendido = 65
      )
    )

    dao.insertProduct(
      ProductEntity(
        nome = "Óleo para Barba Wood & Cedro 30ml",
        categoria = "Barba",
        marca = "Brave Men",
        fornecedor = "Cosméticos SP",
        codigo = "PRD-03",
        codigoBarras = "78910001003",
        valorCompra = 18.0,
        valorVenda = 39.0,
        quantidade = 3, // Estoque Baixo alarme!
        estoqueMinimo = 5,
        validade = "2027-04-10",
        totalVendido = 31
      )
    )

    dao.insertProduct(
      ProductEntity(
        nome = "Kit Esmalte Gel Hipoalergênico - Coleção Outono",
        categoria = "Manicure",
        marca = "ColorGlam",
        fornecedor = "Nails Express",
        codigo = "PRD-04",
        codigoBarras = "78910001004",
        valorCompra = 60.0,
        valorVenda = 135.0,
        quantidade = 12,
        estoqueMinimo = 4,
        validade = "2028-01-20",
        totalVendido = 28
      )
    )

    // Seed Financial Transactions
    dao.insertTransaction(
      FinancialTransactionEntity(
        dataIso = todayIso,
        hora = "09:55",
        tipo = "ENTRADA",
        categoria = "Serviço",
        formaPagamento = "PIX",
        valor = 140.0,
        descricao = "Corte Feminino + Escova - Ana Paula Duarte",
        funcionarioNome = "Fernanda Sampaio",
        clienteNome = "Ana Paula Duarte"
      )
    )

    dao.insertTransaction(
      FinancialTransactionEntity(
        dataIso = todayIso,
        hora = "11:50",
        tipo = "ENTRADA",
        categoria = "Serviço",
        formaPagamento = "Cartão Crédito",
        valor = 120.0,
        descricao = "Corte Moderno Fade + Barba - Guilherme Santos",
        funcionarioNome = "Lucas Andrade",
        clienteNome = "Guilherme Santos"
      )
    )

    dao.insertTransaction(
      FinancialTransactionEntity(
        dataIso = todayIso,
        hora = "12:30",
        tipo = "SAIDA",
        categoria = "Despesa",
        formaPagamento = "PIX",
        valor = 85.0,
        descricao = "Reposição de toalhas e suprimentos de esterilização",
        funcionarioNome = "Administração",
        clienteNome = "Fornecedor CleanSP"
      )
    )

    // Seed Promotions
    dao.insertPromotion(
      PromotionEntity(
        nome = "Semana do Combo Barba + Cabelo",
        descricao = "Desconto especial ao agendar corte de cabelo e barba terapia juntos na terça e quarta.",
        percentualDesconto = 20.0,
        valorFixoDesconto = 0.0,
        cupom = "COMBO20",
        validadeIso = "2026-10-31",
        quantidadeMaxima = 50,
        quantidadeUsada = 14,
        categoriasParticipantes = "Barba, Cabelo",
        status = "Ativa"
      )
    )

    dao.insertPromotion(
      PromotionEntity(
        nome = "Primeira Visita Estética Facial",
        descricao = "Desconto de R$ 30,00 na sua primeira sessão de limpeza de pele ou peeling facial.",
        percentualDesconto = 0.0,
        valorFixoDesconto = 30.0,
        cupom = "BEMVINDO30",
        validadeIso = "2026-12-31",
        quantidadeMaxima = 100,
        quantidadeUsada = 28,
        categoriasParticipantes = "Estética",
        status = "Ativa"
      )
    )

    // Seed Notifications
    dao.insertNotification(
      NotificationEntity(
        titulo = "Estoque Baixo Detectado",
        mensagem = "O produto 'Shampoo Repair Ouro Argan 500ml' atingiu apenas 4 unidades no estoque.",
        timestampIso = "$todayIso 08:30",
        isLida = false,
        categoria = "ESTOQUE"
      )
    )

    dao.insertNotification(
      NotificationEntity(
        titulo = "Próximo Cliente em 30 min",
        mensagem = "Carlos Eduardo Lima agendado com Lucas Andrade às 16:00 - Barba Terapia.",
        timestampIso = "$todayIso 15:30",
        isLida = false,
        categoria = "PROXIMO"
      )
    )

    dao.insertNotification(
      NotificationEntity(
        titulo = "Aniversariante da Semana",
        mensagem = "Guilherme Santos faz aniversário nesta sexta-feira! Envie o cupom BEMVINDO30.",
        timestampIso = "$todayIso 09:00",
        isLida = false,
        categoria = "ANIVERSARIO"
      )
    )

    // Seed Action Logs
    dao.insertLogAction(
      LogActionEntity(
        usuario = "Ricardo Polidoro (Admin)",
        acao = "INICIALIZAÇÃO",
        detalhe = "Banco de dados local inicializado com sucesso e dados padrão carregados.",
        dataHoraIso = "$todayIso 08:00"
      )
    )
  }
}
