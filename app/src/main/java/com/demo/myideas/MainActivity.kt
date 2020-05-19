package com.demo.myideas


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.demo.myideas.ui.ideas.AddIdeasFragment
import com.demo.myideas.ui.ideas.IdeasFragment
import com.demo.myideas.ui.login.LoginViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    var showMenu = false
    private val navController by lazy { findNavController(R.id.nav_host_fragment) }
    private val viewModel: LoginViewModel by lazy {
        ViewModelProviders
            .of(this, viewModelFactory)
            .get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.light_bulb)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.action_settings).setVisible(showMenu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val navHostFragment: NavHostFragment? =
                    supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
               val activeFragment =  navHostFragment!!.childFragmentManager.fragments[0]
                if(activeFragment is IdeasFragment){
                   activeFragment.logoutUser()
                }else if(activeFragment is AddIdeasFragment){
                    activeFragment.logoutUser()
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun enableLogout(enable: Boolean) {
        showMenu = enable
        invalidateOptionsMenu()
    }


}
