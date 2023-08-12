package com.oleksiikravchuk.canadataxcalculator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.oleksiikravchuk.canadataxcalculator.R
import com.oleksiikravchuk.canadataxcalculator.models.CartItem

class CartItemAdapter(val salesItemList: ArrayList<CartItem>) :
    RecyclerView.Adapter<CartItemAdapter.ItemViewHolder>() {


    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemsTag: EditText
        var total: TextView
        var deleteButton: MaterialButton
        var cancelButton: MaterialButton
        var confirmButton: MaterialButton

        init {
            itemsTag = itemView.findViewById(R.id.text_view_item_tag)
            total = itemView.findViewById(R.id.text_view_item_total)
            deleteButton = itemView.findViewById(R.id.icon_delete)
            cancelButton = itemView.findViewById(R.id.icon_cancel)
            confirmButton = itemView.findViewById(R.id.icon_confirm)

            setListeners()
        }

        private fun setListeners() {
            deleteButton.setOnClickListener{
                it.visibility = View.GONE
                cancelButton.visibility = View.VISIBLE
                confirmButton.visibility = View.VISIBLE
            }

            confirmButton.setOnClickListener{
                it.visibility = View.GONE
                deleteButton.visibility = View.VISIBLE
                cancelButton.visibility = View.GONE
                salesItemList.removeAt(adapterPosition)
            }

            cancelButton.setOnClickListener{
                it.visibility = View.GONE
                confirmButton.visibility = View.GONE
                deleteButton.visibility = View.VISIBLE
            }

            itemsTag.addTextChangedListener {
                salesItemList[adapterPosition].tag = it.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return ItemViewHolder(v)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemsTag.setText(salesItemList[position].tag)
        holder.total.text = String.format("%.2f$", salesItemList[position].total.toString())
    }

    override fun getItemCount(): Int {
        return salesItemList.size
    }
}