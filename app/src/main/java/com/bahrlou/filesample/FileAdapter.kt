package com.bahrlou.filesample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bahrlou.filesample.databinding.ItemFileLinearBinding
import java.io.File
import java.net.URLConnection

class FileAdapter(val fileList: ArrayList<File>) :
    RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    private lateinit var binding: ItemFileLinearBinding

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindViews(file: File) {

            binding.textView.text = file.name
            if (file.isDirectory)
                binding.imageView.setImageResource(R.drawable.ic_folder)
            else if (file.isFile)
                binding.imageView.setImageResource(R.drawable.ic_file)


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

    fun isImage(path: String): Boolean {
        val mimeType: String = URLConnection.guessContentTypeFromName(path)

        return mimeType.startsWith("image")
    }

    fun isVideo(path: String): Boolean {
        val mimeType: String = URLConnection.guessContentTypeFromName(path)

        return mimeType.startsWith("video")
    }

    fun isZip(name: String): Boolean {
        return name.contains(".zip") || name.contains(".rar")
    }


}
