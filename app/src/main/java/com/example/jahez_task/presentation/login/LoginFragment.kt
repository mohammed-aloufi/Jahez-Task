package com.example.jahez_task.presentation.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.example.jahez_task.R
import com.example.jahez_task.base.BaseFragment
import com.example.jahez_task.databinding.LoginFragmentBinding
import com.example.jahez_task.utils.Constants
import com.example.jahez_task.utils.Constants.INVALID_EMAIL
import com.example.jahez_task.utils.Constants.INVALID_PASSWORD
import com.example.jahez_task.utils.Constants.VALID_INPUT
import com.example.jahez_task.utils.InputValidator.isValidEmail
import com.example.jahez_task.utils.InputValidator.isValidPassword
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
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
        collectLatestLifecycleFlow(viewLifecycleOwner, viewModel.loginState){ state ->
            when {
                state.isLoading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                state.isSuccessful -> {
                    binding.progressBar.visibility = View.GONE
                    showSnackbar(requireView(), "Success")
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
                state.message.isNotBlank() -> {
                    binding.progressBar.visibility = View.GONE
                    showSnackbar(requireView(), state.message)
                }
            }
        }
    }

    private fun observeInputState(email: String, password: String){
        collectLatestLifecycleFlow(this.viewLifecycleOwner, viewModel.inputState){ state ->
            when(state) {
                INVALID_EMAIL ->{
                    binding.emailTxtInputL.isErrorEnabled = true
                    binding.emailTxtInputL.error = resources.getString(R.string.required_field)
                }
                INVALID_PASSWORD -> {
                    binding.passTxtInputL.isErrorEnabled = true
                    binding.passTxtInputL.error = resources.getString(R.string.required_field)
                }
                VALID_INPUT -> viewModel.login(email, password)
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

        observeInputState(email, password)
        viewModel.checkInputs(email, password)
    }

    private fun onRegisterClicked() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }
}