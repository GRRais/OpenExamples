package ru.rayanis.stroyka.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import ru.rayanis.stroyka.model.ObjectStroy
import ru.rayanis.stroyka.databinding.ObjectListItemBinding

class ObjectStroyRcAdapter(val auth: FirebaseAuth): RecyclerView.Adapter<ObjectStroyRcAdapter.ObjectHolder>() {
    val objectArray = ArrayList<ObjectStroy>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectHolder {
        val b = ObjectListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ObjectHolder(b, auth)
    }

    override fun onBindViewHolder(holder: ObjectHolder, position: Int) {
        holder.setData(objectArray[position])
    }

    override fun getItemCount(): Int {
        return objectArray.size
    }

    fun updateAdapter(newList: List<ObjectStroy>) {
        objectArray.clear()
        objectArray.addAll(newList)
        notifyDataSetChanged()
    }

    class ObjectHolder(val b: ObjectListItemBinding, val auth: FirebaseAuth): RecyclerView.ViewHolder(b.root) {
        fun setData(objectStroy: ObjectStroy) {
            b.apply {
                //tvTitle.text = objectStroy
                tvArea.text = objectStroy.area
                tvVillage.text = objectStroy.village
                tvOrganization.text = objectStroy.organization
                tvDescription.text = objectStroy.description
            }
            showEditPanel(isOwner(objectStroy))
        }

        private fun isOwner(objectStroy: ObjectStroy): Boolean {
            return objectStroy.uid == auth.uid
        }

        private fun showEditPanel(isOwner: Boolean) {
            if (isOwner) {
                b.editPanel.visibility = View.VISIBLE
            } else {
                b.editPanel.visibility = View.GONE
            }
        }
    }
}