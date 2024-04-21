package com.kashif.thread_clone_compose.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kashif.thread_clone_compose.navigations.Routes
import kotlinx.coroutines.delay
import com.kashif.thread_clone_compose.R
import com.kashif.thread_clone_compose.viewmodel.MainViewModel

/**
 * Created by Mohammad Kashif Ansari on 19,April,2024
 */


@Composable
fun Splash(navHostController: NavHostController, viewModel: MainViewModel){

    Column(modifier = Modifier.fillMaxSize().background(Color.White), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Image(painter = painterResource(id = R.drawable.thread_logo),
            contentDescription = "logo", modifier = Modifier.size(80.dp))
    }
    //you can achive same above feature by using constrain layout
//    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
//        val (image:ConstrainedLayoutReference)=createRefs()
//        Image(painter = painterResource(id = R.drawable.ic_launcher_background), contentDescription = "logo", modifier = Modifier.constrainAs(image){
//            top.linkTo(parent.top)
//            bottom.linkTo(parent.bottom)
//            start.linkTo(parent.start)
//            end.linkTo(parent.end)
//        })
//    }
    val firebaseUser=viewModel.userData.value
   // val firebaseUser by viewModel.firebaseUser.observeAsState(null)
    LaunchedEffect(key1 = true) {

        delay(3000)
        Log.d("inside_firebase","splash_"+firebaseUser?.uid)
        if(firebaseUser!=null && !firebaseUser.uid.isNullOrEmpty()){
            navHostController.navigate(Routes.BottomNav.routes){
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop=true
            }
        }
        else{
        navHostController.navigate(Routes.Login.routes){
            popUpTo(navHostController.graph.startDestinationId)
            launchSingleTop=true
        }
    }

    }

}