package ru.rayanis.stroyka.frag

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.rayanis.stroyka.R
import ru.rayanis.stroyka.databinding.ListImageFragBinding
import ru.rayanis.stroyka.utils.ImageManager
import ru.rayanis.stroyka.utils.ImagePicker
import ru.rayanis.stroyka.utils.ItemTouchMoveCallback

class ImageListFrag(private val fragCloseInterface: FragmentCloseInterface, private val newList: ArrayList<String>): Fragment() {

    lateinit var b: ListImageFragBinding
    val adapter = SelectImageRvAdapter()
    val dragCallback = ItemTouchMoveCallback(adapter)
    val touchHelper = ItemTouchHelper(dragCallback)
    private lateinit var job: Job
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = ListImageFragBinding.inflate(inflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        touchHelper.attachToRecyclerView(b.rcViewSelectImage)
        b.rcViewSelectImage.layoutManager = LinearLayoutManager(activity)
        b.rcViewSelectImage.adapter = adapter
        job = CoroutineScope(Dispatchers.Main).launch {
            val text = ImageManager.imageResize(newList)
            Log.d("MyLog", "Result: $text")
        }
        //adapter.updateAdapter(newList, true)
    }

    override fun onDetach() {
        super.onDetach()
        fragCloseInterface.onFragClose(adapter.mainArray)
        job.cancel()
    }

    private fun setUpToolbar(){
        b.tb.inflateMenu(R.menu.menu_choose_image)
        val deleteItem = b.tb.menu.findItem(R.id.id_delete_image)
        val addImageItem = b.tb.menu.findItem(R.id.id_add_image)

        b.tb.setNavigationOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

        deleteItem.setOnMenuItemClickListener {
            adapter.updateAdapter(ArrayList(), true)
            true
        }

        addImageItem.setOnMenuItemClickListener {
            val imageCount = ImagePicker.MAX_IMAGE_COUNT - adapter.mainArray.size
            ImagePicker.getImages(activity as AppCompatActivity, imageCount, ImagePicker.REQUEST_CODE_GET_IMAGES)
            true
        }
    }

     fun updateAdapter(newList: ArrayList<String>) {
         adapter.updateAdapter(newList, false)
     }

    fun setSingleImage(uri: String, pos: Int) {
        adapter.mainArray[pos] = uri
        adapter.notifyDataSetChanged()
    }
}