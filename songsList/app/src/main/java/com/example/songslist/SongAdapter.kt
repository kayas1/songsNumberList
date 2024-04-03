package com.example.songslist

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.songslist.databinding.ItemRowBinding

class SongAdapter : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {
    lateinit var itemClickListener: ItemClickListener
    var songList = mutableListOf<Song>()

    inner class SongViewHolder(private val itemRowBinding: ItemRowBinding) :
        RecyclerView.ViewHolder(itemRowBinding.root), View.OnCreateContextMenuListener {

        init {
            itemView.setOnCreateContextMenuListener(this)
        }

        fun bindInfo(songItem: Song) {
            with(itemRowBinding) {
                title.text = songItem.title
                tj.text = songItem.tj.toString()
                ky.text = songItem.ky.toString()
                info.text = songItem.info
            }

            itemView.setOnClickListener { itemClickListener.onItemClick(layoutPosition) }
        }

        override fun onCreateContextMenu(menu: ContextMenu, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            val menuItem = menu.add("삭제하기")
            menuItem.setOnMenuItemClickListener {
                itemClickListener.onMenuClick(layoutPosition)
                true
            }
        }
    }

    interface ItemClickListener {
        fun onItemClick(position: Int)
        fun onMenuClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemRowBinding = ItemRowBinding.inflate(inflater, parent, false)
        return SongViewHolder(itemRowBinding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.apply { bindInfo(songList[position]) }
    }

    override fun getItemCount() = songList.size

}