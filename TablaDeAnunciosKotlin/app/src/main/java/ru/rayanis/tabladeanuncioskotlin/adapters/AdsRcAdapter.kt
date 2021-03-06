package ru.rayanis.tabladeanuncioskotlin.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.rayanis.tabladeanuncioskotlin.MainActivity
import ru.rayanis.tabladeanuncioskotlin.R
import ru.rayanis.tabladeanuncioskotlin.act.DescriptionActivity
import ru.rayanis.tabladeanuncioskotlin.act.EditAdsAct
import ru.rayanis.tabladeanuncioskotlin.model.Ad
import ru.rayanis.tabladeanuncioskotlin.databinding.AdListItemBinding

class AdsRcAdapter(val act: MainActivity): RecyclerView.Adapter<AdsRcAdapter.AdHolder>() {
    val adArray = ArrayList<Ad>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdHolder {
        val b = AdListItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return AdHolder(b, act)
    }

    override fun onBindViewHolder(holder: AdHolder, position: Int) {
        holder.setData(adArray[position])
    }

    override fun getItemCount(): Int {
        return adArray.size
    }

    fun updateAdapter(newList: List<Ad>) {
        val diffResult = DiffUtil.calculateDiff(DiffUtilHelper(adArray, newList))
        diffResult.dispatchUpdatesTo(this)
        adArray.clear()
        adArray.addAll(newList)
    }

    //Класс отвечает за одно объявление
    class AdHolder(val b: AdListItemBinding, val act: MainActivity): RecyclerView.ViewHolder(b.root) {

        //функция, которая заполняет объявление
        fun setData(ad: Ad) = with(b) {
            tvTitle.text = ad.title
            tvDescription.text = ad.description
            tvPrice.text = ad.price
            tvViewCounter.text = ad.viewsCounter
            tvFavCounter.text = ad.favCounter
            Picasso.get().load(ad.mainImage).into(mainImage)
            isFav(ad)
            showEditPanel(isOwner(ad))
            mainOnClick(ad)
        }

        private fun mainOnClick(ad: Ad) = with(b) {
            ibFav.setOnClickListener {
                if (act.mAuth.currentUser?.isAnonymous == false) act.onFavClicked(ad)
            }
            ibEditAd.setOnClickListener(onClickEdit(ad))
            ibDeleteAd.setOnClickListener{
                act.onDeleteItem(ad)
            }
            itemView.setOnClickListener {
                act.onAdViewed(ad)
                val i = Intent(b.root.context, DescriptionActivity::class.java)
                i.putExtra("AD", ad)
                b.root.context.startActivity(i)
            }
        }

        private fun isFav(ad: Ad) {
            if (ad.isFav) {
                b.ibFav.setImageResource(R.drawable.ic_fav_pressed)
            } else {
                b.ibFav.setImageResource(R.drawable.ic_fav_normal)
            }
        }

        private fun onClickEdit(ad: Ad): View.OnClickListener {
            return View.OnClickListener {
                val editIntent = Intent(act, EditAdsAct::class.java).apply {
                    putExtra(MainActivity.EDIT_STATE, true)
                    putExtra(MainActivity.ADS_DATA, ad)
                }
                act.startActivity(editIntent)
            }
        }

        private fun isOwner(ad: Ad): Boolean {
            return ad.uid == act.mAuth.uid
        }

        private fun showEditPanel(isOwner: Boolean) {
            if (isOwner) {
                b.editPanel.visibility = View.VISIBLE
            } else {
                b.editPanel.visibility = View.GONE
            }
        }
    }

    interface Listener {
        fun onDeleteItem(ad: Ad)
        fun onAdViewed(ad: Ad)
        fun onFavClicked(ad: Ad)
    }
}