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
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        init()
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
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerState.collect { state ->
                    when {
                        state.isLoading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        state.isSuccessful -> {
                            binding.progressBar.visibility = View.GONE
                            popBackStack()
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

    override fun onClick(v: View?) {
        when(v){
            binding.passwordTxtInputL -> onPassEndIconClicked()
            binding.submitBtn -> onSubmitClicked()
            binding.loginTxtView -> popBackStack()
        }
    }

    private fun setOnClickListeners() {
        binding.passwordTxtInputL.setEndIconOnClickListener(this)
        binding.submitBtn.setOnClickListener(this)
        binding.loginTxtView.setOnClickListener(this)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun onPassEndIconClicked(){
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

    private fun onSubmitClicked(){
        val name = binding.nameEdtTxt.text.toString()
        val email = binding.emailEdtTxt.text.toString()
        val password = binding.passwordEdtTxt.text.toString()
        val passwordConfirm = binding.confirmPassEdtTxt.text.toString()

        if (isInputValid(name, email, password, passwordConfirm)){
            viewModel.register(name, email, password)
        }
    }

    private fun popBackStack(){
        findNavController().popBackStack()
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

    private fun isInputValid(name: String, email: String, password: String, confirmPassword: String): Boolean {
        return when {
            name.isBlank() -> {
                binding.nameTxtInputL.isErrorEnabled = true
                binding.nameTxtInputL.error = resources.getString(R.string.required_field)
                false
            }
            email.isBlank() -> {
                binding.emailTxtInputL.isErrorEnabled = true
                binding.emailTxtInputL.error = resources.getString(R.string.required_field)
                false
            }
            password.isBlank() -> {
                binding.passwordTxtInputL.isErrorEnabled = true
                binding.passwordTxtInputL.error = resources.getString(R.string.required_field)
                false
            }
            password != confirmPassword -> {
                binding.passwordTxtInputL.isErrorEnabled = true
                binding.confirmPassTxtInputL.isErrorEnabled = true
                binding.passwordTxtInputL.error =
                    resources.getString(R.string.unmatched_pass_warning)
                false
            }
            password.isValidPassword() -> {
                binding.passwordTxtInputL.isErrorEnabled = true
                binding.confirmPassTxtInputL.isErrorEnabled = true
                binding.confirmPassTxtInputL.error = resources.getString(R.string.pass_rules)
                false
            }
            else -> {
                true
            }
        }
    }

}