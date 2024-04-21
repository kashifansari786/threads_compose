package com.kashif.thread_clone_compose.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kashif.thread_clone_compose.navigations.Routes
import com.kashif.thread_clone_compose.viewmodel.MainViewModel

/**
 * Created by Mohammad Kashif Ansari on 19,April,2024
 */

@Composable
fun Login(navHostController: NavHostController, viewModel: MainViewModel){

    var email by  remember {
        mutableStateOf("")
    }
    var password by  remember {
        mutableStateOf("")
    }
    val context= LocalContext.current
    val firebaseUser=viewModel.userData.value
   // val firebaseUser by viewModel.firebaseUser.observeAsState(null)
    Log.d("inside_firebase","login_"+firebaseUser?.uid)
    LaunchedEffect(key1 = firebaseUser) {
        if(firebaseUser!=null){
            navHostController.navigate(Routes.BottomNav.routes){
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop=true
            }
        }

    }
    Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text = "Login", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
        Box(modifier = Modifier.height(50.dp))
        OutlinedTextField(value = email, onValueChange = {email=it}, label = {
            Text(text = "Email", )
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email
        ), singleLine = true,
            modifier = Modifier.fillMaxWidth())
        Box(modifier = Modifier.height(20.dp))
        OutlinedTextField(value = password, onValueChange = {password=it}, label = {
            Text(text = "Password", )
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ), singleLine = true,
            modifier = Modifier.fillMaxWidth())
        Box(modifier = Modifier.height(30.dp))
        ElevatedButton(onClick = {
            if(email.isNullOrEmpty() or password.isNullOrEmpty())
                Toast.makeText(context,"Please fill all details",Toast.LENGTH_SHORT).show()
            else
                viewModel.loginUser(email,password,context)
        }, modifier = Modifier.fillMaxWidth().padding(30.dp)) {
            Text(text = "Login", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
        }
        TextButton(onClick = {
            navHostController.navigate(Routes.Register.routes){
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop=true
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "New User ? Create Account", fontWeight = FontWeight.Light, color = Color.Black, fontSize = 16.sp)
        }
    }
}

