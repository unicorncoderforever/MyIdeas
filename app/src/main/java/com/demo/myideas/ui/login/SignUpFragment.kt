package com.demo.myideas.ui.login


import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.demo.myideas.MainActivity
import com.demo.myideas.R
import com.demo.myideas.databinding.SignUpFragmentBinding
import com.demo.myideas.utility.Utility
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class SignUpFragment : DaggerFragment() {
    val  TAG = "SignUpFragment";
    var signUpFragmentBinding : SignUpFragmentBinding? = null

    @Inject
    lateinit  var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: LoginViewModel by lazy {
        ViewModelProviders
            .of(this, viewModelFactory)
            .get(LoginViewModel::class.java)
    }
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        signUpFragmentBinding =  DataBindingUtil.inflate(
            inflater,
            R.layout.sign_up_fragment, container, false)
        addSpannableText()
        (activity as MainActivity?)!!.supportActionBar!!.title = resources.getString(
            R.string.app_name
        )
        (activity as MainActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        return signUpFragmentBinding?.root;
    }

    private fun addSpannableText() {
        val ss = Utility.getSpannableString(resources.getString(R.string.login_text),navigate = ::navigateToLogin);
        signUpFragmentBinding!!.loginText.movementMethod = LinkMovementMethod.getInstance();
        signUpFragmentBinding!!.loginText.setText(ss,TextView.BufferType.SPANNABLE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            mUserInfo.observe(this@SignUpFragment.viewLifecycleOwner, Observer {
                if(it != null && it.accessToken != null && it.refreshToken != null) {
                    navigateToIdea(it.accessToken!!, it.refreshToken!!)
                }else{
                    navigateToLogin()
                }
            })
            error.observe(this@SignUpFragment.viewLifecycleOwner, Observer {
                Toast.makeText(context,it.getErrorMessage(resources), Toast.LENGTH_LONG).show()
            })
        }
        signUpFragmentBinding!!.singUpButton.setOnClickListener {
            signupUser()
        }
    }

    private fun signupUser() {
        val name = signUpFragmentBinding!!.name.text
        val email = signUpFragmentBinding!!.emailId.text
        val password = signUpFragmentBinding!!.password.text
        if(!email.isEmpty() && !password.isEmpty() && !name.isEmpty()){
            if(email.isValidEmail() && Utility.isValidPassword(password.toString())) {
                signUpFragmentBinding!!.progressLayout.visibility = View.VISIBLE
                viewModel.signUpUser(
                    name = name.toString(),
                    userEmail = email.toString(),
                    password = password.toString(),
                    success = {
                        signUpFragmentBinding!!.progressLayout.visibility = View.GONE
                    },
                    failure = {
                        signUpFragmentBinding!!.progressLayout.visibility = View.GONE
                    })
            }else{
                    Utility.showToast(requireContext(),R.string.invalid_input)
            }
        }else{
            Toast.makeText(context,resources.getString(R.string.text_empty), Toast.LENGTH_LONG).show()
        }


    }

    private fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity?)?.enableLogout(false)
    }
    private fun navigateToIdea(accessToken: String , refreshToken : String){
        findNavController().navigate(R.id.action_SignUpFragment_to_ideasFragment)
    }

    private fun navigateToLogin(){
        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
    }
}
