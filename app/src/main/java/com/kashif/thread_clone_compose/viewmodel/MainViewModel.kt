package com.kashif.thread_clone_compose.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.kashif.thread_clone_compose.model.ThreadModel
import com.kashif.thread_clone_compose.model.UserModel
import com.kashif.thread_clone_compose.utils.Event
import com.kashif.thread_clone_compose.utils.SharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import java.util.UUID
import javax.inject.Inject

/**
 * Created by Mohammad Kashif Ansari on 19,April,2024
 */

@HiltViewModel
class MainViewModel @Inject constructor(val auth:FirebaseAuth,val database:FirebaseDatabase,val storage:FirebaseStorage):ViewModel(){


    val userRef=database.getReference("user")
    val imageRef=storage.reference.child("user/{${UUID.randomUUID()}.jpg}")
    val signIn= mutableStateOf(false)
    var inProgress= mutableStateOf(false)
    var eventMutableState= mutableStateOf<Event<String>?>(null)
   var userData= mutableStateOf<UserModel?>(null)

    //thread upload variables
    val threadRef=database.getReference("threads")
    val threadImageRef=storage.reference.child("threads/{${UUID.randomUUID()}.jpg}")
    var isThreadProgress= mutableStateOf(false)
    var isThreadPosted= mutableStateOf(false)

    var threadsData= mutableStateOf<List<Pair<ThreadModel,UserModel>>>(listOf())


    init {
      getData()
        fetchThreadsAndUsers {
            threadsData.value=it
        }
    }
    fun getData(){
        val currentUser=auth.currentUser
        Log.d("inside_firebase","viewmodel_"+currentUser?.uid)
        signIn.value=currentUser!=null
        //_firebaseUser.value=currentUser!=null
        currentUser?.uid?.let{
            getUserData(it)
        }
    }
    private fun getUserData(uId: String) {
        inProgress.value=true
        userRef.child(uId).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val data=snapshot.getValue(UserModel::class.java)
                userData.value=data
                inProgress.value=false
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
//            .get().addOnSuccessListener {
//            if(it.exists()){
//                var user=it.getValue() as HashMap<String,Any>
//                var userModel=UserModel()
//                with(userModel){
//                    name=user["name"].toString()
//                    email=user["email"].toString()
//                    bio=user["bio"].toString()
//                    userName=user["userName"].toString()
//                    imageUrl=user["imageUrl"].toString()
//                    uid=user["uid"].toString()
//                }
//
//                userData.value=userModel
//                inProgress.value=false
//            }
//        }
    }
    fun logout(){
        auth.signOut()
        signIn.value=false
    }
    fun loginUser(email:String,password:String,context: Context){
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
            if(it.isSuccessful){
                getData()
                val data=userData.value
                data?.let {
                    SharedPref.storeData(email=it.email,name=it.name,bio=it.bio, userName = it.userName, imageUrl = it.imageUrl, uid = it.uid!!, context = context)
                }

            }else
                it.exception?.message?.let { it1 -> handleException(customException = it1) }
        }
    }

    fun registerUser(email:String,password:String,name:String,bio:String,userName:String,imageUri:Uri,context: Context){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
            if(it.isSuccessful){
                getData()
                val data=userData.value
                data?.let {
                    SharedPref.storeData(email=it.email,name=it.name,bio=it.bio, userName = it.userName, imageUrl = it.imageUrl, uid = it.uid!!, context = context)
                }
                saveImage(email,password,name,bio,userName,imageUri,auth.currentUser?.uid,context)
            }else
                it.exception?.message?.let { it1 -> handleException(customException = it1) }

        }
    }

    private fun saveImage(email: String, password: String, name: String, bio: String, userName: String, imageUri: Uri, uid: String?,context: Context) {

        val uploadTask=imageRef.putFile(imageUri).addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(email,password,name,bio,userName,it.toString(),uid,context)
            }
        }

    }

    private fun  saveData(email: String, password: String, name: String, bio: String, userName: String, imageUrl: String, uid: String?,context: Context) {

        val userData=UserModel(email,password,name,bio,userName,imageUrl,uid)
        userRef.child(uid!!).setValue(userData).addOnSuccessListener {
            SharedPref.storeData(email,name,bio,userName,imageUrl,uid,context)
        }.addOnFailureListener{
            handleException(exception = it)
            inProgress.value=false
        }
    }

    //thread functions
    fun saveThreadImage(thread: String,  imageUri: Uri) {

        val uploadTask=threadImageRef.putFile(imageUri).addOnSuccessListener {
            threadImageRef.downloadUrl.addOnSuccessListener {
                saveThreadData(thread,it.toString())
            }
        }

    }
     fun  saveThreadData(thread: String, imageUrl: String) {
         isThreadProgress.value=true
        val userData= ThreadModel(thread,auth.currentUser?.uid,imageUrl,System.currentTimeMillis().toString())
        threadRef.child(threadRef.push().key!!).setValue(userData).addOnSuccessListener {
            Log.d("inside_threads","child posted")
            isThreadProgress.value=false
            isThreadPosted.value=true
        }.addOnFailureListener{
            handleException(exception = it)
            isThreadPosted.value=false
        }
    }


    fun fetchThreadsAndUsers(onResult:(List<Pair<ThreadModel,UserModel>>)->Unit){
        threadRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val result= mutableListOf<Pair<ThreadModel,UserModel>>()  //getting both threads along with user data
                for(threadSnapshot in snapshot.children){
                        val thread=threadSnapshot.getValue(ThreadModel::class.java)
                    thread?.let {
                        fetchUserFromThread(it!!){
                            user->
                            result.add(0,it to user)
                            if(result.size==snapshot.childrenCount.toInt())
                                onResult(result)

                        }
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun fetchUserFromThread(threadModel: ThreadModel,onResult:(UserModel)->Unit){
        threadModel!!.userId?.let { userRef.child(it).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user=snapshot.getValue(UserModel::class.java)
                user?.let(onResult)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }) }
    }

    fun handleException(exception: Exception?=null, customException:String=""){
        val errMessage=exception?.localizedMessage?:""
        val message=if(customException.isNullOrEmpty()) errMessage else customException

        eventMutableState.value= Event(message)

    }



}