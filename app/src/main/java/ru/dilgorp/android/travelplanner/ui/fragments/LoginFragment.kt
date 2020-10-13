package ru.dilgorp.android.travelplanner.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.dilgorp.android.travelplanner.R
import ru.dilgorp.android.travelplanner.databinding.FragmentLoginBinding
import ru.dilgorp.android.travelplanner.provider.AppComponentProvider
import ru.dilgorp.android.travelplanner.ui.dialog.showMessage
import ru.dilgorp.android.travelplanner.vm.ViewModelFactory
import ru.dilgorp.android.travelplanner.vm.fragment.LoginViewModel
import javax.inject.Inject

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: LoginViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as AppComponentProvider).getAppComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        setupObservers()
        setupViews()

        return binding.root
    }

    private fun setupObservers() {
        with(viewModel){
            message.observe(viewLifecycleOwner){
                showMessage(requireView(), getString(R.string.error_message), it){
                    messageShown()
                }
            }
        }
    }

    private fun setupViews() {
        with(binding){
            signInButton.setOnClickListener {
                viewModel.login(
                    username.editText?.text.toString(),
                    password.editText?.text.toString()
                )
            }
            signUpButton.setOnClickListener {
                viewModel.register(
                    username.editText?.text.toString(),
                    password.editText?.text.toString()
                )
            }
        }
    }
}