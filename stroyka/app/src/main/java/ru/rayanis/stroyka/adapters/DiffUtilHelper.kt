package ru.rayanis.stroyka.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.rayanis.stroyka.model.ObjectStroy

class DiffUtilHelper(val oldList: List<ObjectStroy>, val newList: List<ObjectStroy>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].key == newList[newItemPosition].key
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}