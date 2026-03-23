package com.farmassist.app.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.farmassist.app.R
import com.farmassist.app.data.local.database.FarmAssistDatabase
import com.farmassist.app.data.remote.RssFeedService
import kotlinx.coroutines.launch

class GenericListActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context, type: String): Intent {
            return Intent(context, GenericListActivity::class.java).apply {
                putExtra("TYPE", type)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generic_list)

        val type = intent.getStringExtra("TYPE") ?: "SCHEMES"
        val listView = findViewById<ListView>(R.id.listView)
        val tvTitle = findViewById<TextView>(R.id.tvTitle)

        val database = FarmAssistDatabase.getDatabase(this)
        val dao = database.farmAssistDao()

        lifecycleScope.launch {
            val displayList = mutableListOf<String>()
            when (type) {
                "TERRACE" -> {
                    tvTitle.text = "Terrace Farming"
                    val items = dao.getAllTerraceFarmingData()
                    displayList.addAll(items.map { "${it.crop}: Sunlight - ${it.sunlight}, Water - ${it.water}, Days - ${it.days}" })
                }
                "WASTE" -> {
                    tvTitle.text = "Waste Management"
                    val items = dao.getAllWasteManagementData()
                    displayList.addAll(items.map { "${it.waste} -> ${it.reuse}\nSteps: ${it.steps.joinToString()}" })
                }
                "SCHEMES" -> {
                    tvTitle.text = "Government Schemes"
                    val items = dao.getAllGovtSchemes()
                    displayList.addAll(items.map { "${it.name}\nBenefit: ${it.benefit}\nEligibility: ${it.eligibility}" })
                }
                "NEWS" -> {
                    tvTitle.text = "Agriculture News"
                    val feedUrl = "https://kisan.net/feed/" 
                    val items = RssFeedService().fetchAgriNews(feedUrl)
                    if (items.isEmpty()) {
                        displayList.add("No news available currently or offline.")
                    } else {
                        displayList.addAll(items.map { "${it.title}\n\n${it.description}" })
                    }
                }
            }

            val adapter = ArrayAdapter(this@GenericListActivity, android.R.layout.simple_list_item_1, displayList)
            listView.adapter = adapter
        }
    }
}
