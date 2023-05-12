package com.oleksiikravchuk.canadataxcalculator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.oleksiikravchuk.canadataxcalculator.models.Province
import com.oleksiikravchuk.canadataxcalculator.R

class ProvinceArrayAdapter(private val dataArray: Array<Province>)
    : BaseAdapter() {

    override fun getCount(): Int {
        return  dataArray.size
    }

    override fun getItem(position: Int): Any {
        return dataArray[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val itemHolder: ItemHolder
        if (convertView == null) {
            view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_province,parent,false)
            itemHolder = ItemHolder(view)
            view?.tag = itemHolder
        } else {
            view = convertView
            itemHolder = view.tag as ItemHolder
        }
        itemHolder.provinceNameTextView.text = dataArray[position].provinceName
        itemHolder.provinceFlagImageView.setImageResource(dataArray[position].imageId)

        //val id = context.resources.getIdentifier(dataSource.get(position).url, "drawable", context.packageName)
        //vh.img.setBackgroundResource(id)

        return view
    }

    private class ItemHolder(row: View){
        val provinceNameTextView : TextView = row.findViewById(R.id.text_view_province_name)
        val provinceFlagImageView : ImageView = row.findViewById(R.id.image_view_flag)

//        init {
//            provinceNameTextView =
//            provinceFlagImageView =
//        }
    }
}