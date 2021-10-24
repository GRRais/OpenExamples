package ru.rayanis.stroyka.frag

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.rayanis.stroyka.R
import ru.rayanis.stroyka.act.EditObjectsAct
import ru.rayanis.stroyka.databinding.ListImageFragBinding
import ru.rayanis.stroyka.dialoghelper.ProgressDialog
import ru.rayanis.stroyka.utils.AdapterCallback
import ru.rayanis.stroyka.utils.ImageManager
import ru.rayanis.stroyka.utils.ImagePicker
import ru.rayanis.stroyka.utils.ItemTouchMoveCallback

class ImageListFrag(private val fragCloseInterface: FragmentCloseInterface): Fragment(), AdapterCallback {

    lateinit var b: ListImageFragBinding
    private val adapter = SelectImageRvAdapter(this)
    private val dragCallback = ItemTouchMoveCallback(adapter)
    private val touchHelper = ItemTouchHelper(dragCallback)
    private var job: Job? = null
    private var addImageItem: MenuItem? = null

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
    }

    override fun onItemDelete() {
        addImageItem?.isVisible = true
    }

    fun updateAdapterFromEdit(bitmapList: List<Bitmap>){
        adapter.updateAdapter(bitmapList, true)
    }

    override fun onDetach() {
        super.onDetach()
        fragCloseInterface.onFragClose(adapter.mainArray)
        job?.cancel()
    }

    fun resizeSelectedImages(newList: ArrayList<Uri>, needClear: Boolean, activity: Activity) {
        job = CoroutineScope(Dispatchers.Main).launch {
            val dialog = ProgressDialog.createProgressDialog(activity)
            val bitmapList = ImageManager.imageResize(newList, activity)
            dialog.dismiss()
            adapter.updateAdapter(bitmapList, needClear)
            if (adapter.mainArray.size > 2) addImageItem?.isVisible = false
        }
    }

    private fun setUpToolbar(){
        b.tb.inflateMenu(R.menu.menu_choose_image)
        val deleteItem = b.tb.menu.findItem(R.id.id_delete_image)
        addImageItem = b.tb.menu.findItem(R.id.id_add_image)
        if (adapter.mainArray.size > 2) addImageItem?.isVisible = false

        b.tb.setNavigationOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

        deleteItem.setOnMenuItemClickListener {
            adapter.updateAdapter(ArrayList(), true)
            addImageItem?.isVisible = true
            true
        }

        addImageItem?.setOnMenuItemClickListener {
            val imageCount = ImagePicker.MAX_IMAGE_COUNT - adapter.mainArray.size
            ImagePicker.addImages(activity as EditObjectsAct, imageCount)
            true
        }
    }

     fun updateAdapter(newList: ArrayList<Uri>, activity: Activity) {
         resizeSelectedImages(newList, false, activity)
     }

    fun setSingleImage(uri: Uri, pos: Int) {
        val pBar = b.rcViewSelectImage[pos].findViewById<ProgressBar>(R.id.pBar)
        job = CoroutineScope(Dispatchers.Main).launch {
            pBar.visibility = View.VISIBLE
            val bitmapList = ImageManager.imageResize(arrayListOf(uri), activity as Activity)
            pBar.visibility = View.GONE
            adapter.mainArray[pos] = bitmapList[0]
            adapter.notifyItemChanged(pos)
        }

    }
}