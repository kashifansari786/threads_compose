package com.kashif.thread_clone_compose.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.kashif.thread_clone_compose.model.UserModel
import com.kashif.thread_clone_compose.utils.SharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

/**
 * Created by Mohammad Kashif Ansari on 19,April,2024
 */

@HiltViewModel
class MainViewModel @Inject constructor(val auth:FirebaseAuth,val database:FirebaseDatabase,val storage:FirebaseStorage):ViewModel(){


    val userRef=database.getReference("user")
    val imageRef=storage.reference.child("user/{${UUID.randomUUID()}.jpg}")

    private val _firebaseUser=MutableLiveData<FirebaseUser>()
    val firebaseUser:LiveData<FirebaseUser> = _firebaseUser

    private val _error=MutableLiveData<String>()
    val error:LiveData<String> = _error

    init {
        val currentUser=auth.currentUser
//        signIn.value=currentUser!=null
        _firebaseUser.value=currentUser!!
        currentUser?.uid?.let{

        }
    }

    fun loginUser(email:String,password:String){
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
            if(it.isSuccessful){
                _firebaseUser.postValue(auth.currentUser)
            }else
                _error.postValue("Something went wrong.")
        }
    }

    fun registerUser(email:String,password:String,name:String,bio:String,userName:String,imageUri:Uri,context: Context){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
            if(it.isSuccessful){
                _firebaseUser.postValue(auth.currentUser)
                saveImage(email,password,name,bio,userName,imageUri,auth.currentUser?.uid,context)
            }else
                _error.postValue("Something went wrong.")
        }
    }

    private fun saveImage(email: String, password: String, name: String, bio: String, userName: String, imageUri: Uri, uid: String?,context: Context) {

        val uploadTask=imageRef.putFile(imageUri).addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(email,password,name,bio,userName,it.toString(),uid,context)
            }
        }

    }

    private fun saveData(email: String, password: String, name: String, bio: String, userName: String, imageUrl: String, uid: String?,context: Context) {

        val userData=UserModel(email,password,name,bio,userName,imageUrl,uid)
        userRef.child(uid!!).setValue(userData).addOnSuccessListener {
            SharedPref.storeData(email,name,bio,userName,imageUrl,uid,context)
        }.addOnFailureListener{

        }
    }

}