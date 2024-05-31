package com.bahrlou.filesample

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bahrlou.filesample.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    companion object {
        var ourViewType = 0 //linear
        var ourSpanCount = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val file = getExternalFilesDir(null)!! //null means externalStorage of App
        val path = file.path //main path of external storage
        loadFragment(path)

    }

    private fun loadFragment(path: String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frame_main_container, FileFragment(path))
        transaction.commit()
    }
}