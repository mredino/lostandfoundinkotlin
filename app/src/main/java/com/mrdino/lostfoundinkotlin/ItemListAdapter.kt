package com.mrdino.lostfoundinkotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ItemListAdapter(private val context: Context, private val items: List<Item>) : BaseAdapter() {

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Item {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_list_row, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val item = getItem(position)
        viewHolder.bind(item)

        return view
    }

    private class ViewHolder(view: View) {
        private val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        private val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
        private val contactTextView: TextView = view.findViewById(R.id.contactTextView)
        private val dateTextView: TextView = view.findViewById(R.id.dateTextView)
        private val locationTextView: TextView = view.findViewById(R.id.locationTextView)

        fun bind(item: Item) {
            nameTextView.text = item.name
            descriptionTextView.text = item.description
            contactTextView.text = item.contact
            dateTextView.text = item.date
            locationTextView.text = item.location
        }
    }
}
