package com.example.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R

@Composable
fun WelcomeScreen(
  onNavigateToLogin: () -> Unit,
  onNavigateToRegister: () -> Unit
) {
  Scaffold { innerPadding ->
    Box(
      modifier =
        Modifier.fillMaxSize()
          .padding(innerPadding)
          .background(MaterialTheme.colorScheme.background)
    ) {
      Column(
        modifier =
          Modifier.fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
      ) {
        Spacer(modifier = Modifier.height(16.dp))

        // 1. Logo & Name
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Box(
            modifier =
              Modifier.size(90.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(Color.Black)
                .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(22.dp)),
            contentAlignment = Alignment.Center
          ) {
            Image(
              painter = painterResource(id = R.drawable.img_app_icon_rptech),
              contentDescription = "Logo",
              contentScale = ContentScale.Crop,
              modifier = Modifier.size(90.dp).clip(RoundedCornerShape(22.dp))
            )
          }

          Spacer(modifier = Modifier.height(18.dp))
          Text(
            text = "RP TECH",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 1.2.sp
          )
          Text(
            text = "GESTÃO PROFISSIONAL & TECNOLOGIA",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 1.sp
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Modern Hero Illustration
        Surface(
          modifier = Modifier.fillMaxWidth().height(210.dp),
          shape = RoundedCornerShape(24.dp),
          tonalElevation = 4.dp
        ) {
          Image(
            painter = painterResource(id = R.drawable.img_welcome_hero),
            contentDescription = "Salão de Beleza e Barbearia Moderno",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Welcome Message
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "Gestão Integrada para o Seu Negócio",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text =
              "Controle completo de clientes, agenda inteligente em tempo real, financeiro, comissões de profissionais e promoções automatizadas.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
          )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4. Buttons Entrar & Criar Conta
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Button(
            onClick = onNavigateToLogin,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors =
              ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
              )
          ) {
            Text(
              text = "ENTRAR NO SISTEMA",
              fontWeight = FontWeight.ExtraBold,
              fontSize = 15.sp
            )
          }

          Surface(
            onClick = onNavigateToRegister,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 2.dp
          ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
              Text(
                text = "CRIAR NOVA CONTA",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
  onLoginSuccess: (provider: String, email: String) -> Unit,
  onNavigateToRegister: () -> Unit,
  onNavigateToForgot: () -> Unit,
  onBack: () -> Unit
) {
  var email by remember { mutableStateOf("polidoro.rp@gmail.com") }
  var password by remember { mutableStateOf("admin123") }
  var rememberMe by remember { mutableStateOf(true) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Acesso ao Sistema") },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
          }
        },
        colors =
          TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
          )
      )
    }
  ) { innerPadding ->
    Column(
      modifier =
        Modifier.fillMaxSize()
          .padding(innerPadding)
          .padding(24.dp)
          .verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Spacer(modifier = Modifier.height(8.dp))

      Box(
        modifier =
          Modifier.size(70.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.ContentCut,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(36.dp)
        )
      }

      Text(
        text = "Bem-vindo de volta",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.onBackground
      )
      Text(
        text = "Acesse sua agenda e métricas em tempo real",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(8.dp))

      // SOCIAL LOGIN BUTTONS (Google, Facebook)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Surface(
          onClick = { onLoginSuccess("GOOGLE", "usuario.google@gmail.com") },
          modifier = Modifier.weight(1f).height(50.dp),
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant,
          tonalElevation = 2.dp
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Text(
              text = "G",
              fontWeight = FontWeight.ExtraBold,
              fontSize = 18.sp,
              color = Color(0xFF4285F4)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Google", fontWeight = FontWeight.Bold)
          }
        }

        Surface(
          onClick = { onLoginSuccess("FACEBOOK", "usuario.facebook@gmail.com") },
          modifier = Modifier.weight(1f).height(50.dp),
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant,
          tonalElevation = 2.dp
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Text(
              text = "f",
              fontWeight = FontWeight.ExtraBold,
              fontSize = 18.sp,
              color = Color(0xFF1877F2)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Facebook", fontWeight = FontWeight.Bold)
          }
        }
      }

      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
      ) {
        Box(
          modifier =
            Modifier.weight(1f)
              .height(1.dp)
              .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
        )
        Text(
          text = "ou com e-mail e senha",
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(horizontal = 12.dp)
        )
        Box(
          modifier =
            Modifier.weight(1f)
              .height(1.dp)
              .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
        )
      }

      // EMAIL & PASSWORD FIELD
      OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("E-mail do Salão") },
        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
      )

      OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Senha") },
        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.clickable { rememberMe = !rememberMe }
        ) {
          Checkbox(
            checked = rememberMe,
            onCheckedChange = { rememberMe = it },
            colors =
              CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary
              )
          )
          Text("Permanecer conectado", fontSize = 13.sp)
        }

        TextButton(onClick = onNavigateToForgot) {
          Text(
            text = "Esqueci a senha",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.primary
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Button(
        onClick = { onLoginSuccess("EMAIL", email) },
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors =
          ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
          )
      ) {
        Text(text = "ENTRAR NO DASHBOARD", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Não tem uma conta?", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        TextButton(onClick = onNavigateToRegister) {
          Text("Criar conta grátis", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
  onRegisterSuccess: (email: String) -> Unit,
  onNavigateToLogin: () -> Unit,
  onBack: () -> Unit
) {
  var name by remember { mutableStateOf("") }
  var email by remember { mutableStateOf("") }
  var phone by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Criar Nova Conta") },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
          }
        }
      )
    }
  ) { innerPadding ->
    Column(
      modifier =
        Modifier.fillMaxSize()
          .padding(innerPadding)
          .padding(24.dp)
          .verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Text(
        text = "Cadastre seu Salão ou Barbearia",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
      Text(
        text = "Comece com 14 dias gratuitos do plano completo",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      OutlinedTextField(
        value = name,
        onValueChange = { name = it },
        label = { Text("Nome do Negócio ou Responsável") },
        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
        modifier = Modifier.fillMaxWidth()
      )

      OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("E-mail profissional") },
        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
        modifier = Modifier.fillMaxWidth()
      )

      OutlinedTextField(
        value = phone,
        onValueChange = { phone = it },
        label = { Text("WhatsApp do Salão") },
        modifier = Modifier.fillMaxWidth()
      )

      OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Crie uma Senha Forte") },
        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(16.dp))

      Button(
        onClick = { onRegisterSuccess(email.ifBlank { "novo.salao@gmail.com" }) },
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(16.dp)
      ) {
        Text("CRIAR CONTA AGORA", fontWeight = FontWeight.Bold)
      }

      TextButton(onClick = onNavigateToLogin) {
        Text("Já possui conta? Fazer login")
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
  onSendRecovery: () -> Unit,
  onBack: () -> Unit
) {
  var email by remember { mutableStateOf("") }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Recuperar Senha") },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
          }
        }
      )
    }
  ) { innerPadding ->
    Column(
      modifier =
        Modifier.fillMaxSize()
          .padding(innerPadding)
          .padding(24.dp)
          .verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Text(
        text = "Redefinição de Senha",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
      )
      Text(
        text =
          "Digite o e-mail associado à conta do salão. Enviaremos um link seguro para cadastrar uma nova senha.",
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("E-mail cadastrado") },
        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(16.dp))

      Button(
        onClick = onSendRecovery,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(16.dp)
      ) {
        Text("ENVIAR LINK DE RECUPERAÇÃO", fontWeight = FontWeight.Bold)
      }
    }
  }
}
