package com.bahrlou.filesample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bahrlou.filesample.databinding.ItemFileLinearBinding
import java.io.File
import java.net.URLConnection

class FileAdapter(val fileList: ArrayList<File>, val fileEvent: FileEvent) :
    RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    private lateinit var binding: ItemFileLinearBinding

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindViews(file: File) {

            var fileType: String = ""
            binding.textView.text = file.name


            when {

                file.isDirectory -> {
                    binding.imageView.setImageResource(R.drawable.ic_folder)
                }

                isImage(file.path) -> {
                    binding.imageView.setImageResource(R.drawable.ic_image)
                    fileType = "image/*"
                }

                isVideo(file.path) -> {
                    binding.imageView.setImageResource(R.drawable.ic_video)
                    fileType = "video/*"
                }

                isZip(file.name) -> {
                    binding.imageView.setImageResource(R.drawable.ic_zip)
                    fileType = "application/zip"

                }

                else -> {
                    binding.imageView.setImageResource(R.drawable.ic_file)
                    fileType = "text/plain"
                }

            }
            itemView.setOnClickListener {
                if (file.isDirectory) {
                    fileEvent.onFolderClicked(file.path)
                } else {
                    fileEvent.onFileClicked(file, fileType)
                }
            }

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


    interface FileEvent {
        fun onFileClicked(file: File, type: String)
        fun onFolderClicked(path: String)
    }

}
