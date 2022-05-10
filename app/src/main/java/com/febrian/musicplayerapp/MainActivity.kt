package com.febrian.musicplayerapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.febrian.musicplayerapp.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var listMusic: ArrayList<Music> = ArrayList()

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!checkPermission()) {
            requestPermission()
            return
        }

        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION
        )

        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"

        val cursor: Cursor? = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            null
        )

        try {
            while (cursor!!.moveToNext()) {
                val music = Music(cursor.getString(1), cursor.getString(0), cursor.getString(2))
                if (File(music.path).exists()) listMusic.add(music)
            }
        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
        }

        if (listMusic.size == 0) {
            binding.noSongsText.visibility = View.VISIBLE
        } else {
            //recyclerview
            binding.rv.layoutManager = LinearLayoutManager(this)
            binding.rv.adapter = MusicListAdapter(listMusic)
        }

    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this@MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(
                this@MainActivity,
                "READ PERMISSION IS REQUIRED,PLEASE ALLOW FROM SETTTINGS",
                Toast.LENGTH_SHORT
            ).show()
        } else ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            123
        )
    }
}