package com.example.jahez_task.presentation.register

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.example.jahez_task.R
import com.example.jahez_task.base.BaseFragment
import com.example.jahez_task.databinding.RegisterFragmentBinding
import com.example.jahez_task.utils.Constants
import com.example.jahez_task.utils.Constants.EMPTY_EMAIL
import com.example.jahez_task.utils.Constants.EMPTY_PASSWORD
import com.example.jahez_task.utils.Constants.INVALID_EMAIL
import com.example.jahez_task.utils.Constants.INVALID_NAME
import com.example.jahez_task.utils.Constants.INVALID_PASSWORD
import com.example.jahez_task.utils.Constants.PASSWORDS_NOT_MATCHING
import com.example.jahez_task.utils.Constants.VALID_INPUT
import com.example.jahez_task.utils.InputValidator.isValidEmail
import com.example.jahez_task.utils.InputValidator.isValidPassword
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment(),
    View.OnClickListener {

    private val viewModel by viewModels<RegisterViewModel>()
    private lateinit var binding: RegisterFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegisterFragmentBinding.inflate(layoutInflater)
        init()
        setBaseViewModel(viewModel)
        observeRegisterState()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setOnClickListeners()
        textWatchers()
    }

    private fun init() {
        binding.nameTxtInputL.isEndIconVisible = false
        binding.emailTxtInputL.isEndIconVisible = false
    }

    private fun observeRegisterState() {
        collectLatestLifecycleFlow(viewLifecycleOwner, viewModel.registerState) { state ->
            if (state) {
                binding.progressBar.visibility = View.GONE
                popBackStack()
            }
        }
    }

    private fun observeInputState(name: String, email: String, password: String) {
        collectLatestLifecycleFlow(viewLifecycleOwner, viewModel.inputState) { state ->
            when (state) {
                INVALID_NAME -> {
                    binding.nameTxtInputL.isErrorEnabled = true
                    binding.nameTxtInputL.error = resources.getString(R.string.required_field)
                }
                INVALID_EMAIL -> {
                    binding.emailTxtInputL.isErrorEnabled = true
                    binding.emailTxtInputL.error = resources.getString(R.string.invalid_email)
                }
                INVALID_PASSWORD -> {
                    binding.passwordTxtInputL.isErrorEnabled = true
                    binding.confirmPassTxtInputL.isErrorEnabled = true
                    binding.confirmPassTxtInputL.error = resources.getString(R.string.pass_rules)
                }
                EMPTY_EMAIL -> {
                    binding.emailTxtInputL.isErrorEnabled = true
                    binding.emailTxtInputL.error = resources.getString(R.string.required_field)
                }
                EMPTY_PASSWORD -> {
                    binding.passwordTxtInputL.isErrorEnabled = true
                    binding.passwordTxtInputL.error = resources.getString(R.string.required_field)
                }
                PASSWORDS_NOT_MATCHING -> {
                    binding.passwordTxtInputL.isErrorEnabled = true
                    binding.confirmPassTxtInputL.isErrorEnabled = true
                    binding.passwordTxtInputL.error =
                        resources.getString(R.string.unmatched_pass_warning)
                }
                VALID_INPUT -> viewModel.register(name, email, password)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.submitBtn -> onSubmitClicked()
            binding.loginTxtView -> popBackStack()
        }
    }

    private fun setOnClickListeners() {
        binding.submitBtn.setOnClickListener(this)
        binding.loginTxtView.setOnClickListener(this)

        binding.passwordTxtInputL.setEndIconOnClickListener {
            onPassEndIconClicked()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun onPassEndIconClicked() {
        if (viewModel.isPasswordVisible) {
            binding.passwordEdtTxt.inputType = 129 // it's wrong in InputType interface
            binding.confirmPassEdtTxt.inputType = 129
            binding.passwordTxtInputL.endIconDrawable = resources.getDrawable(
                R.drawable.ic_baseline_visibility_off_24,
                resources.newTheme()
            )
            viewModel.isPasswordVisible = false
        } else {
            binding.passwordEdtTxt.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.confirmPassEdtTxt.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.passwordTxtInputL.endIconDrawable = resources.getDrawable(
                R.drawable.ic_baseline_visibility_24,
                resources.newTheme()
            )
            viewModel.isPasswordVisible = true
        }
    }

    private fun onSubmitClicked() {
        val name = binding.nameEdtTxt.text.toString()
        val email = binding.emailEdtTxt.text.toString()
        val password = binding.passwordEdtTxt.text.toString().trim()
        val passwordConfirm = binding.confirmPassEdtTxt.text.toString().trim()

        observeInputState(name, email, password)
        viewModel.isInputValid(name, email, password, passwordConfirm)
    }

    private fun textWatchers() {
        binding.nameEdtTxt.doOnTextChanged { text, _, _, _ ->
            binding.nameTxtInputL.isEndIconVisible = !text.isNullOrBlank()
            binding.nameTxtInputL.isErrorEnabled = false
        }

        binding.emailEdtTxt.doOnTextChanged { text, _, _, _ ->
            binding.emailTxtInputL.isEndIconVisible = !text.isNullOrBlank() && text.isValidEmail()
            binding.emailTxtInputL.isErrorEnabled = false
        }

        binding.passwordEdtTxt.doOnTextChanged { text, _, _, _ ->
            binding.passwordTxtInputL.isErrorEnabled = false
            binding.confirmPassTxtInputL.isErrorEnabled = false
        }

        binding.confirmPassEdtTxt.doOnTextChanged { text, _, _, _ ->
            binding.passwordTxtInputL.isErrorEnabled = false
            binding.confirmPassTxtInputL.isErrorEnabled = false
        }
    }

}