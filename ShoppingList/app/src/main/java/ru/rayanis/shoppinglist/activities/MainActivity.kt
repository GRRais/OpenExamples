package ru.rayanis.shoppinglist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ru.rayanis.shoppinglist.R
import ru.rayanis.shoppinglist.databinding.ActivityMainBinding
import ru.rayanis.shoppinglist.dialogs.NewListDialog
import ru.rayanis.shoppinglist.fragments.FragmentManager
import ru.rayanis.shoppinglist.fragments.NoteFragment

class MainActivity : AppCompatActivity(), NewListDialog.Listener {

    lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        setBottomNavListener()
    }

    private fun setBottomNavListener() {
        b.bNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.settings -> {}
                R.id.notes -> {
                    FragmentManager.setFragment(NoteFragment.newInstance(), this)
                }
                R.id.shop_list -> {}
                R.id.new_item -> {
                    //FragmentManager.currentFrag?.onClickNew()
                    NewListDialog.showDialog(this, this)
                }
            }
            true
        }
    }

    override fun onClick(name: String) {
        Log.d("MyLog", "Name: $name")
    }
}