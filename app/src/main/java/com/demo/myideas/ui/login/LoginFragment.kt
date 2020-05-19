package com.demo.myideas.ui.login

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.demo.myideas.MainActivity
import com.demo.myideas.R
import com.demo.myideas.databinding.LoginFragmentBinding
import com.demo.myideas.utility.Utility
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class LoginFragment : DaggerFragment() {
    private var loginFragmentBinding: LoginFragmentBinding? = null

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
        loginFragmentBinding =  DataBindingUtil.inflate(
            inflater, R.layout.login_fragment, container, false)
        (activity as MainActivity?)!!.supportActionBar!!.title = resources.getString(
            R.string.app_name
        )
        (activity as MainActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        with(viewModel) {
            mUserInfo.observe(this@LoginFragment.viewLifecycleOwner, Observer {
                navigateToIdeaFragment()
            })

            error.observe(this@LoginFragment.viewLifecycleOwner, Observer {
                loginFragmentBinding!!.progressLayout.visibility = View.GONE
                Toast.makeText(context,it.getErrorMessage(resources), Toast.LENGTH_LONG).show()
            })
        }

        addSpannableText()
        return loginFragmentBinding?.root
    }

    private fun navigateToIdeaFragment() {
        findNavController().navigate(R.id.action_LoginFragment_to_ideasFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginFragmentBinding!!.loginButton.setOnClickListener {
            loginUser()
        }


    }

    private fun loginUser() {
        val email = loginFragmentBinding!!.email.text
        val password = loginFragmentBinding!!.password.text
        if(!email.isEmpty() && !password.isEmpty()){
            loginFragmentBinding!!.progressLayout.visibility = View.VISIBLE
            viewModel.loginUser(email.toString(),password.toString(), {
                loginFragmentBinding!!.progressLayout.visibility = View.GONE
            }, {
                loginFragmentBinding!!.progressLayout.visibility = View.GONE

            });
        }else{
            Toast.makeText(context,resources.getString(R.string.text_empty),Toast.LENGTH_LONG).show()
        }
    }

    private fun addSpannableText() {
        val ss = Utility.getSpannableString(resources.getString(R.string.sign_text_in_login),navigate = ::navigateToSignup)
        loginFragmentBinding!!.loginText.movementMethod = LinkMovementMethod.getInstance()
        loginFragmentBinding!!.loginText.setText(ss, TextView.BufferType.SPANNABLE)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity?)?.enableLogout(false)
    }
    private fun navigateToSignup(){
        findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
    }
}
