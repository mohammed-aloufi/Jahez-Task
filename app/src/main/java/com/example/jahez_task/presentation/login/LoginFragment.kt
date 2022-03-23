package com.example.jahez_task.presentation.login

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.jahez_task.R
import com.example.jahez_task.base.BaseFragment
import com.example.jahez_task.databinding.LoginFragmentBinding
import com.example.jahez_task.utils.InputValidator.isValidEmail
import com.example.jahez_task.utils.InputValidator.isValidPassword
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment(),
    View.OnClickListener {

    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var binding: LoginFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginFragmentBinding.inflate(layoutInflater)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        init()
        observeLoginState()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setOnClickListeners()
        textWatchers()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.forgetPassTxtView -> {
                //TODO: not yet implemented
            }
            binding.submitBtn -> onSubmitClicked()
            binding.registerTxtView -> onRegisterClicked()
        }
    }

    private fun init() {
        binding.emailTxtInputL.isEndIconVisible = false
    }

    private fun observeLoginState() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginState.collect { state ->
                    when {
                        state.isLoading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        state.isSuccessful -> {
                            binding.progressBar.visibility = View.GONE
                            showSnackbar(requireView(), "Success")
                        }
                        state.message.isNotBlank() -> {
                            binding.progressBar.visibility = View.GONE
                            showSnackbar(requireView(), state.message)
                        }
                    }
                }
            }
        }
    }

    private fun setOnClickListeners() {
        binding.forgetPassTxtView.setOnClickListener(this)
        binding.submitBtn.setOnClickListener(this)
        binding.registerTxtView.setOnClickListener(this)

        binding.passTxtInputL.setEndIconOnClickListener {
            onPassEndIconClicked()
        }
    }

    private fun textWatchers() {
        binding.emailEdtTxt.doOnTextChanged { text, _, _, _ ->
            binding.emailTxtInputL.isEndIconVisible = !text.isNullOrBlank() && text.isValidEmail()
            binding.emailTxtInputL.isErrorEnabled = false
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun onPassEndIconClicked() {
        if (viewModel.isPasswordVisible) {
            binding.passEdtTxt.inputType = 129 // it's wrong in InputType interface
            binding.passTxtInputL.endIconDrawable = resources.getDrawable(
                R.drawable.ic_baseline_visibility_off_24,
                resources.newTheme()
            )
            viewModel.isPasswordVisible = false
        } else {
            binding.passEdtTxt.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.passTxtInputL.endIconDrawable = resources.getDrawable(
                R.drawable.ic_baseline_visibility_24,
                resources.newTheme()
            )
            viewModel.isPasswordVisible = true
        }
    }

    private fun onSubmitClicked() {
        val email = binding.emailEdtTxt.text.toString()
        val password = binding.passEdtTxt.text.toString()

        if (isInputValid(email, password)) {
            viewModel.login(email, password)
        }
    }

    private fun onRegisterClicked() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    private fun isInputValid(email: String, password: String): Boolean {
        return when {
            email.isBlank() && !email.isValidEmail() -> {
                binding.emailTxtInputL.isErrorEnabled = true
                binding.emailTxtInputL.error = resources.getString(R.string.required_field)
                false
            }
            password.isBlank() -> {
                binding.passTxtInputL.isErrorEnabled = true
                binding.passTxtInputL.error = resources.getString(R.string.required_field)
                false
            }
            else -> {
                true
            }
        }
    }
}