package com.androminor.mangaapp.presentation.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.androminor.mangaapp.R
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignInScreen(
    viewModel: SignInViewModel,
    onNavigatingHome: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(key1 = viewModel.isSignInSuccessful) {
        viewModel.isSignInSuccessful.collectLatest { isSuccessful ->
            if (isSuccessful) {
                onNavigatingHome()
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        //close button
        Icon(imageVector = Icons.Default.Close,
            contentDescription = stringResource(R.string.close),
            tint = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
                .clickable { //later action will be handled
                })
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.zenithra),
                color = Color.White,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.please_enter_your_details_to_sign_in),
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )
            //Social

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                //Google signin
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.DarkGray)
                        .clickable { viewModel.onEvent(BehaviouralEvent.GoogleSignIn) },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = stringResource(R.string.sign_in_with_google),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))

                //Apple signin
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.DarkGray)
                        .clickable { viewModel.onEvent(BehaviouralEvent.AppleSignIn) },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_apple),
                        contentDescription = stringResource(R.string.sign_in_with_apple),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            // or divider
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color.DarkGray
                )
                Text(
                    text = stringResource(R.string.or),
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color.DarkGray
                )
            }
            //Email input fields
            OutlinedTextField(
                value = state.email,
                onValueChange = { viewModel.onEvent(StatefulEvent.EmailChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.your_email_address)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    backgroundColor = Color.Transparent,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.Gray,
                    placeholderColor = Color.Gray
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true

            )
            Spacer(modifier = Modifier.height(16.dp))

            //Password input fields
            OutlinedTextField(
                value = state.password,
                onValueChange = { viewModel.onEvent(StatefulEvent.PasswordChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.password)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon =
                        if (state.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { viewModel.onEvent(BehaviouralEvent.TogglePasswordVisibility) }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = stringResource(R.string.toggle_password),
                            tint = Color.Gray
                        )

                    }


                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    backgroundColor = Color.Transparent,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.Gray,
                    placeholderColor = Color.Gray
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true

            )
            //Forgot password
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(text = "Forgot password?",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { viewModel.onEvent(BehaviouralEvent.ForgetPassword) })
            }
            Spacer(modifier = Modifier.height(24.dp))

            //Sign in button
            Button(
                onClick = { viewModel.onEvent(BehaviouralEvent.SignIn) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray
                )

            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(stringResource(R.string.sign_in))
                }

            }
            //Error msg
            if(state.error !=null){
                Text(text = state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp)
                    )
            }
            //Create account option
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
                ) {
                Text(
                    text = stringResource(R.string.don_t_have_an_account),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(text = stringResource(R.string.sign_up),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        viewModel.onEvent(BehaviouralEvent.SignUp)
                    }
                )
            }
        }
    }
}