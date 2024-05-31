package com.bahrlou.filesample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bahrlou.filesample.databinding.ItemFileLinearBinding
import java.io.File
import java.net.URLConnection

class FileAdapter(private val fileList: ArrayList<File>, private val fileEvent: FileEvent) :
    RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    private var ourViewType: Int = 0
    // private lateinit var binding: ItemFileLinearBinding

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindViews(file: File) {

            var fileType: String = ""

            val txt = itemView.findViewById<TextView>(R.id.textView)
            txt.text = file.name

            val img = itemView.findViewById<ImageView>(R.id.imageView)


            when {

                file.isDirectory -> {
                    img.setImageResource(R.drawable.ic_folder)
                }

                isImage(file.path) -> {
                    img.setImageResource(R.drawable.ic_image)
                    fileType = "image/*"
                }

                isVideo(file.path) -> {
                    img.setImageResource(R.drawable.ic_video)
                    fileType = "video/*"
                }

                isZip(file.name) -> {
                    img.setImageResource(R.drawable.ic_zip)
                    fileType = "application/zip"

                }

                else -> {
                    img.setImageResource(R.drawable.ic_file)
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


            itemView.setOnLongClickListener {

                fileEvent.onLongClicked(file, adapterPosition)
                true
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {

        val view: View

        if (viewType == 0) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_file_linear, parent, false)
        } else {
            view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_file_grid, parent, false)

        }

        return FileViewHolder(view)


        //val inflaytor = LayoutInflater.from(parent.context)
        /*  binding = ItemFileLinearBinding.inflate(inflaytor, parent, false)
          return FileViewHolder(binding.root)*/
    }

    override fun getItemCount(): Int {
        return fileList.size
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {

        holder.bindViews(fileList[position])
    }

    private fun isImage(path: String): Boolean {
        val mimeType: String = URLConnection.guessContentTypeFromName(path)

        return mimeType.startsWith("image")
    }

    private fun isVideo(path: String): Boolean {
        val mimeType: String = URLConnection.guessContentTypeFromName(path)

        return mimeType.startsWith("video")
    }

    private fun isZip(name: String): Boolean {
        return name.contains(".zip") || name.contains(".rar")
    }

    fun addNewFile(newFile: File) {
        fileList.add(0, newFile)
        notifyItemInserted(0)
    }

    fun deleteFile(file: File, position: Int) {
        fileList.remove(file)
        notifyItemRemoved(position)
    }

    fun changeViewType(newViewType: Int) {
        ourViewType = newViewType
        notifyDataSetChanged()
    }


    interface FileEvent {
        fun onFileClicked(file: File, type: String)
        fun onFolderClicked(path: String)

        fun onLongClicked(file: File, position: Int)
    }

}
