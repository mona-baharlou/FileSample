package com.bahrlou.filesample

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Visibility
import com.bahrlou.filesample.databinding.FragmentFileBinding
import java.io.File

class FileFragment(val path: String) : Fragment() {

    private lateinit var binding: FragmentFileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFileBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ourFile = File(path)

        binding.txtPath.text = "${ourFile.name} > "

        if (ourFile.isDirectory) {
            val fileList = ArrayList<File>()
            ourFile.listFiles()?.let { fileList.addAll(it) }

            if (fileList.size > 0) {
                val myAdapter = FileAdapter(fileList)
                binding.recyclerMain.adapter = myAdapter
                binding.recyclerMain.layoutManager = LinearLayoutManager(context)

                binding.imgNoData.visibility = View.GONE
                binding.recyclerMain.visibility = View.VISIBLE
            }
            else{
                binding.imgNoData.visibility = View.VISIBLE
                binding.recyclerMain.visibility = View.GONE
            }

        }
    }
}