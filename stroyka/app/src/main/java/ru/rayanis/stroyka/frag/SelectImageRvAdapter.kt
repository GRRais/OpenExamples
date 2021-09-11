package ru.rayanis.stroyka.frag

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.rayanis.stroyka.R
import ru.rayanis.stroyka.act.EditObjectsAct
import ru.rayanis.stroyka.databinding.SelectImageFragItemBinding
import ru.rayanis.stroyka.utils.AdapterCallback
import ru.rayanis.stroyka.utils.ImageManager
import ru.rayanis.stroyka.utils.ImagePicker
import ru.rayanis.stroyka.utils.ItemTouchMoveCallback

class SelectImageRvAdapter(val adapterCallback: AdapterCallback): RecyclerView.Adapter<SelectImageRvAdapter.ImageHolder>(), ItemTouchMoveCallback.ItemTouchAdapter {

    val mainArray = ArrayList<Bitmap>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageHolder {
        val b = SelectImageFragItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageHolder(b, parent.context, this)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.setData(mainArray[position])
    }

    override fun getItemCount(): Int {
        return mainArray.size
    }

    override fun onMove(startPos: Int, targetPos: Int) {
        val targetItem = mainArray[targetPos]
        mainArray[targetPos] = mainArray[startPos]
        mainArray[startPos] = targetItem
        notifyItemMoved(startPos, targetPos)
    }

    override fun onClear() {
        notifyDataSetChanged()
    }

    class ImageHolder(val b: SelectImageFragItemBinding, val context: Context, val adapter: SelectImageRvAdapter) : RecyclerView.ViewHolder(b.root) {

        fun setData(bitmap: Bitmap){

            b.imEditImage.setOnClickListener {
                //ImagePicker.getImages(context as EditObjectsAct, 1, ImagePicker.REQUEST_CODE_GET_SINGLE_IMAGE)
                //context.editImagePos = adapterPosition
            }

            b.imDelete.setOnClickListener {
                adapter.mainArray.removeAt(adapterPosition)
                adapter.notifyItemRemoved(adapterPosition)
                for (n in 0 until adapter.mainArray.size) adapter.notifyItemChanged(n)
                adapter.adapterCallback.onItemDelete()
            }

            b.tvTitle.text = context.resources.getStringArray(R.array.title_array)[adapterPosition]
            ImageManager.chooseScaleType(b.imageContent, bitmap)
            b.imageContent.setImageBitmap(bitmap)
        }
    }

    fun updateAdapter(newList: List<Bitmap>, needClear: Boolean){
        if (needClear) mainArray.clear()
        mainArray.addAll(newList)
        notifyDataSetChanged()
    }

}