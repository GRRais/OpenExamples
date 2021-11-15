package ru.rayanis.stroyka

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import ru.rayanis.stroyka.accounthelper.AccountHelper
import ru.rayanis.stroyka.act.CreateEditMaterialAct
import ru.rayanis.stroyka.act.EditObjectsAct
import ru.rayanis.stroyka.adapters.ObjStroyRcAdapter
import ru.rayanis.stroyka.databinding.ActivityMainBinding
import ru.rayanis.stroyka.dialoghelper.DialogConst
import ru.rayanis.stroyka.dialoghelper.DialogHelper
import ru.rayanis.stroyka.dialoghelper.GoogleAccConst
import ru.rayanis.stroyka.model.ObjectStroy
import ru.rayanis.stroyka.viewmodel.FirebaseViewModel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, ObjStroyRcAdapter.Listener {
    private lateinit var tvAccount: TextView
    private lateinit var imAccount: ImageView
    private lateinit var b: ActivityMainBinding
    private val dialogHelper = DialogHelper(this)
    val mAuth = Firebase.auth
    val adapter = ObjStroyRcAdapter(this)
    lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    private val firebaseViewModel: FirebaseViewModel by viewModels()
     private var clearUpdate: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)
        init()
        initRecyclerView()
        initViewModel()
        firebaseViewModel.loadAllObjStroy("0")
        bottomMenuOnClick()
        scrollListener()
    }

    override fun onResume() {
        super.onResume()
        b.mainContent.bNavView.selectedItemId = R.id.id_objects
    }

    //открытие
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.id_new_object) {
            val i = Intent(this, EditObjectsAct::class.java)
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

    //инициализируем лаунчер, и создается колбэк который ожидает результат
    private fun onActivityResult() {
        googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    dialogHelper.accHelper.signInFirebaseWithGoogle(account.idToken!!)
                }
            } catch (e:ApiException) {
                Log.d("MyLog", "Api error: ${e.message}")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        uiUpdate(mAuth.currentUser)
    }

    private fun initViewModel() {
        firebaseViewModel.liveObjStroyData.observe(this, {
            if (!clearUpdate) {
                adapter.updateAdapter(it)
            } else {
                adapter.updateAdapterWithClear(it)
            }
            b.mainContent.tvEmpty.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE
        })
    }

    private fun init() {
        setSupportActionBar(b.mainContent.toolbar)
        onActivityResult()
        navViewSettings()
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
        imAccount = b.navView.getHeaderView(0).findViewById(R.id.imAccountImage)
    }

    //обработка нажатий на кнопки нижнего меню
    private fun bottomMenuOnClick() = with(b) {
        mainContent.bNavView.setOnItemSelectedListener { item ->
            clearUpdate = true
            when(item.itemId) {
                R.id.id_shipments -> {
                    mainContent.toolbar.title = getString(R.string.shipments)
                    //Toast.makeText(this@MainActivity, R.string.shipments, Toast.LENGTH_SHORT).show()
                }
                R.id.id_requests -> {
                    mainContent.toolbar.title = getString(R.string.requests)
                }
                R.id.id_objects -> {
                    firebaseViewModel.loadAllObjStroy("0")
                    mainContent.toolbar.title = getString(R.string.objects)
                }
                R.id.id_active_objects -> {
                    firebaseViewModel.loadActiveObjStroy()
                    mainContent.toolbar.title = getString(R.string.active_objects)
                }
                R.id.id_materials -> {
                    mainContent.toolbar.title = getString(R.string.materials)
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
        clearUpdate = true
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
                if (mAuth.currentUser?.isAnonymous == true) {
                    b.drawerLayout.closeDrawer(GravityCompat.START)
                    return true
                }
                uiUpdate(null)
                mAuth.signOut()
                dialogHelper.accHelper.signOutG()
            }
        }
        b.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    //изменение статуса пользователя в боковом всплывающем меню
    fun uiUpdate(user: FirebaseUser?) {
        if (user == null) {
            dialogHelper.accHelper.signInAnonymously(object : AccountHelper.Listener {
                override fun onComplete() {
                    tvAccount.setText(R.string.guest)
                    imAccount.setImageResource(R.drawable.ic_image_def)
                }
            })
        } else if (user.isAnonymous) {
            tvAccount.setText(R.string.guest)
            imAccount.setImageResource(R.drawable.ic_image_def)
        } else if (!user.isAnonymous) {
            tvAccount.text = user.email
            Picasso.get().load(user.photoUrl).into(imAccount)
        }
    }

    override fun onDeleteItem(objStroy: ObjectStroy) {
        firebaseViewModel.deleteItem(objStroy)
    }

    override fun onActiveClicked(objStroy: ObjectStroy) {
        firebaseViewModel.onActiveClick(objStroy)
    }

    private fun navViewSettings() = with(b){
        val menu = navView.menu
        val objCat = menu.findItem(R.id.objCat)
        val spanObjCat = SpannableString(objCat.title)
        spanObjCat.setSpan(ForegroundColorSpan(
            ContextCompat.getColor(this@MainActivity, R.color.color_red))
            , 0, objCat.title.length, 0)
        objCat.title = spanObjCat

        val accCat = menu.findItem(R.id.accCat)
        val spanAccCat = SpannableString(accCat.title)
        spanAccCat.setSpan(ForegroundColorSpan(
            ContextCompat.getColor(this@MainActivity, R.color.color_red))
            , 0, accCat.title.length, 0)
        accCat.title = spanAccCat
    }

    private fun scrollListener() = with(b.mainContent) {
        rcView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recView, newState)
                if (!recView.canScrollVertically(SCROLL_DOWN)
                     && newState == RecyclerView.SCROLL_STATE_IDLE) {
                         val objStroyList = firebaseViewModel.liveObjStroyData.value!!
                    if (objStroyList.isNotEmpty()) {
                        objStroyList[objStroyList.size - 1]
                            .let { firebaseViewModel.loadAllObjStroy(it.time) }
                    }
                }
            }
        })
    }

    companion object {
        const val EDIT_STATE = "edit_state"
        const val OBJSTROY_DATA = "objstroy_data"
        const val SCROLL_DOWN = 1
    }
}