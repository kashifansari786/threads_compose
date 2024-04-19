package com.kashif.thread_clone_compose.screens

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.kashif.thread_clone_compose.R
import com.kashif.thread_clone_compose.navigations.Routes
import com.kashif.thread_clone_compose.viewmodel.MainViewModel

/**
 * Created by Mohammad Kashif Ansari on 19,April,2024
 */

@Composable
fun Register(navHostController: NavHostController, viewModel: MainViewModel){

    var email by  remember {
        mutableStateOf("")
    }
    var name by  remember {
        mutableStateOf("")
    }
    var userName by  remember {
        mutableStateOf("")
    }
    var bio by  remember {
        mutableStateOf("")
    }
    var password by  remember {
        mutableStateOf("")
    }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val context= LocalContext.current
    var permissionToRequest = if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU)
        android.Manifest.permission.READ_MEDIA_IMAGES
    else
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    val launcher= rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
        uri:Uri?->
        imageUri=uri
    }
    val permissionLauncher= rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
            isGranted->
        if(isGranted){

        }else{

        }
    }
    val firebaseUser by viewModel.firebaseUser.observeAsState(null)
    LaunchedEffect(key1 = firebaseUser) {
        if(firebaseUser!=null){
            navHostController.navigate(Routes.BottomNav.routes){
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop=true
            }
        }
        else {
            navHostController.navigate(Routes.Login.routes) {
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

        Text(text = "Register", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
        Box(modifier = Modifier.height(25.dp))
        Image(painter = if(imageUri==null) painterResource(id = R.drawable.ic_launcher_background) else rememberAsyncImagePainter(
            model = imageUri
        ), contentDescription = "person", modifier = Modifier
            .size(96.dp)
            .clip(
                CircleShape
            )
            .background(Color.LightGray)
            .clickable {
                val isGranted = ContextCompat.checkSelfPermission(
                    context, permissionToRequest
                ) == PackageManager.PERMISSION_GRANTED

                if (isGranted)
                    launcher.launch("image/*")
                else
                    permissionLauncher.launch(permissionToRequest)
            }, contentScale = ContentScale.Crop )
        Box(modifier = Modifier.height(25.dp))
        OutlinedTextField(value = name, onValueChange = { name = it }, label = {
            Text(text = "Name",)
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ), singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Box(modifier = Modifier.height(10.dp))
        OutlinedTextField(value = userName, onValueChange = { userName = it }, label = {
            Text(text = "UserName",)
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ), singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Box(modifier = Modifier.height(10.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = {
            Text(text = "Email",)
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email
        ), singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Box(modifier = Modifier.height(10.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = {
            Text(text = "Password",)
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ), singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Box(modifier = Modifier.height(10.dp))
        OutlinedTextField(value = bio, onValueChange = { bio = it }, label = {
            Text(text = "Bio",)
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ), singleLine = false,
            modifier = Modifier.fillMaxWidth()
        )
        Box(modifier = Modifier.height(30.dp))
        ElevatedButton(onClick = {
                if(name.isEmpty() or email.isEmpty() or bio.isEmpty() or password.isEmpty() or imageUri.toString().isEmpty()){
                    Toast.makeText(context,"Please fill all details",Toast.LENGTH_LONG).show()
                }else
                    viewModel.registerUser(email=email,password=password,name=name,bio=bio,userName=userName, imageUri=imageUri!!, context = context)
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp)) {
            Text(text = "Register", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
        }
        TextButton(onClick = {
            navHostController.navigate(Routes.Login.routes){
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop=true
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "New Register ? Login here ->",
                fontWeight = FontWeight.Light,
                color = Color.Black,
                fontSize = 16.sp
            )
        }
    }
}