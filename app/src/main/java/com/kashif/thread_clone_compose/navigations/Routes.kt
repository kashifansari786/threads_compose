package com.kashif.thread_clone_compose.navigations

/**
 * Created by Mohammad Kashif Ansari on 19,April,2024
 */

sealed class Routes (val routes: String){

    object Home:Routes("home")
    object Search:Routes("search")
    object Profile:Routes("profile")
    object Notifications:Routes("notifications")
    object Splash:Routes("splash")
    object Login:Routes("login")
    object Register:Routes("register")
    object AddThreads:Routes("add_threads")
    object BottomNav:Routes("bottom_nav")

}