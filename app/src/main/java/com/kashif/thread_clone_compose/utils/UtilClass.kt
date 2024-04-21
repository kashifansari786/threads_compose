package com.kashif.thread_clone_compose.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by Mohammad Kashif Ansari on 20,April,2024
 */

@Composable
fun basicTextFieldWithHint(hint:String,value:String,onValueChange:(String)->Unit,modifier: Modifier){

    Box(modifier = modifier){
        if(value.isEmpty())
            Text(text = hint, color = Color.Gray)
        BasicTextField(value = value, onValueChange = onValueChange, textStyle = TextStyle.Default.copy(color=Color.Black),modifier=Modifier.fillMaxWidth())
    }
}
@Composable
fun commonProgressBar(){
    Row (modifier = Modifier
        .alpha(0.5f)
        .background(Color.LightGray)
        .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center){
        CircularProgressIndicator()
        Text(text = "Please wait....", color = Color.White)
    }
}

fun imageCompress(context: Context, uri: Uri): Uri? {
    val contentResolver: ContentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(uri)

    // Decode bitmap from the input Uri
    val bitmap = BitmapFactory.decodeStream(inputStream)

    // Compress the bitmap
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

    // Create a Uri for the compressed image
    val compressedImageUri: Uri? = try {
        // Create a file to save the compressed image
        val fileName = "compressed_image.jpg"
        val compressedImageFile = context.cacheDir.resolve(fileName)
        val fileOutputStream = FileOutputStream(compressedImageFile)

        // Write the compressed image to the file
        fileOutputStream.write(outputStream.toByteArray())
        fileOutputStream.flush()
        fileOutputStream.close()

        // Return the Uri of the compressed image file
        Uri.fromFile(compressedImageFile)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    } finally {
        // Close the input stream
        inputStream?.close()
    }

    return compressedImageUri
}