package com.example.songslist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.songslist.databinding.ActivitySongAddBinding
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SongAddActivity : AppCompatActivity() {
    private val binding: ActivitySongAddBinding by lazy {
        ActivitySongAddBinding.inflate(
            layoutInflater
        )
    }
    private val helper: SongDBHelper by lazy { SongDBHelper(this, "song.db", null, 1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var idx = intent.getIntExtra("idx", -1)

        if (idx != -1) {
            val item = helper.selectSong(idx.toString())
            binding.apply {
                songTitle.setText(item?.title)
                tjNumber.setText(item?.tj)
                kyNumber.setText(item?.ky)
                infoContent.setText(item?.info)
                when(item?.category) {
                    0 -> radioKr.isChecked =true
                    1->radioEn.isChecked=true
                    2->radioJp.isChecked=true
                    3->radioOthers.isChecked=true
                }
            }
        }

        binding.save.setOnClickListener {
            val title = binding.songTitle.text.toString()
            val tj = binding.tjNumber.text.toString()
            val ky = binding.kyNumber.text.toString()
            val info = binding.infoContent.text.toString()
            val category: RadioGroup = binding.category
            val checkedCategory: Int =
                category.indexOfChild(findViewById(category.checkedRadioButtonId))

            if (title == "") {
                Toast.makeText(this, "제목을 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
            else{
                if (idx == -1) {
                    if(helper.findSong(title)==null) {
                        helper.insertSong(Song(0, title, tj, ky, info, checkedCategory))
                    }
                    else{
                        Toast.makeText(this, "이미 등록된 곡명입니다!", Toast.LENGTH_SHORT).show()
                        Intent(this, MainActivity::class.java).apply {
                            setResult(RESULT_CANCELED, this)
                            finish()
                        }
                    }
                } else {
                    helper.updateSong(Song(idx, title, tj, ky, info, checkedCategory))
                }

                Intent(this, MainActivity::class.java).apply {
                    setResult(RESULT_OK, this)
                    putExtra("position", intent.getIntExtra("position", -1))
                    finish()
                }
            }
        }

        binding.cancel.setOnClickListener { finish() }
    }
}