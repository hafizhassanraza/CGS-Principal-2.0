package com.enfotrix.cgs_principal.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.enfotrix.cgs_principal.Models.PrincipalModel
import com.enfotrix.cgs_principal.Models.PrincipalViewModel
import com.enfotrix.cgs_principal.SharedPrefManager
import com.enfotrix.cgs_principal.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class ActivityLogin : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var mContext: Context
    private val pricipalViewModel: PrincipalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@ActivityLogin
        sharedPrefManager = SharedPrefManager(mContext)

        binding.btnLogin.setOnClickListener {
            val principalId = binding.etRegistration.text.toString()
            val password = binding.etPassword.text.toString()

            if (principalId.isEmpty()) {
                binding.etRegistration.error = "Enter ID, please"
            } else if (password.isEmpty()) {
                binding.etPassword.error = "Enter Password, please"
            } else {
                LogInCheck(principalId, password)
            }
        }
    }

    private fun LogInCheck(id: String, password: String) {


        lifecycleScope.launch {
            try {
                Toast.makeText(mContext, ""+id, Toast.LENGTH_SHORT).show()
                Toast.makeText(mContext, ""+password, Toast.LENGTH_SHORT).show()

                pricipalViewModel.checkLogin(id, password).addOnCompleteListener { task->
                    if (task.isSuccessful) {
                        val documents = task.result
                            sharedPrefManager.savePrincipal(PrincipalModel(id, password))
                            val intent = Intent(mContext, ActivityClasses::class.java)
                            startActivity(intent)
                            finish()

                    } else {
                        Toast.makeText(mContext, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                Toast.makeText(mContext, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}