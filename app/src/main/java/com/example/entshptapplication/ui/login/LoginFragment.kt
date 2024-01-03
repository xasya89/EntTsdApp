package com.example.entshptapplication.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.entshptapplication.MainActivity
import com.example.entshptapplication.R
import com.example.entshptapplication.databinding.FragmentLoginBinding
import com.example.entshptapplication.ui.actions.ActionsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel by activityViewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)

        binding.loginBtn.setOnClickListener {
            loginViewModel.authorize(binding.smartCartEdit.text.toString())
        }


        return  binding.root
    }
    @Suppress
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launch {

            loginViewModel.error.observe(viewLifecycleOwner, {error ->
                if(error!=null && error!="")
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            })

            loginViewModel.worker.observe(viewLifecycleOwner, {
                if(it!=null) openActionFragment()
            })

            loginViewModel.getLogin()
        }
        clearBarCode()
    }

    fun clearBarCode(){
        (activity as MainActivity).clearBarCode()
    }

    private fun openActionFragment(){
        parentFragmentManager.commit {
            replace(R.id.fragmentContainerView, ActionsFragment.newInstance())
            setReorderingAllowed(true)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {}
    }
}