package com.onurcancansu.socialmediaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.onurcancansu.socialmediaapp.databinding.ActivitySignupBinding
import com.onurcancansu.socialmediaapp.model.UserModel
import com.onurcancansu.socialmediaapp.util.UiUtil


class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmit.setOnClickListener {
            signUp()
        }
        binding.goToLoginBtn.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }
    fun setInProgress(inProgress : Boolean){
        if(inProgress){
            binding.progressBar.visibility = View.VISIBLE
            binding.btnSubmit.visibility = View.GONE
        }else{
            binding.progressBar.visibility = View.GONE
            binding.btnSubmit.visibility = View.VISIBLE
        }
    }
    fun signUp(){
        val email=binding.emailInput.text.toString()
        val password=binding.passwordInput.text.toString()
        val confirmPassword=binding.passwordInput.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailInput.error = "Email not valid"
            return;
        }
        if (password.length<6){
            binding.passwordInput.error= "Minumum 6 character"
            return
        }
        if (password !=confirmPassword){
            binding.confirmPasswordInput.error = "Password not matched"
            return
        }
        signUpwithFirebase(email,password)

    }
    fun signUpwithFirebase(email : String, password : String){
        setInProgress(true)
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            email,password
        ).addOnSuccessListener {
            it.user?.let {user->
                val userModel = UserModel( user.uid,email,email.substringBefore("@") )
                Firebase.firestore.collection("users")
                    .document(user.uid)
                    .set(userModel).addOnSuccessListener {
                        UiUtil.showToast(applicationContext,"Account created successfully")
                        setInProgress(false)
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }
            }
        }.addOnFailureListener {
            UiUtil.showToast(applicationContext,it.localizedMessage?: "Something went wrong")
            setInProgress(false)
        }
    }
}
