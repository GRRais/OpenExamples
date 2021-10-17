package ru.rayanis.stroyka

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ru.rayanis.stroyka.act.CreateEditMaterialAct
import ru.rayanis.stroyka.act.EditObjectStroyAct
import ru.rayanis.stroyka.adapters.ObjStroyRcAdapter
import ru.rayanis.stroyka.databinding.ActivityMainBinding
import ru.rayanis.stroyka.dialoghelper.DialogConst
import ru.rayanis.stroyka.dialoghelper.DialogHelper
import ru.rayanis.stroyka.dialoghelper.GoogleAccConst
import ru.rayanis.stroyka.model.ObjectStroy
import ru.rayanis.stroyka.viewmodel.FirebaseViewModel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, ObjStroyRcAdapter.Listener {
    private lateinit var tvAccount: TextView
    private lateinit var b: ActivityMainBinding
    private val dialogHelper = DialogHelper(this)
    val mAuth = Firebase.auth
    val adapter = ObjStroyRcAdapter(this)
    private val firebaseViewModel: FirebaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)
        init()
        initRecyclerView()
        initViewModel()
        firebaseViewModel.loadAllObjStroy()
        bottomMenuOnClick()
    }

    override fun onResume() {
        super.onResume()
        b.mainContent.bNavView.selectedItemId = R.id.id_shipments
    }

    //открытие
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.id_new_object) {
            val i = Intent(this,EditObjectStroyAct::class.java)
            startActivity(i)
        }
        if (item.itemId == R.id.id_new_edit_material) {
            val i = Intent(this,CreateEditMaterialAct::class.java)
            startActivity(i)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE) {
           // Log.d("MyLog", "Sign in result")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    dialogHelper.accHelper.signInFirebaseWithGoogle(account.idToken!!)
                }
            } catch (e:ApiException) {
                Log.d("MyLog", "Api error: ${e.message}")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
        uiUpdate(mAuth.currentUser)
    }

    private fun initViewModel() {
        firebaseViewModel.liveObjStroyData.observe(this, {
            adapter.updateAdapter(it)
        })
    }

    private fun init() {
        setSupportActionBar(b.mainContent.toolbar)
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

    //обработка нажатий на кнопки нижнего меню
    private fun bottomMenuOnClick() = with(b) {
        mainContent.bNavView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.id_shipments -> {
                    mainContent.toolbar.title = getString(R.string.shipments)
                    Toast.makeText(this@MainActivity, R.string.shipments, Toast.LENGTH_SHORT).show()
                }
                R.id.id_requests -> {
                    mainContent.toolbar.title = getString(R.string.requests)
                    Toast.makeText(this@MainActivity, R.string.requests, Toast.LENGTH_SHORT).show()
                }
                R.id.id_objects -> {
                    firebaseViewModel.loadAllObjStroy()
                    mainContent.toolbar.title = getString(R.string.objects)
                    Toast.makeText(this@MainActivity, R.string.objects, Toast.LENGTH_SHORT).show()
                }
                R.id.id_active_objects -> {
                    firebaseViewModel.loadActiveObjStroy()
                    mainContent.toolbar.title = getString(R.string.active_objects)
                    Toast.makeText(this@MainActivity, R.string.active_objects, Toast.LENGTH_SHORT).show()
                }
                R.id.id_materials -> {
                    mainContent.toolbar.title = getString(R.string.materials)
                    Toast.makeText(this@MainActivity, R.string.materials, Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
    }

    private fun initRecyclerView() {
        b.apply {
            mainContent.rcView.layoutManager = LinearLayoutManager(this@MainActivity)
            mainContent.rcView.adapter = adapter
        }
    }

    //обработка нажатий на пункты бокового всплывающего меню
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

    //изменение статуса пользователя в боковом всплывающем меню
    fun uiUpdate(user: FirebaseUser?) {
        tvAccount.text = if (user == null) {
            resources.getString(R.string.not_reg)
        } else {
            user.email
        }
    }

    companion object {
        const val EDIT_STATE = "edit_state"
        const val OBJSTR_DATA = "objstroy_data"
    }

    override fun onDeleteItem(objStroy: ObjectStroy) {
        firebaseViewModel.deleteItem(objStroy)
    }

    override fun onActiveClicked(objStroy: ObjectStroy) {
        firebaseViewModel.onActiveClick(objStroy)
    }
}