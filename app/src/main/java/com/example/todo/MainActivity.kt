package com.example.todo

import android.os.Bundle
import android.os.FileUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object  : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
//                1. Remove the item from the list
                listOfTasks.removeAt(position)
//                2. Notify the adapter that our data has been updated
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

        loadItems()
//        Look up recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

//        Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

//        Button functionality
        findViewById<Button>(R.id.button).setOnClickListener {
//          1. get the user input
            val userInputtedTask = inputTextField.text.toString()
//          2. add the input to the list of task
            listOfTasks.add(userInputtedTask)

//          Notify the adapter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

//          3.Reset text field
            inputTextField.setText("")

            saveItems()
        }

    }
    //        Save the date the user inputted by writing and reading from a file
//    1.Ge the file we need
    fun getDataFile() : File {
        return File(filesDir, "data.txt")
    }
//    2. Load the items by reading every line from the data file
    fun loadItems() {
        try{
            listOfTasks = org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
//    3. Save items by writing them into the data file
    fun saveItems() {
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

}