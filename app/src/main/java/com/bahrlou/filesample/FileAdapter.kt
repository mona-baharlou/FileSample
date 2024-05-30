package com.bahrlou.filesample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bahrlou.filesample.databinding.ItemFileLinearBinding
import java.io.File

class FileAdapter(val fileList: ArrayList<File>) :
    RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    private lateinit var binding: ItemFileLinearBinding

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindViews(file: File) {

            binding.textView.text = file.name
            binding.imageView.setImageResource(R.drawable.ic_folder)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val inflaytor = LayoutInflater.from(parent.context)
        binding = ItemFileLinearBinding.inflate(inflaytor, parent, false)
        return FileViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return fileList.size
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {

        holder.bindViews(fileList[position])
    }
}