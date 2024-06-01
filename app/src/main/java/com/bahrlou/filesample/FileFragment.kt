package com.bahrlou.filesample

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bahrlou.filesample.databinding.DialogAddFileBinding
import com.bahrlou.filesample.databinding.DialogAddFolderBinding
import com.bahrlou.filesample.databinding.DialogDeleteItemBinding
import com.bahrlou.filesample.databinding.FragmentFileBinding
import java.io.File


//linear -> 0
//grid -> 1

class FileFragment(val path: String) : Fragment(), FileAdapter.FileEvent {

    private lateinit var binding: FragmentFileBinding
    private lateinit var myAdapter: FileAdapter


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

        if(MainActivity.ourViewType == 0){
            binding.btnShowType.setImageResource(R.drawable.ic_list)
        }
        else{
            binding.btnShowType.setImageResource(R.drawable.ic_grid)

        }

        val ourFile = File(path)
        binding.txtPath.text = "${ourFile.name} > "

        setRecyclerView(ourFile)

        setButtonClicks()
    }

    private fun setButtonClicks() {
        binding.btnAddFolder.setOnClickListener {
            createFolder()
        }

        binding.btnAddFile.setOnClickListener {
            createFile()
        }

        binding.btnShowType.setOnClickListener {
            if (MainActivity.ourViewType == 0) {

                MainActivity.ourViewType = 1
                MainActivity.ourSpanCount = 3
                myAdapter.changeViewType(MainActivity.ourViewType)

                binding.recyclerMain.layoutManager = GridLayoutManager(
                    context,
                    MainActivity.ourSpanCount
                )

                binding.btnShowType.setImageResource(R.drawable.ic_grid)

            } else if (MainActivity.ourViewType == 1) {

                MainActivity.ourViewType = 0
                MainActivity.ourSpanCount = 1

                myAdapter.changeViewType(
                    MainActivity.ourViewType

                )

                binding.recyclerMain.layoutManager = GridLayoutManager(
                    context,
                    MainActivity.ourSpanCount
                )

                binding.btnShowType.setImageResource(R.drawable.ic_list)

            }
        }
    }



    private fun createFile() {
        val dialog = AlertDialog.Builder(requireContext()).create()

        val addFileBinding = DialogAddFileBinding.inflate(layoutInflater)
        dialog.setView(addFileBinding.root)
        //dialog.create()
        dialog.show()

        addFileBinding.btnCancel.setOnClickListener { dialog.dismiss() }
        addFileBinding.btnCreate.setOnClickListener {

            val newFileName = addFileBinding.edtAddFolder.text.toString()

            //file/pic ---> for example
            val newFile = File(path + File.separator + newFileName)

            if (!newFile.exists()) {
                if (newFile.createNewFile()) {
                    myAdapter.addNewFile(newFile)
                    binding.recyclerMain.scrollToPosition(0)
                    binding.recyclerMain.visibility = View.VISIBLE
                    binding.imgNoData.visibility = View.GONE
                }
            }

            dialog.dismiss()
        }
    }

    private fun createFolder() {

        val dialog = AlertDialog.Builder(requireContext()).create()

        val addFolderBinding = DialogAddFolderBinding.inflate(layoutInflater)
        dialog.setView(addFolderBinding.root)
        //dialog.create()
        dialog.show()

        addFolderBinding.btnCancel.setOnClickListener {
            dialog.dismiss() }
        addFolderBinding.btnCreate.setOnClickListener {

            val newFolderName = addFolderBinding.edtAddFolder.text.toString()

            //file/pic ---> for example
            val newFile = File(path + File.separator + newFolderName)

            if (!newFile.exists()) {
                if (newFile.mkdir()) {
                    myAdapter.addNewFile(newFile)
                    binding.recyclerMain.scrollToPosition(0)
                    binding.recyclerMain.visibility = View.VISIBLE
                    binding.imgNoData.visibility = View.GONE
                }
            }

            dialog.dismiss()
        }
    }

    private fun setRecyclerView(ourFile: File) {
        if (ourFile.isDirectory) {
            val fileList = ArrayList<File>()
            ourFile.listFiles()?.let {
                fileList.addAll(it)
                fileList.sort()
            }

            myAdapter = FileAdapter(fileList, this)
            binding.recyclerMain.adapter = myAdapter
            binding.recyclerMain.layoutManager =
                GridLayoutManager(
                    context,
                    MainActivity.ourSpanCount,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            //LinearLayoutManager(context)

            myAdapter.changeViewType(MainActivity.ourViewType)

            if (fileList.size > 0) {

                binding.imgNoData.visibility = View.GONE
                binding.recyclerMain.visibility = View.VISIBLE
            } else {
                binding.imgNoData.visibility = View.VISIBLE
                binding.recyclerMain.visibility = View.GONE
            }

        }
    }

    override fun onFileClicked(file: File, type: String) {
        val intent = Intent(Intent.ACTION_VIEW)

        //api level 27
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            val fileProvider = FileProvider.getUriForFile(
                requireContext(),
                requireActivity().packageName + ".provider",
                file
            )

            intent.setDataAndType(fileProvider, type)

        } else {
            intent.setDataAndType(Uri.fromFile(file), type)
        }

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        startActivity(intent)
    }

    override fun onFolderClicked(path: String) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_main_container, FileFragment(path))
        transaction.addToBackStack(null)
        transaction.commit()

    }

    override fun onLongClicked(file: File, position: Int) {
        val dialog = AlertDialog.Builder(requireContext()).create()

        val dialogDeleteItemBinding = DialogDeleteItemBinding.inflate(layoutInflater)
        dialog.setView(dialogDeleteItemBinding.root)

        dialog.show()

        dialogDeleteItemBinding.btnCancel.setOnClickListener { dialog.dismiss() }
        dialogDeleteItemBinding.btnYes.setOnClickListener {

            if (file.exists()) {
                if (file.deleteRecursively()) {
                    myAdapter.removeFile(file, position)
                }
            }

            dialog.dismiss()
        }

    }
}