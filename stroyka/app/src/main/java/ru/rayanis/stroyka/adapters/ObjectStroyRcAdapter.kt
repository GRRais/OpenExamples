package ru.rayanis.stroyka.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.rayanis.stroyka.MainActivity
import ru.rayanis.stroyka.act.EditObjectStroyAct
import ru.rayanis.stroyka.model.ObjectStroy
import ru.rayanis.stroyka.databinding.ObjectListItemBinding

class ObjectStroyRcAdapter(val act: MainActivity): RecyclerView.Adapter<ObjectStroyRcAdapter.ObjectHolder>() {
    val objectStroyArray = ArrayList<ObjectStroy>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectHolder {
        val b = ObjectListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ObjectHolder(b, act)
    }

    override fun onBindViewHolder(holder: ObjectHolder, position: Int) {
        holder.setData(objectStroyArray[position])
    }

    override fun getItemCount(): Int {
        return objectStroyArray.size
    }

    fun updateAdapter(newList: List<ObjectStroy>) {
        val diffResult = DiffUtil.calculateDiff(DiffUtilHelper(objectStroyArray, newList))
        diffResult.dispatchUpdatesTo(this)
        objectStroyArray.clear()
        objectStroyArray.addAll(newList)
    }

    class ObjectHolder(val b: ObjectListItemBinding, val act: MainActivity): RecyclerView.ViewHolder(b.root) {
        fun setData(objectStroy: ObjectStroy) = with(b) {
            //tvTitle.text = objectStroy
            tvArea.text = objectStroy.area
            tvVillage.text = objectStroy.village
            tvOrganization.text = objectStroy.organization
            tvDescription.text = objectStroy.description
            showEditPanel(isOwner(objectStroy))
            ibEditObject.setOnClickListener(onClickEdit(objectStroy))
            ibDeleteObject.setOnClickListener{
                act.onDeleteItem(objectStroy)
            }
        }

        private fun onClickEdit(objectStroy: ObjectStroy): View.OnClickListener {
            return View.OnClickListener {
                val editIntent = Intent(act, EditObjectStroyAct::class.java).apply {
                    putExtra(MainActivity.EDIT_STATE, true)
                    putExtra(MainActivity.ADS_DATA, objectStroy)
                }
                act.startActivity(editIntent)
            }
        }

        private fun isOwner(objectStroy: ObjectStroy): Boolean {
            return objectStroy.uid == act.mAuth.uid
        }

        private fun showEditPanel(isOwner: Boolean) {
            if (isOwner) {
                b.editPanel.visibility = View.VISIBLE
            } else {
                b.editPanel.visibility = View.GONE
            }
        }
    }

    interface  DeleteItemListener {
        fun onDeleteItem(objectStroy: ObjectStroy)
    }
}