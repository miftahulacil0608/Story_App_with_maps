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
import com.example.storyapp.ui.viewModel.SignupViewModel
import com.example.storyapp.databinding.ActivitySignUpAcitivityBinding


@Suppress("DEPRECATION")
class SignupActivity : AppCompatActivity() {
    private lateinit var signUpActivityBinding: ActivitySignUpAcitivityBinding
    private lateinit var signupViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpActivityBinding = ActivitySignUpAcitivityBinding.inflate(layoutInflater)
        setContentView(signUpActivityBinding.root)

        setupView()

        setListener()
        setupAction()
        playAnimation()
    }

    private fun playAnimation() {
        val labelName = ObjectAnimator.ofFloat(signUpActivityBinding.titleUsername,View.ALPHA,1f).setDuration(500)
        val inputName = ObjectAnimator.ofFloat(signUpActivityBinding.usernameEditTextLayout,View.ALPHA,1f).setDuration(500)
        val labelEmail = ObjectAnimator.ofFloat(signUpActivityBinding.titleEmail,View.ALPHA,1f).setDuration(500)
        val inputEmail = ObjectAnimator.ofFloat(signUpActivityBinding.emailEditTextLayout,View.ALPHA,1f).setDuration(500)
        val labelPassword = ObjectAnimator.ofFloat(signUpActivityBinding.titlePassword,View.ALPHA,1f).setDuration(500)
        val inputPassword = ObjectAnimator.ofFloat(signUpActivityBinding.passwordEditTextLayout,View.ALPHA,1f).setDuration(500)
        val signupButton = ObjectAnimator.ofFloat(signUpActivityBinding.btnSignupSignupActivity,View.ALPHA,1f).setDuration(500)
        val titleHaveAccount = ObjectAnimator.ofFloat(signUpActivityBinding.signupQuestion,View.ALPHA,1f).setDuration(500)
        val loginButton = ObjectAnimator.ofFloat(signUpActivityBinding.btnLogin,View.ALPHA,1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(labelName,inputName,labelEmail,inputEmail,labelPassword,inputPassword,signupButton,titleHaveAccount,loginButton)
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

    private fun setupViewModel(inputUsername: String, inputEmail: String, inputPassword: String) {
        signupViewModel = ViewModelProvider(this)[SignupViewModel::class.java]
        signupViewModel.apply {
            signup(this@SignupActivity, inputUsername, inputEmail, inputPassword)
            isLoading.observe(this@SignupActivity) {
                showLoading(it)
            }
            isSuccess.observe(this@SignupActivity) { condition ->
                if (condition == true) {
                    showDialogs(
                        resources.getString(R.string.signup_success_title),
                        resources.getString(R.string.signup_success_message),
                        resources.getString(R.string.instruction_success),
                        condition
                    )
                } else {
                    showDialogs(
                        resources.getString(R.string.signup_failed_title),
                        resources.getString(R.string.signup_failed_message),
                        resources.getString(R.string.instruction_failed),
                        condition
                    )
                }
            }
        }
    }

    private fun setupAction() {
        signUpActivityBinding.btnSignupSignupActivity.setOnClickListener {
            val username = signUpActivityBinding.inputTextUsername.text.toString().trim()
            val email = signUpActivityBinding.inputTextEmail.text.toString().trim()
            val password = signUpActivityBinding.inputTextPassword.text.toString().trim()

            setupViewModel(username, email, password)

        }

        signUpActivityBinding.btnLogin.setOnClickListener {
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
        }
        signUpActivityBinding.backButton.setOnClickListener {
            navigateToWelcome()
        }
    }

    private fun showLoading(state: Boolean) {
        signUpActivityBinding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun showDialogs(title: String, message: String, instruction: String, state: Boolean) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(instruction) { _, _ ->
                if (state) {
                    val intent = Intent(context, LoginActivity::class.java)
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

    private fun setListener() {
        setEditText(signUpActivityBinding.inputTextUsername)
        setEditText(signUpActivityBinding.inputTextEmail)
        setEditText(signUpActivityBinding.inputTextPassword)
    }


    private fun setEditText(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                signUpActivityBinding.btnSignupSignupActivity.isValid(
                    signUpActivityBinding.inputTextEmail,
                    signUpActivityBinding.inputTextPassword,
                    signUpActivityBinding.emailEditTextLayout,
                    signUpActivityBinding.passwordEditTextLayout
                )
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
    private fun navigateToWelcome(){
        val intent = Intent(this@SignupActivity,WelcomeActivity::class.java)
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