package com.example.storyappsubmission1.Activity.Start

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.widget.ImageView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.storyappsubmission1.Activity.Main.MainActivity
import com.example.storyappsubmission1.R
import com.example.storyappsubmission1.databinding.ActivityLoginBinding
import com.example.storyappsubmission1.Data.API.Config
import com.example.storyappsubmission1.Data.Functon.Preference
import com.example.storyappsubmission1.Data.Response.LoginR
import com.example.storyappsubmission1.Data.Response.LoginResult
import com.example.storyappsubmission1.ViewModel.FactoryVM
import com.example.storyappsubmission1.ViewModel.LoginVM
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginVM
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
        playAnimation()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.apply {
            visibility = if (isLoading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun setupView() {
        window.insetsController?.hide(WindowInsets.Type.statusBars())
        supportActionBar?.hide()

        if (!intent.getStringExtra("email").isNullOrEmpty()) {
            binding.edLoginEmail.setText(intent.getStringExtra("email"))
        }
        if (!intent.getStringExtra("password").isNullOrEmpty()) {
            binding.edLoginPassword.setText(intent.getStringExtra("password"))
        }
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            FactoryVM(Preference.getInstance(dataStore))
        )[LoginVM::class.java]
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            loginViewModel.
            /*when {
                email.isEmpty() -> {
                    binding.edLoginEmail.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.edLoginPassword.error = "Masukkan password"
                }
                else -> {
                    showLoading(true)
                    val client = Config.getApiService()
                        .login(
                            binding.edLoginEmail.text.toString(),
                            binding.edLoginPassword.text.toString()
                        )
                    client.enqueue(object : Callback<LoginR> {
                        override fun onResponse(call: Call<LoginR>, response: Response<LoginR>) {
                            val responseBody = response.body()
                            if (response.isSuccessful && responseBody != null) {
                                val imageView: ImageView = findViewById(R.id.imageView)
                                Glide.with(this@LoginActivity).load(R.drawable.welcome)
                                    .into(imageView)

                                if (responseBody.error) {
                                    showLoading(false)
                                    Toast.makeText(
                                        this@LoginActivity,
                                        responseBody.message,
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                } else {
                                    saveSession(responseBody.loginResult)
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Welcome",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } else {
                                showLoading(false)
                                Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                                Toast.makeText(
                                    this@LoginActivity,
                                    response.message(),
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                        }

                        override fun onFailure(call: Call<LoginR>, t: Throwable) {
                            showLoading(false)
                            Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                            Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_LONG).show()
                        }

                    })
                }
            }
        }
    }

             */

    private fun saveSession(loginResult: LoginResult) {
        showLoading(false)
        loginViewModel.setUser(loginResult)
        val i = Intent(this, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
        finish()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(500)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(500)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passowrdTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 500
        }.start()
    }
}

