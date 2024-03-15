package com.project.spass.presentation.screens.sign_up_screen.component

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.project.spass.R
import com.project.spass.presentation.common.component.CustomDefaultBtn
import com.project.spass.presentation.common.component.CustomTextField
import com.project.spass.presentation.common.component.DefaultBackArrow
import com.project.spass.presentation.common.component.ErrorSuggestion
import com.project.spass.presentation.graphs.auth_graph.AuthScreen
import com.project.spass.presentation.ui.theme.PrimaryColor
import com.project.spass.presentation.ui.theme.PrimaryLightColor
import com.project.spass.presentation.ui.theme.TextColor
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SignUpScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    val emailErrorState = remember { mutableStateOf(false) }
    val passwordErrorState = remember { mutableStateOf(false) }
    val conPasswordErrorState = remember { mutableStateOf(false) }
    val firstNameErrorState = remember { mutableStateOf(false) }
    val lastNameErrorState = remember { mutableStateOf(false) }
    val phoneNumberErrorState = remember { mutableStateOf(false) }
    val addressErrorState = remember { mutableStateOf(false) }
    val animate = remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    AnimatedContent(targetState = animate.value, transitionSpec = {
        slideInHorizontally(
            initialOffsetX = { value ->
                value
            }
        ) with slideOutHorizontally(
            targetOffsetX = { value ->
                -value
            }
        )
    }) {
        if (it) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    Box(modifier = Modifier.weight(0.7f)) {
                        DefaultBackArrow {
                             navController.popBackStack()
                        }
                    }
                    Box(modifier = Modifier.weight(1.0f)) {
                        Text(
                            text = "Sign Up",
                            color = MaterialTheme.colors.TextColor,
                            fontSize = 18.sp
                        )
                    }


                }
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = "Register Account", fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "Complete your details or continue\nwith social media.",
                    color = MaterialTheme.colors.TextColor,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(50.dp))
                CustomTextField(
                    placeholder = "example@gmail.com",
                    trailingIcon = R.drawable.mail,
                    label = "Email",
                    errorState = emailErrorState,
                    keyboardType = KeyboardType.Email,
                    visualTransformation = VisualTransformation.None,
                    onChanged = { newEmail ->
                        email = newEmail.text
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))
                CustomTextField(
                    placeholder = "********",
                    trailingIcon = R.drawable.lock,
                    label = "Password",
                    keyboardType = KeyboardType.Password,
                    errorState = passwordErrorState,
                    visualTransformation = PasswordVisualTransformation(),
                    onChanged = { newPass ->
                        password = newPass.text
                    }
                )


                Spacer(modifier = Modifier.height(20.dp))
                CustomTextField(
                    placeholder = "********",
                    trailingIcon = R.drawable.lock,
                    label = "Confirm Password",
                    keyboardType = KeyboardType.Password,
                    errorState = conPasswordErrorState,
                    visualTransformation = PasswordVisualTransformation(),
                    onChanged = { newPass ->
                        confirmPass = newPass.text
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))
                if (emailErrorState.value) {
                    ErrorSuggestion("Please enter valid email address.")
                }
                if (passwordErrorState.value) {
                    Row() {
                        ErrorSuggestion("Please enter valid password.")
                    }
                }
                if (conPasswordErrorState.value) {
                    ErrorSuggestion("Confirm Password miss matched.")
                }
                CustomDefaultBtn(shapeSize = 50f, btnText = "Continue") {
                    //email pattern
                    val pattern = Patterns.EMAIL_ADDRESS
                    val isEmailValid = pattern.matcher(email).matches()
                    val isPassValid = password.length >= 8
                    val conPassMatch = password == confirmPass
                    emailErrorState.value = !isEmailValid
                    passwordErrorState.value = !isPassValid
                    conPasswordErrorState.value = !conPassMatch
                    if (isEmailValid && isPassValid && conPassMatch) {
                        animate.value = !animate.value
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 50.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            space = 10.dp,
                            alignment = Alignment.CenterHorizontally
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(
                                    MaterialTheme.colors.PrimaryLightColor,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.google_icon),
                                contentDescription = "Google Login Icon"
                            )
                        }

                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp)
                            .clickable {

                            },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "By continuing you confirm that you agree",
                            color = MaterialTheme.colors.TextColor
                        )
                        Row()
                        {
                            Text(
                                text = "with our ",
                                color = MaterialTheme.colors.TextColor,
                            )
                            Text(
                                text = "Terms & Condition",
                                color = MaterialTheme.colors.PrimaryColor,
                                modifier = Modifier.clickable {

                                })
                        }

                    }
                }


            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    Box(modifier = Modifier.weight(0.7f)) {
                        DefaultBackArrow {
                            animate.value = !animate.value
                        }
                    }
                    Box(modifier = Modifier.weight(1.0f)) {
                        Text(
                            text = "Sign Up",
                            color = MaterialTheme.colors.TextColor,
                            fontSize = 18.sp
                        )
                    }


                }
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = "Complete Profile", fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "Complete your details or continue\nwith social media.",
                    color = MaterialTheme.colors.TextColor,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(50.dp))
                CustomTextField(
                    placeholder = "Enter your first name",
                    trailingIcon = R.drawable.user,
                    label = "First Name",
                    errorState = firstNameErrorState,
                    keyboardType = KeyboardType.Text,
                    visualTransformation = VisualTransformation.None,
                    onChanged = { newText ->
                        firstName = newText.text
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
                CustomTextField(
                    placeholder = "Enter your last name",
                    trailingIcon = R.drawable.user,
                    label = "Last Name",
                    errorState = lastNameErrorState,
                    keyboardType = KeyboardType.Text,
                    visualTransformation = VisualTransformation.None,
                    onChanged = { newText ->
                        lastName = newText.text
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))
                CustomTextField(
                    placeholder = "Enter your phone number",
                    trailingIcon = R.drawable.phone,
                    label = "Phone Number",
                    keyboardType = KeyboardType.Phone,
                    errorState = phoneNumberErrorState,
                    visualTransformation = VisualTransformation.None,
                    onChanged = { newNumber ->
                        phoneNumber = newNumber.text
                    }
                )


                Spacer(modifier = Modifier.height(20.dp))
                CustomTextField(
                    placeholder = "example: Mohapada, Rasayani",
                    trailingIcon = R.drawable.location_point,
                    label = "Address",
                    keyboardType = KeyboardType.Password,
                    errorState = addressErrorState,
                    visualTransformation = VisualTransformation.None,
                    onChanged = { newText ->
                        address = newText.text
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                if (firstNameErrorState.value || lastNameErrorState.value) {
                    ErrorSuggestion("Please enter valid name.")
                }
                if (phoneNumberErrorState.value) {
                    ErrorSuggestion("Please enter valid phone number.")
                }
                if (addressErrorState.value) {
                    ErrorSuggestion("Please enter valid address.")
                }

                CustomDefaultBtn(shapeSize = 50f, btnText = "Continue") {
                    val isPhoneValid = phoneNumber.isEmpty() || phoneNumber.length < 10 || phoneNumber.length > 10
                    val isFNameValid = firstName.isEmpty()
                    val isLNameValid = lastName.isEmpty()
                    val isAddressValid = address.isEmpty()
                    firstNameErrorState.value = isFNameValid
                    lastNameErrorState.value = isLNameValid
                    addressErrorState.value = isAddressValid
                    phoneNumberErrorState.value = isPhoneValid
                    if (!isFNameValid && !isLNameValid && !isAddressValid && !isPhoneValid) {
                        coroutineScope.launch {
                            signUpWithEmailAndPassword(context, email, password, firstName, lastName, phoneNumber, address) {
                                navController.navigate(AuthScreen.SignInScreen.route)
                            }
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 50.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp)
                        .clickable {

                        },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "By continuing you confirm that you agree",
                        color = MaterialTheme.colors.TextColor
                    )
                    Row()
                    {
                        Text(
                            text = "with our ",
                            color = MaterialTheme.colors.TextColor,
                        )
                        Text(
                            text = "Terms & Condition",
                            color = MaterialTheme.colors.PrimaryColor,
                            modifier = Modifier.clickable {

                            })
                    }

                }
            }


        }
    }
}

data class SignUpData(
    val userId: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val address: String
)

private suspend fun signUpWithEmailAndPassword(
    context: Context,
    email: String,
    password: String,
    firstName: String,
    lastName: String,
    phoneNumber: String,
    address: String,
    onSignUpSuccess: () -> Unit
) {
    try {
        val auth = FirebaseAuth.getInstance()
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        if (result.user != null) {
            // User signed up successfully
            // You can navigate to the next screen or perform additional tasks here
            Toast.makeText(context, "Sign-up successful", Toast.LENGTH_SHORT).show()
            val signUpData = SignUpData(
                userId = result.user!!.uid,
                email = email,
                password = password,
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phoneNumber,
                address = address
            )
            saveUserDataToFirebase(signUpData)
            onSignUpSuccess.invoke()
        } else {
            // Handle sign-up failure
            Toast.makeText(context, "Sign-up failed", Toast.LENGTH_SHORT).show()
        }
    } catch (e: FirebaseAuthException) {
        // Handle FirebaseAuthException
        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        // Handle other exceptions
        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

private fun saveUserDataToFirebase(userData: SignUpData) {
    val database = Firebase.database
    val usersRef = database.getReference("users")
    usersRef.child(userData.userId).setValue(userData)
}