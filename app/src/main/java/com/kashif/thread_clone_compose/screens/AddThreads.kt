package com.kashif.thread_clone_compose.screens

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.kashif.thread_clone_compose.R
import com.kashif.thread_clone_compose.navigations.Routes
import com.kashif.thread_clone_compose.utils.SharedPref
import com.kashif.thread_clone_compose.utils.basicTextFieldWithHint
import com.kashif.thread_clone_compose.utils.commonProgressBar
import com.kashif.thread_clone_compose.utils.imageCompress
import com.kashif.thread_clone_compose.viewmodel.MainViewModel

/**
 * Created by Mohammad Kashif Ansari on 19,April,2024
 */

@Composable
fun AddThreads(navController: NavHostController, viewModel: MainViewModel)
{
    var isThreadProgress= remember {
        mutableStateOf(false)
    }
    val context= LocalContext.current
    var thread by remember {
        mutableStateOf("")
    }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var permissionToRequest = if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU)
        android.Manifest.permission.READ_MEDIA_IMAGES
    else
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    val launcher= rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            uri: Uri?->
        imageUri=uri
    }
    val permissionLauncher= rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
            isGranted->
        if(isGranted){

        }else{

        }
    }
    LaunchedEffect(key1 = viewModel.isThreadPosted.value) {
        Log.d("inside_threads","child posted inside coroutine "+viewModel.isThreadPosted.value+", "+viewModel.isThreadProgress.value)
        if(viewModel.isThreadPosted.value!!){
            thread=""
            imageUri=null
            viewModel.isThreadPosted.value=false
            isThreadProgress.value=false
            Toast.makeText(context,"Thread added",Toast.LENGTH_SHORT).show()
            navController.navigate(Routes.Home.routes){
                popUpTo(Routes.AddThreads.routes){
                    inclusive=true//it will remove addthreads only from the backstack
                }
            }
        }

    }

    if(isThreadProgress.value)
        commonProgressBar()
    else{
        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()

        ) {
            val (topConstrain,crossPic,text,logo,userName,editText,attachedMediaLogo,replyText,button,imageBox,bottomBox) =createRefs()
            ConstraintLayout(modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .constrainAs(topConstrain) {
                    top.linkTo(parent.top, margin = 50.dp)
                    start.linkTo(parent.start)
                    bottom.linkTo(bottomBox.top)
                }
            ) {

                Image(painter = painterResource(id = R.drawable.baseline_close_24), contentDescription ="close", modifier = Modifier
                    .constrainAs(crossPic) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .clickable {
                        navController.navigate(Routes.Home.routes) {
                            popUpTo(Routes.AddThreads.routes) {
                                inclusive = true//it will remove addthreads only from the backstack
                            }
                        }
                    } )
                Text(text = "Add Thread", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, modifier = Modifier.constrainAs(text){
                    top.linkTo(crossPic.top)
                    start.linkTo(crossPic.end, margin = 20.dp)
                    bottom.linkTo(crossPic.bottom)
                })

                Image(painter = rememberAsyncImagePainter(model = SharedPref.getUserImage(context),placeholder  = painterResource(
                    id = R.drawable.baseline_person_24)), contentDescription ="profile_pic", modifier = Modifier
                    .constrainAs(logo) {
                        top.linkTo(text.bottom, margin = 30.dp)
                        start.linkTo(parent.start)
                    }
                    .size(40.dp)
                    .clip(CircleShape), contentScale = ContentScale.Crop
                )
                Text(text = SharedPref.getUserName(context),fontSize = 20.sp, modifier = Modifier.constrainAs(userName){
                    top.linkTo(logo.top)
                    start.linkTo(logo.end, margin = 20.dp)
                    bottom.linkTo(logo.bottom)
                })
                basicTextFieldWithHint(hint = "Start a thread...", value = thread, onValueChange = {thread=it}, modifier = Modifier
                    .constrainAs(editText) {
                        top.linkTo(logo.bottom)
                        start.linkTo(logo.start)
                        end.linkTo(parent.end)
                    }
                    .padding(horizontal = 8.dp, vertical = 8.dp) )
                if(imageUri==null){
                    Image(painter = painterResource(id = R.drawable.baseline_attach_file_24),
                        contentDescription = "attachement",
                        modifier = Modifier
                            .constrainAs(attachedMediaLogo) {
                                top.linkTo(editText.bottom)
                                start.linkTo(editText.start)
                            }
                            .clickable {
                                val isGranted = ContextCompat.checkSelfPermission(
                                    context, permissionToRequest
                                ) == PackageManager.PERMISSION_GRANTED

                                if (isGranted)
                                    launcher.launch("image/*")
                                else
                                    permissionLauncher.launch(permissionToRequest)
                            } )
                }else{
                    Box(modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .constrainAs(imageBox) {
                            top.linkTo(editText.bottom)
                            start.linkTo(editText.start)
                            end.linkTo(parent.end)
                        }){
                        Image(painter = rememberAsyncImagePainter(model = imageCompress(context,imageUri!!)),
                            contentDescription ="imageAttach",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentScale = ContentScale.Crop)
                        Icon(imageVector = Icons.Default.Close, contentDescription = "remove image", modifier = Modifier
                            .align(
                                Alignment.TopEnd
                            )
                            .clickable {
                                imageUri = null
                            })
                    }
                }



            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .constrainAs(bottomBox) {
                    bottom.linkTo(parent.bottom)
                }, horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Anyone can reply",fontSize = 20.sp)
                TextButton(onClick = {
                    if(imageUri==null && thread.isNullOrEmpty())
                        Toast.makeText(context,"Please write thread or upload image",Toast.LENGTH_SHORT).show()
                    else{
                        isThreadProgress.value=true
                        if (imageUri==null)
                            viewModel.saveThreadData(thread,"")
                        else
                            viewModel.saveThreadImage(thread,imageUri!!)
                    }


                }) {
                    Text(text = "Post", fontSize = 20.sp)
                }
            }

        }
    }



}




