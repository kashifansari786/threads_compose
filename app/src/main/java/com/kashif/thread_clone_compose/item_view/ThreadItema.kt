package com.kashif.thread_clone_compose.item_view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.kashif.thread_clone_compose.R
import com.kashif.thread_clone_compose.model.ThreadModel
import com.kashif.thread_clone_compose.model.UserModel
import com.kashif.thread_clone_compose.utils.SharedPref
import com.kashif.thread_clone_compose.utils.imageCompress

/**
 * Created by Mohammad Kashif Ansari on 21,April,2024
 */

@Composable
fun ThreadItems(threadModel: ThreadModel,
                userModel: UserModel,
                navHostController: NavHostController,
                userId:String){

    Column {
        ConstraintLayout (modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)){
            val (userImage,userName,date,time,title,image)=createRefs()

            Image(painter = rememberAsyncImagePainter(model = userModel.imageUrl,placeholder  = painterResource(
                id = R.drawable.baseline_person_24)
            ), contentDescription ="profile_pic", modifier = Modifier
                .constrainAs(userImage) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .size(40.dp)
                .clip(CircleShape), contentScale = ContentScale.Crop
            )
            Text(text = userModel.userName,fontSize = 20.sp,
                modifier = Modifier.constrainAs(userName){
                    top.linkTo(userImage.top)
                    start.linkTo(userImage.end, margin = 10.dp)
                    bottom.linkTo(userImage.bottom)
                })
            Text(text = threadModel.thread!!,fontSize = 18.sp,
                modifier = Modifier.constrainAs(title){
                    top.linkTo(userName.bottom, margin = 8.dp)
                    start.linkTo(userName.start)
                })
            if(threadModel.image!=""){
                Card(modifier = Modifier.constrainAs(image){
                    top.linkTo(title.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                    Log.d("inside_model","image:- "+threadModel.image)
                    Image(painter = rememberAsyncImagePainter(model = threadModel.image),
                        contentDescription ="image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop)
                }
            }

        }
        Divider(color = Color.LightGray, thickness = 1.dp)
    }

}
