package ru.rayanis.stroyka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ru.rayanis.stroyka.databinding.ActivityMainBinding
import ru.rayanis.stroyka.dialoghelper.DialogConst
import ru.rayanis.stroyka.dialoghelper.DialogHelper

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var tvAccount: TextView
    private lateinit var b: ActivityMainBinding
    private val dialogHelper = DialogHelper(this)
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        init()

    }

    override fun onStart() {
        super.onStart()
        uiUpdate(mAuth.currentUser)
    }

    private fun init() {
        val toggle = ActionBarDrawerToggle(
            this,
            b.drawerLayout,
            b.mainContent.toolbar,
            R.string.open,
            R.string.close
        )
        b.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        b.navView.setNavigationItemSelectedListener(this)
        tvAccount = b.navView.getHeaderView(0).findViewById(R.id.tvAccountEmail)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.objects -> {
                Toast.makeText(this, "Pressed objects", Toast.LENGTH_LONG).show()
            }
            R.id.materials -> {
                Toast.makeText(this, "Pressed materials", Toast.LENGTH_LONG).show()
            }
            R.id.sign_in -> {
                dialogHelper.createSignDialog(DialogConst.SIGN_IN_STATE)
            }
            R.id.sign_up -> {
                dialogHelper.createSignDialog(DialogConst.SIGN_UP_STATE)
            }
            R.id.sign_out -> {
                uiUpdate(null)
            mAuth.signOut()
            }
        }
        b.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun uiUpdate(user: FirebaseUser?) {
        tvAccount.text = if (user == null) {
            resources.getString(R.string.not_reg)
        } else {
            user.email
        }
    }
}