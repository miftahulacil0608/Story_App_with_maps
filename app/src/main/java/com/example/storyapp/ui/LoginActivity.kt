package com.example.storyapp.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.data.SharedPreferences
import com.example.storyapp.data.dataClass.DataSharedPreferences
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.ui.viewModel.LoginViewModel


@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding

    private lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        setEditText(loginBinding.inputTextEmail)
        setEditText(loginBinding.inputTextPassword)
        setupView()
        setupAction()
        playAnimation()

    }

    private fun playAnimation() {
        val labelEmail = ObjectAnimator.ofFloat(loginBinding.titleEmail,View.ALPHA,1f).setDuration(500)
        val inputEmail = ObjectAnimator.ofFloat(loginBinding.emailEditTextLayout,View.ALPHA,1f).setDuration(500)
        val labelPassword = ObjectAnimator.ofFloat(loginBinding.titlePassword,View.ALPHA,1f).setDuration(500)
        val inputPassword = ObjectAnimator.ofFloat(loginBinding.passwordEditTextLayout,View.ALPHA,1f).setDuration(500)
        val loginButton = ObjectAnimator.ofFloat(loginBinding.btnLogin,View.ALPHA,1f).setDuration(500)
        val titleHaveAccount = ObjectAnimator.ofFloat(loginBinding.signupQuestion,View.ALPHA,1f).setDuration(500)
        val signupButton = ObjectAnimator.ofFloat(loginBinding.btnSignup,View.ALPHA,1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(labelEmail,inputEmail,labelPassword,inputPassword,loginButton,titleHaveAccount,signupButton)
            startDelay = 500
            start()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }


    private fun setupViewModel(username: String, password: String) {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(SharedPreferences.instance(dataStore))
        )[LoginViewModel::class.java]
        loginViewModel.apply {
            loginViewModel.login(this@LoginActivity, username, password)
            isLoading.observe(this@LoginActivity) {
                showLoading(it)
            }
            user.observe(this@LoginActivity){
                println("value dari name = ${it}")
            }

            user.observe(this@LoginActivity) {
                if (!it.error) {

                    loginViewModel.setToken(it.loginResult.token)
                    loginViewModel.saveUser(
                        DataSharedPreferences(
                            username = it.loginResult.name,
                            userId = it.loginResult.userId,
                            token = it.loginResult.token,
                            isLogin = true
                        )
                    )
                    showDialogs(
                        resources.getString(R.string.login_success_title),
                        resources.getString(R.string.login_success_message),
                        resources.getString(R.string.instruction_success),
                        true
                    )
                } else {
                    showDialogs(
                        resources.getString(R.string.login_failed_title),
                        resources.getString(R.string.login_failed_message),
                        resources.getString(R.string.instruction_failed),
                        false
                    )
                }
            }

        }
    }

    private fun setupAction() {

        loginBinding.btnLogin.setOnClickListener {
            val email = loginBinding.inputTextEmail.text.toString().trim()
            val password = loginBinding.inputTextPassword.text.toString().trim()
            setupViewModel(email, password)
        }

        loginBinding.btnSignup.setOnClickListener {
            val intent = Intent(this@LoginActivity,SignupActivity::class.java)
            startActivity(intent)
        }
        loginBinding.backButton.setOnClickListener {
            navigateToWelcome()
        }

    }

    private fun showLoading(state: Boolean) {
        loginBinding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }


    private fun showDialogs(title: String, message: String, instruction: String, state: Boolean) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(instruction) { _, _ ->
                if (state) {
                    val intent = Intent(context, MainActivityStoryApp::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                } else {
                    finish()
                    overridePendingTransition(0, 0)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
            }
            create()
            show()
        }
    }


    private fun setEditText(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                loginBinding.btnLogin.isValid(
                    loginBinding.inputTextEmail,
                    loginBinding.inputTextPassword,
                    loginBinding.emailEditTextLayout,
                    loginBinding.passwordEditTextLayout
                )
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
    private fun navigateToWelcome(){
        val intent = Intent(this@LoginActivity,WelcomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        navigateToWelcome()
        super.onBackPressed()

    }
}