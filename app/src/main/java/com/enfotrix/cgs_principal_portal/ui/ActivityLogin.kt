package com.enfotrix.cgs_principal_portal.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.enfotrix.cgs_principal_portal.Models.PrincipalViewModel
import com.enfotrix.cgs_principal_portal.SharedPrefManager
import com.enfotrix.cgs_principal_portal.Utils
import com.enfotrix.cgs_principal_portal.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class ActivityLogin : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var mContext: Context
    private lateinit var utils: Utils
    private val pricipalViewModel: PrincipalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@ActivityLogin
        sharedPrefManager = SharedPrefManager(mContext)
        utils = Utils(mContext)


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
        utils.startLoadingAnimation()

        lifecycleScope.launch {
            try {
                val trimmedId = id.trim()
                val trimmedPassword = password.trim()

                pricipalViewModel.checkLogin(trimmedId, trimmedPassword).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val documents = task.result
                        var isIdCorrect = false
                        var isPasswordCorrect = false

                        for (document in documents) {
                            val name = document.getString("name")
                            val documentpassword = document.getString("password")

                            if (name == trimmedId && documentpassword == trimmedPassword) {
                                isIdCorrect = true
                                isPasswordCorrect = true
                                break // Exit the loop early if credentials match
                            } else if (name == trimmedId) {
                                isIdCorrect = true
                            } else if (documentpassword == trimmedPassword) {
                                isPasswordCorrect = true
                            }
                        }

                        if (isIdCorrect && isPasswordCorrect) {
                            val intent = Intent(mContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                            utils.endLoadingAnimation()
                        } else {
                            if (isIdCorrect && isPasswordCorrect) {
                                // Both are correct
                                // You can handle this case here
                            } else if (isIdCorrect) {
                                // Only ID is correct
                                // You can handle this case here
                            } else if (isPasswordCorrect) {
                                // Only Password is correct
                                // You can handle this case here
                            } else {
                                // Both are incorrect
                                Toast.makeText(mContext, "Incorrect information", Toast.LENGTH_SHORT).show()
                            }
                            utils.endLoadingAnimation()
                        }
                    } else {
                        // Handle network error
                        Toast.makeText(mContext, "Network error", Toast.LENGTH_SHORT).show()
                        utils.endLoadingAnimation()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(mContext, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                utils.endLoadingAnimation()
            }
        }
    }

}