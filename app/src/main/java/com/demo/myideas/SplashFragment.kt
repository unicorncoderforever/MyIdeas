package com.demo.myideas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.demo.myideas.databinding.FragmentSplashBinding
import com.demo.myideas.ui.login.LoginViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class SplashFragment : DaggerFragment() {

    private var splashBinding: FragmentSplashBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: LoginViewModel by lazy {
        ViewModelProviders
            .of(this, viewModelFactory)
            .get(LoginViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        with(viewModel) {
            mUserInfo.observe(this@SplashFragment.viewLifecycleOwner, Observer {
                if (it == null) {
                    navigateToSignup()
                } else
                    navigateToIdeaFragment()
            })
            error.observe(this@SplashFragment.viewLifecycleOwner, Observer {
                navigateToSignup()
            })
        }
        viewModel.getCurrentUserInfo()
        splashBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_splash, container, false
        )
        return splashBinding?.root
    }

    private fun navigateToSignup() {
        findNavController().navigate(R.id.action_splashFragment_to_SignUpFragment)
    }

    private fun navigateToIdeaFragment() {
        findNavController().navigate(R.id.action_splashFragment_to_ideasFragment)
    }


}
