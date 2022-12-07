package com.example.storyappsubmission1.Activity.Start

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.widget.Toast
import com.example.storyappsubmission1.databinding.ActivitySingupBinding
import com.example.storyappsubmission1.Data.API.Config
import com.example.storyappsubmission1.Data.Response.GeneralR
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SingupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySingupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
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
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            when {
                name.isEmpty() -> {
                    binding.edRegisterName.error = "Masukkan Nama"
                }
                email.isEmpty() -> {
                    binding.edRegisterEmail.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.edRegisterPassword.error = "Masukkan password"
                }
                else -> {
                    showLoading(true)
                    val client = Config.getApiService().register(
                        binding.edRegisterName.text.toString(),
                        binding.edRegisterEmail.text.toString(),
                        binding.edRegisterPassword.text.toString()
                    )
                    client.enqueue(object : Callback<GeneralR> {
                        override fun onResponse(call: Call<GeneralR>, response: Response<GeneralR>) {
                            val responseBody = response.body()
                            if (response.isSuccessful && responseBody != null) {
                                if (responseBody.error) {
                                    showLoading(false)
                                    Toast.makeText(
                                        this@SingupActivity,
                                        responseBody.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    openLoginPage()
                                    Toast.makeText(
                                        this@SingupActivity,
                                        responseBody.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } else {
                                showLoading(false)
                                Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                                Toast.makeText(this@SingupActivity, response.message(), Toast.LENGTH_LONG)
                                    .show()
                            }
                        }

                        override fun onFailure(call: Call<GeneralR>, t: Throwable) {
                            showLoading(false)
                            Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                            Toast.makeText(this@SingupActivity, t.message, Toast.LENGTH_LONG).show()
                        }
                    })
                }
            }
        }


    }
    private fun openLoginPage() {
        val i = Intent(this, LoginActivity::class.java)
        i.putExtra("email", binding.edRegisterEmail.text.toString())
        i.putExtra("password", binding.edRegisterPassword.text.toString())
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(i)
        showLoading(false)
        finish()
    }
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val nameTextView = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val nameEditTextLayout = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(500)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 500
        }.start()
    }
}


