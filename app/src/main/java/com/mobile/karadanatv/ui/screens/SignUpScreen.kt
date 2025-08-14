package com.mobile.karadanatv.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mobile.karadanatv.DestinationScreen
import com.mobile.karadanatv.ui.components.CustomAppBar
import com.mobile.karadanatv.ui.components.CustomOutlinedTextField
import com.mobile.karadanatv.ui.theme.Background
import com.mobile.karadanatv.utils.CheckSignedIn
import com.mobile.karadanatv.utils.ObserveErrorMessage
import com.mobile.karadanatv.utils.navigateTo
import com.mobile.karadanatv.viewmodels.AuthViewModel


@Composable
fun SignUpScreen(
    navController: NavController, authViewModel: AuthViewModel
) {
    val errorMessage by authViewModel.errorMessage

    ObserveErrorMessage(errorMessage)
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var generalErrorMessage by remember { mutableStateOf<String?>(null) }

    var isLoading by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()



    CheckSignedIn(authViewModel, navController)



    Scaffold(topBar = {
        CustomAppBar(
            title = "Kayıt Ol",
            showBackButton = true,
            onBackClicked = {
                navController.popBackStack()
            })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))



            Text(
                text = "Tekrar Hoş Geldiniz!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Hesabınıza giriş yapın veya yeni bir hesap oluşturun.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )


            CustomOutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = null
                    generalErrorMessage = null
                },
                label = "İsim Soyisim",
                leadingIcon = Icons.Default.Person,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }),
                isError = nameError != null,
                errorMessage = nameError
            )


            CustomOutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                    generalErrorMessage = null
                },
                label = "E-posta Adresi",
                leadingIcon = Icons.Default.Email,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }),
                isError = emailError != null,
                errorMessage = emailError
            )

            CustomOutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                    generalErrorMessage = null
                },
                label = "Şifre",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Sharp.Search
                    else Icons.Sharp.Search

                    val description = if (passwordVisible) "Şifreyi Gizle" else "Şifreyi Göster"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    authViewModel.signUp(name = name, email = email, password = password)
                }),
                isError = passwordError != null,
                errorMessage = passwordError
            )

            Text(
                text = "Şifrenizi mi unuttunuz?",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 16.dp)
                    .align(Alignment.End)
                    .clickable {
                        // onForgotPasswordClicked()
                        println("Şifremi unuttum tıklandı")
                    },
                style = MaterialTheme.typography.bodySmall
            )

            generalErrorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {

                    var isValid = true
                    if (name.isBlank()) {
                        nameError = "İsim alanı boş bırakılamaz."
                        isValid = false
                    }
                    if (email.isBlank()) {
                        emailError = "E-posta alanı boş bırakılamaz."
                        isValid = false
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        emailError = "Geçerli bir e-posta adresi giriniz."
                        isValid = false
                    }
                    if (password.isBlank()) {
                        passwordError = "Şifre alanı boş bırakılamaz."
                        isValid = false
                    } else if (password.length < 6) {
                        passwordError = "Şifre en az 6 karakter olmalıdır."
                        isValid = false
                    }

                    if (isValid) {
                        isLoading = true
                        generalErrorMessage = null
                        println("Giriş yapılıyor: İsim: $name, E-posta: $email, Şifre: $password")

                        authViewModel.signUp(name = name, email = email, password = password)

                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !isLoading,
                shape = MaterialTheme.shapes.medium
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("GİRİŞ YAP / KAYIT OL", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Hesabınız var mı? ", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Giriş Yapın",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navigateTo(navController, DestinationScreen.Login.route)
                        println("Kayıt ol tıklandı")
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}