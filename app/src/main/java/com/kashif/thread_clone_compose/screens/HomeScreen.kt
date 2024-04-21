package com.kashif.thread_clone_compose.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.kashif.thread_clone_compose.item_view.ThreadItems
import com.kashif.thread_clone_compose.viewmodel.MainViewModel

/**
 * Created by Mohammad Kashif Ansari on 19,April,2024
 */

@Composable
fun HomeScreen(navController: NavHostController, viewModel: MainViewModel) {

    val threadAndUsers=viewModel.threadsData.value
    val firebaseauth=viewModel.auth
    LazyColumn {
        items(threadAndUsers?: emptyList()){pairs->
            firebaseauth.currentUser?.uid?.let {
                ThreadItems(threadModel = pairs.first,
                    userModel = pairs.second,
                    navHostController = navController,
                    userId = it
                )
            }
        }
    }
}
