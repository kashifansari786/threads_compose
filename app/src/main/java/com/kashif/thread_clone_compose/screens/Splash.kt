package com.kashif.thread_clone_compose.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.kashif.thread_clone_compose.navigations.Routes
import kotlinx.coroutines.delay
import com.kashif.thread_clone_compose.R
import com.kashif.thread_clone_compose.viewmodel.MainViewModel

/**
 * Created by Mohammad Kashif Ansari on 19,April,2024
 */

@Composable
fun Splash(navHostController: NavHostController, viewModel: MainViewModel){

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Image(painter = painterResource(id = R.drawable.ic_launcher_background), contentDescription = "logo", modifier = Modifier.size(100.dp))
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
    val firebaseUser by viewModel.firebaseUser.observeAsState(null)
    LaunchedEffect(key1 = true) {

        delay(3000)

        if(firebaseUser!=null){
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