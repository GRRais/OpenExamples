package ru.rayanis.stroyka.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.rayanis.stroyka.MainActivity
import ru.rayanis.stroyka.R
import ru.rayanis.stroyka.act.EditObjectStroyAct
import ru.rayanis.stroyka.model.ObjectStroy
import ru.rayanis.stroyka.databinding.ObjectListItemBinding

class ObjStroyRcAdapter(val act: MainActivity): RecyclerView.Adapter<ObjStroyRcAdapter.ObjectHolder>() {
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
        fun setData(objStroy: ObjectStroy) = with(b) {
            //tvTitle.text = objectStroy
            tvArea.text = objStroy.area
            tvVillage.text = objStroy.village
            tvOrganization.text = objStroy.organization
            tvDescription.text = objStroy.description
            if (objStroy.isActive) {
                ibActive.setImageResource(R.drawable.ic_active_pressed)
            } else {
                ibActive.setImageResource(R.drawable.ic_active_normal)
            }
            showEditPanel(isOwner(objStroy))
            ibActive.setOnClickListener {
                act.onActiveClicked(objStroy)
            }
            ibEditObject.setOnClickListener(onClickEdit(objStroy))
            ibDeleteObject.setOnClickListener{
                act.onDeleteItem(objStroy)
            }
        }


        private fun onClickEdit(objectStroy: ObjectStroy): View.OnClickListener {
            return View.OnClickListener {
                val editIntent = Intent(act, EditObjectStroyAct::class.java).apply {
                    putExtra(MainActivity.EDIT_STATE, true)
                    putExtra(MainActivity.OBJSTR_DATA, objectStroy)
                }
                act.startActivity(editIntent)
            }
        }

        //определяем, владелец созданного объекта зашел или нет
        private fun isOwner(objectStroy: ObjectStroy): Boolean {
            return objectStroy.uid == act.mAuth.uid
        }

        //отображаем панель редактирования объекта, если зашел владелец, иначе скрываем
        private fun showEditPanel(isOwner: Boolean) {
            if (isOwner) {
                b.editPanel.visibility = View.VISIBLE
            } else {
                b.editPanel.visibility = View.GONE
            }
        }
    }

    interface  Listener {
        fun onDeleteItem(objectStroy: ObjectStroy)
        fun onActiveClicked(objectStroy: ObjectStroy)
    }
}