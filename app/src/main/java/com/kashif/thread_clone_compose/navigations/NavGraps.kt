package com.kashif.thread_clone_compose.navigations

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kashif.thread_clone_compose.screens.AddThreads
import com.kashif.thread_clone_compose.screens.BottomNav
import com.kashif.thread_clone_compose.screens.HomeScreen
import com.kashif.thread_clone_compose.screens.Login
import com.kashif.thread_clone_compose.screens.Notifications
import com.kashif.thread_clone_compose.screens.Profile
import com.kashif.thread_clone_compose.screens.Register
import com.kashif.thread_clone_compose.screens.Search
import com.kashif.thread_clone_compose.screens.Splash
import com.kashif.thread_clone_compose.viewmodel.MainViewModel

/**
 * Created by Mohammad Kashif Ansari on 19,April,2024
 */

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: MainViewModel
){

        NavHost(navController = navController, startDestination = Routes.Splash.routes) {
            composable(Routes.Register.routes){
                Register(navController,viewModel)
            }
            composable(Routes.Login.routes){
                Login(navController,viewModel)
            }
            composable(Routes.Splash.routes){
                Splash(navController,viewModel)
            }
            composable(Routes.Home.routes){
                HomeScreen(navController,viewModel)
            }
            composable(Routes.Profile.routes){
                Profile(navController,viewModel)
            }
            composable(Routes.Notifications.routes){
                Notifications(navController,viewModel)
            }
            composable(Routes.Search.routes){
                Search(navController,viewModel)
            }
            composable(Routes.AddThreads.routes){
                AddThreads(navController,viewModel)
            }
            composable(Routes.BottomNav.routes){
                BottomNav(navController,viewModel)
            }
        }
}