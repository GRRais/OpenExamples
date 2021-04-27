package ru.rayanis.lessonsqlitekotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.rayanis.lessonsqlitekotlin.databinding.ActivityMainBinding
import ru.rayanis.lessonsqlitekotlin.db.MyAdapter
import ru.rayanis.lessonsqlitekotlin.db.MyDbManager

class MainActivity : AppCompatActivity() {

    private val myDbManager = MyDbManager(this)
    private val myAdapter = MyAdapter(ArrayList(), this)

    private lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        init()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        fillAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    fun onClickNew(view: View) {
        val i = Intent(this, EditActivity::class.java)
        startActivity(i)
    }

    private fun init() {
        b.rcView.layoutManager = LinearLayoutManager(this)
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(b.rcView)
        b.rcView.adapter = myAdapter
    }

    fun initSearchView(){
        b.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d()
                return true
            }
        })
    }

    private fun fillAdapter() {

        val list = myDbManager.readDbData()
        myAdapter.updateAdapter(list)

        if (list.size > 0) {
            b.tvNoElements.visibility = View.GONE
        } else {
            b.tvNoElements.visibility = View.VISIBLE
        }
    }

    private fun  getSwapMg():ItemTouchHelper {
        return ItemTouchHelper(object: ItemTouchHelper.
        SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myAdapter.removeItem(viewHolder.adapterPosition, myDbManager)
            }

        } )
    }
}