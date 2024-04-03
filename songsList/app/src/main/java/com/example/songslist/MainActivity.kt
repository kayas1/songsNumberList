package com.example.songslist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.RadioGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.songslist.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import java.util.stream.IntStream

val TAG: String = "asdf"

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val helper: SongDBHelper by lazy { SongDBHelper(this, "song.db", null, 1) }
    private val adapter: SongAdapter by lazy { SongAdapter() }
    private val requestActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode != Activity.RESULT_OK) return@registerForActivityResult
            adapter.songList = helper.selectAllSongs()
            val k = it.data?.getIntExtra("position", -1) ?: -1
            if (k == -1) adapter.notifyItemChanged(adapter.itemCount - 1)
            else adapter.notifyItemChanged(k)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val clickListener = object : SongAdapter.ItemClickListener {
            override fun onItemClick(position: Int) {
                Intent(this@MainActivity, SongAddActivity::class.java).apply {
                    putExtra("idx", adapter.songList[position].id)
                    putExtra("position", position)
                    requestActivity.launch(this)
                }
            }

            override fun onMenuClick(position: Int) {
                helper.deleteSong(adapter.songList[position].id.toString())
                adapter.songList = helper.selectAllSongs()
                adapter.notifyItemRemoved(position)
            }
        }

        binding.fab.setOnClickListener {

            Intent(this, SongAddActivity::class.java).apply { requestActivity.launch(this) }
        }

        binding.songCategory.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> adapter.songList = helper.selectAllSongs()
                    else -> adapter.songList = helper.selectSongsByCategory(tab.position - 1)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        with(adapter) {
            itemClickListener = clickListener
            songList = helper.selectAllSongs()
            IntStream.range(0, helper.getCount()).forEach { notifyItemChanged(it) }
        }

        binding.recyclerView.apply {
            adapter = this@MainActivity.adapter
            addItemDecoration(DividerItemDecoration(this@MainActivity, LinearLayout.VERTICAL))
        }
    }
}