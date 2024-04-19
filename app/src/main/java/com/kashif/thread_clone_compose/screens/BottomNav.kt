package com.kashif.thread_clone_compose.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kashif.thread_clone_compose.model.BottomNavItems
import com.kashif.thread_clone_compose.navigations.Routes
import com.kashif.thread_clone_compose.viewmodel.MainViewModel

/**
 * Created by Mohammad Kashif Ansari on 19,April,2024
 */

@Composable
fun BottomNav(navController: NavHostController, viewModel: MainViewModel){

    val navController1= rememberNavController()
    Scaffold(bottomBar = { MyBottomBar(navController1) }) {
        NavHost(navController = navController1, startDestination = Routes.Home.routes, modifier = Modifier.padding(it)) {
            composable(Routes.Splash.routes){
                Splash(navController1, viewModel)
            }
            composable(Routes.Home.routes){
                HomeScreen(navController, viewModel)
            }
            composable(Routes.Profile.routes){
                Profile(navController, viewModel)
            }
            composable(Routes.Notifications.routes){
                Notifications(navController, viewModel)
            }
            composable(Routes.Search.routes){
                Search(navController, viewModel)
            }
            composable(Routes.AddThreads.routes){
                AddThreads(navController, viewModel)
            }
            composable(Routes.BottomNav.routes){
                BottomNav(navController1, viewModel)
            }
        }
    }
}

@Composable
fun MyBottomBar(navHostController: NavHostController){

    val backStackEntry=navHostController.currentBackStackEntryAsState()
    val list= listOf(
        BottomNavItems(
            "Home",Routes.Home.routes,Icons.Rounded.Home
        ),
        BottomNavItems(
            "Search",Routes.Search.routes,Icons.Rounded.Search
        ),
        BottomNavItems(
            "Add Threads",Routes.AddThreads.routes,Icons.Rounded.Add
        ),
        BottomNavItems(
            "Notification",Routes.Notifications.routes,Icons.Rounded.Notifications
        ),
        BottomNavItems(
            "Profile",Routes.Profile.routes,Icons.Rounded.Person
        )

    )

    BottomAppBar {
        list.forEach{
            val selected=it.route==backStackEntry?.value?.destination?.route
            NavigationBarItem(selected = selected, onClick = {
                navHostController.navigate(it.route){
                    popUpTo(navHostController.graph.findStartDestination().id){
                        saveState=true
                    }
                    launchSingleTop=true
                }

                                                             }, icon = {
                                                                 Icon(
                                                                     imageVector = it.icon,
                                                                     contentDescription = it.title
                                                                 )
            })
        }
    }

}