package com.febrian.musicplayerapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.febrian.musicplayerapp.databinding.ItemMusicBinding

class MusicListAdapter(private val listMusic: ArrayList<Music>) :
    RecyclerView.Adapter<MusicListAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(music: Music, position: Int) {
            binding.musicTitleText.text = music.title

            itemView.setOnClickListener {
                MusicMediaPlayer.getInstance().reset()
                MusicMediaPlayer.currentIndex = position
                val intent = Intent(itemView.context, MusicPlayerActivity::class.java)
                intent.putExtra("LIST", listMusic)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicListAdapter.ViewHolder {
        val view = ItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicListAdapter.ViewHolder, position: Int) {
        holder.bind(listMusic[position], position)
    }

    override fun getItemCount(): Int = listMusic.size
}