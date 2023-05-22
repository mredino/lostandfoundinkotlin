package com.mrdino.lostfoundinkotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ShowItemsActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: ItemListAdapter
    private lateinit var items: ArrayList<Item>
    private lateinit var goBackButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_items)

        listView = findViewById(R.id.itemsListView)
        items = getItemsFromDatabase()
        adapter = ItemListAdapter(this, items)
        listView.adapter = adapter
        goBackButton = findViewById(R.id.goBackButton)

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, view, position, _ ->
            val removeButton: Button = view.findViewById(R.id.removeButton)
            removeButton.visibility = View.VISIBLE

            removeButton.setOnClickListener {
                val selectedItem = adapter.getItem(position)
                removeItemFromDatabase(selectedItem)
                items.remove(selectedItem)
                adapter.notifyDataSetChanged()
            }
        }

        goBackButton.setOnClickListener {
            //val intent = Intent(this, MainActivity::class.java)
            //startActivity(intent)
            finish()
        }
    }

    private fun getItemsFromDatabase(): ArrayList<Item> {
        val dbHelper = DatabaseHelper(this)
        return dbHelper.getAllItems()
    }

    private fun removeItemFromDatabase(item: Item) {
        val dbHelper = DatabaseHelper(this)
        dbHelper.deleteItem(item)
    }
}
