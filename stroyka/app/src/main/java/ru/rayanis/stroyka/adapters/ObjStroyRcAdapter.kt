package ru.rayanis.stroyka.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.rayanis.stroyka.MainActivity
import ru.rayanis.stroyka.R
import ru.rayanis.stroyka.act.DescriptionActivity
import ru.rayanis.stroyka.act.EditObjectsAct
import ru.rayanis.stroyka.model.ObjectStroy
import ru.rayanis.stroyka.databinding.ObjectListItemBinding

class ObjStroyRcAdapter(val act: MainActivity): RecyclerView.Adapter<ObjStroyRcAdapter.ObjectHolder>() {
    val objStroyArray = ArrayList<ObjectStroy>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectHolder {
        val b = ObjectListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ObjectHolder(b, act)
    }

    override fun onBindViewHolder(holder: ObjectHolder, position: Int) {
        holder.setData(objStroyArray[position])
    }

    override fun getItemCount(): Int {
        return objStroyArray.size
    }

    fun updateAdapter(newList: List<ObjectStroy>) {
        val tempArray = ArrayList<ObjectStroy>()
        tempArray.addAll(objStroyArray)
        tempArray.addAll(newList)
        val diffResult = DiffUtil.calculateDiff(DiffUtilHelper(objStroyArray, tempArray))
        diffResult.dispatchUpdatesTo(this)
        objStroyArray.clear()
        objStroyArray.addAll(tempArray)
    }

    fun updateAdapterWithClear(newList: List<ObjectStroy>) {
        val diffResult = DiffUtil.calculateDiff(DiffUtilHelper(objStroyArray, newList))
        diffResult.dispatchUpdatesTo(this)
        objStroyArray.clear()
        objStroyArray.addAll(newList)
    }

    class ObjectHolder(val b: ObjectListItemBinding, val act: MainActivity): RecyclerView.ViewHolder(b.root) {
        fun setData(objStroy: ObjectStroy) = with(b) {
            tvArea.text = objStroy.area
            tvVillage.text = objStroy.village
            tvOrganization.text = objStroy.organization
            tvDescription.text = objStroy.description
            Picasso.get().load(objStroy.mainImage).into(mainImage)

            isActive(objStroy)
            showEditPanel(isOwner(objStroy))
            mainOnClick(objStroy)
        }

        //обрабатываем слушателей нажатий
        private fun mainOnClick(objStroy: ObjectStroy) = with(b) {
            ibActive.setOnClickListener {
                if (act.mAuth.currentUser?.isAnonymous == false)
                    act.onActiveClicked(objStroy)
            }
            ibEditObject.setOnClickListener(onClickEdit(objStroy))
            ibDeleteObject.setOnClickListener{
                act.onDeleteItem(objStroy)
            }
            itemView.setOnClickListener {
                val i = Intent(b.root.context, DescriptionActivity::class.java)
                i.putExtra("OBJSTROY", objStroy)
                b.root.context.startActivity(i)
            }
        }

        //определяем, активный объект или нет
        private fun isActive(objStroy: ObjectStroy) {
            if (objStroy.isActive) {
                b.ibActive.setImageResource(R.drawable.ic_active_pressed)
            } else {
                b.ibActive.setImageResource(R.drawable.ic_active_normal)
            }
        }


        private fun onClickEdit(objectStroy: ObjectStroy): View.OnClickListener {
            return View.OnClickListener {
                val editIntent = Intent(act, EditObjectsAct::class.java).apply {
                    putExtra(MainActivity.EDIT_STATE, true)
                    putExtra(MainActivity.OBJSTROY_DATA, objectStroy)
                }
                act.startActivity(editIntent)
            }
        }

        //определяем, владелец созданного объекта или нет
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