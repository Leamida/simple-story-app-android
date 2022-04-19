package com.example.storyapp.presentation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.storyapp.MainActivity
import com.example.storyapp.databinding.ActivityAuthBinding
import com.example.storyapp.R
import com.example.storyapp.core.util.Internet
import com.example.storyapp.core.util.Result
import com.example.storyapp.feature.auth.data.source.local.preferences.UserPreferences
import com.example.storyapp.presentation.view_model.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private var auth: String = "login"
    private lateinit var userPreferences: UserPreferences
    private val userViewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferences = UserPreferences(applicationContext)
        supportActionBar?.hide()
        setUiLogin()

        binding.apply {
            btnAuth.setOnClickListener {
                if (Internet.isAvailable(this@AuthActivity)) {
                    inputValidation()
                } else {
                    Toast.makeText(
                        this@AuthActivity,
                        resources.getString(R.string.no_internet_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            btnAuthContrast.setOnClickListener {
                setUi()
            }
        }
    }


    private fun setUiLogin() {
        binding.apply {
            btnAuth.text = resources.getString(R.string.button_login)
            btnAuthContrast.text = resources.getString(R.string.button_register)
            tvAsk.text = resources.getString(R.string.dont_have_an_account)
            cName.visibility = View.GONE
            etName.error = null
            etName.text = null
        }
    }

    private fun setUiRegister() {
        binding.apply {
            btnAuth.text = resources.getString(R.string.button_register)
            btnAuthContrast.text = resources.getString(R.string.button_login)
            tvAsk.text = resources.getString(R.string.already_have_an_account)
            cName.visibility = View.VISIBLE
        }
    }

    private fun setUi() {
        when (auth) {
            "register" -> {
                setUiLogin()
                auth = "login"
            }
            "login" -> {
                setUiRegister()
                auth = "register"
            }
        }
    }

    private fun postLogin(email: String, password: String) {
        userViewModel.postLogin(email, password).observe(this@AuthActivity) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.apply {
                        btnAuth.isEnabled = false
                        pbButtonAuth.visibility = View.VISIBLE
                        btnAuth.text = ""
                    }
                    Log.d(TAG, "Loading...")
                }
                is Result.Success -> {
                    val user = result.data
                    CoroutineScope(Dispatchers.IO).launch {
                        userPreferences.setUser(user)
                        startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                        this@AuthActivity.finish()
                    }
                    Log.d(TAG, result.data.toString())

                }
                is Result.Error -> {
                    binding.apply {
                        btnAuth.isEnabled = true
                        pbButtonAuth.visibility = View.GONE
                        btnAuth.text = resources.getString(R.string.button_login)
                    }
                    Toast.makeText(
                        this@AuthActivity,
                        resources.getString(R.string.login_failed) + ".\n" +
                                resources.getString(R.string.invalid_email) + " " +
                                resources.getString(R.string.or) + " " +
                                resources.getString(R.string.invalid_password),
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d(TAG, result.error)
                }
            }
        }
    }

    private fun postRegister(name: String, email: String, password: String) {
        userViewModel.postRegister(name, email, password).observe(this@AuthActivity) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.apply {
                        btnAuth.isEnabled = false
                        pbButtonAuth.visibility = View.VISIBLE
                        btnAuth.text = ""
                    }
                    Log.d(TAG, "Loading...")
                }
                is Result.Success -> {
                    Log.d(TAG, result.data)
                    when (result.data) {
                        "User created" -> {
                            Toast.makeText(
                                this@AuthActivity,
                                resources.getString(R.string.user_created_trying_to_login),
                                Toast.LENGTH_LONG
                            ).show()
                            binding.cName.visibility = View.GONE
                            auth = "login"
                            postLogin(email, password)
                        }
                    }
                }
                is Result.Error -> {
                    binding.apply {
                        btnAuth.isEnabled = true
                        pbButtonAuth.visibility = View.GONE
                        btnAuth.text = resources.getString(R.string.button_register)
                    }
                    when (result.error) {
                        "HTTP 400 Bad Request" -> {
                            binding.apply {
                                etEmail.error = resources.getString(R.string.invalid_email) + " " +
                                        resources.getString(R.string.or) + " " +
                                        resources.getString(R.string.email_already_taken)
                                etEmail.requestFocus()
                            }
                        }
                        else -> {
                            Toast.makeText(this@AuthActivity, result.error, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                    Log.d(TAG, result.error)
                }
            }
        }
    }

    private fun inputValidation() {
        binding.apply {
            when (auth) {
                "login" -> {
                    when {
                        etEmail.text.isNullOrBlank() -> {
                            etEmail.error = resources.getString(R.string.required)
                            etEmail.requestFocus()
                        }
                        etPassword.text.isNullOrBlank() -> {
                            etPassword.error = resources.getString(R.string.required)
                            etPassword.requestFocus()
                        }
                        else -> {
                            if (etEmail.error.isNullOrEmpty() && etPassword.error.isNullOrEmpty()) {
                                postLogin(
                                    etEmail.text?.trim().toString(),
                                    etPassword.text.toString()
                                )
                            }
                        }
                    }
                }
                "register" -> {
                    when {
                        etName.text.isNullOrBlank() -> {
                            etName.error = resources.getString(R.string.required)
                            etName.requestFocus()
                        }
                        etEmail.text.isNullOrBlank() -> {
                            etEmail.error = resources.getString(R.string.required)
                            etEmail.requestFocus()
                        }
                        etPassword.text.isNullOrBlank() -> {
                            etPassword.error = resources.getString(R.string.required)
                            etPassword.requestFocus()
                        }
                        else -> {
                            if (etName.error.isNullOrEmpty() && etEmail.error.isNullOrEmpty() && etPassword.error.isNullOrEmpty()) {
                                postRegister(
                                    etName.text.toString(),
                                    etEmail.text?.trim().toString(),
                                    etPassword.text.toString()
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "AuthActivity"
    }
}