package ru.rayanis.stroyka.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.rayanis.stroyka.data.ObjectStroy
import ru.rayanis.stroyka.databinding.ObjectListItemBinding

class ObjectStroyRcAdapter: RecyclerView.Adapter<ObjectStroyRcAdapter.ObjectHolder>() {
    val objectArray = ArrayList<ObjectStroy>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectHolder {
        val b = ObjectListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ObjectHolder(b)
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

    class ObjectHolder(val b: ObjectListItemBinding): RecyclerView.ViewHolder(b.root) {
        fun setData(objectStroy: ObjectStroy) {
            b.apply {
                //tvTitle.text = objectStroy
                tvArea.text = objectStroy.area
                tvVillage.text = objectStroy.village
                tvOrganization.text = objectStroy.organization
                tvDescription.text = objectStroy.description

            }
        }
    }
}