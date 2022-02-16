package com.example.myapplication

import android.content.Context
import android.os.*
import android.provider.Telephony.Mms.Part.FILENAME
import android.view.Gravity
import android.view.KeyEvent
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.itemshoppinglist.*
import java.io.*
import com.example.myapplication.ShoppingList as ShoppingList
import android.widget.CompoundButton





class MainActivity() : AppCompatActivity() {

    private lateinit var shoppingListAdapter: ShoppingListAdapter



    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        shoppingListAdapter = ShoppingListAdapter(mutableListOf())

        //val cbdone = findViewById<CheckBox>(R.id.CBDone)


        RVShoppingList.adapter = shoppingListAdapter
        RVShoppingList.layoutManager = LinearLayoutManager(this)

        loadFile()

        ETShoppingListTitle.setOnKeyListener { v, keyCode, event ->
            when {
                ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {
                    BTNAddItem.performClick()
                    return@setOnKeyListener true
                }
                else -> false
            }
        }

        //cbdone.setOnCheckedChangeListener { _, _ ->  saveFile()}
/*
        if (CBDone.isClickable){
            CBDone.setOnClickListener {
                try {
                    saveFile()
                } catch (e:Exception){
                    e.printStackTrace()
                    println("Failed saving")
                }
            }
        }
*/
        TVItem.setOnClickListener{
            saveFile()
        }

        BTNAddItem.setOnClickListener {
            val listTitle = ETShoppingListTitle.text?.toString()!!
            if (listTitle != null) {
                if (listTitle.isNotEmpty()) {
                    val list = listTitle.let { it1 -> ShoppingList(it1) }
                    if (list != null) {
                        shoppingListAdapter.addList(list)
                    }
                    ETShoppingListTitle.text.clear()
                }
            }
            saveFile()
        }

        BTNDeleteItem.setOnClickListener {
            shoppingListAdapter.deleteDoneList()
            saveFile()
        }

    }

    private fun saveFile() {
        try{
            var counter = 0
            val file = openFileOutput("SaveFile.txt", Context.MODE_PRIVATE)
            val outStream = ObjectOutputStream(file)
            while (counter < shoppingListAdapter.itemCount) {
                outStream.writeChars(shoppingListAdapter.getShoppingList(counter).title+"\n")
                println("saving :"+shoppingListAdapter.getShoppingList(counter).title)
                outStream.writeBoolean(shoppingListAdapter.getShoppingList(counter).isChecked)
                println("saving :"+shoppingListAdapter.getShoppingList(counter).isChecked)
                counter++
            }
            outStream.close()
            file.close()

        }catch(e:Exception){
            e.printStackTrace()
            println("Failed saving")
        }
    }

    private fun loadFile() {
        try {
            println("Loading: ")
            val file = openFileInput("SaveFile.txt")
            val inStream = ObjectInputStream(file)
            var name : String = "Food"
            var bool : Boolean = false
            try {
                while ({ name = inStream.readLine(); name }() != null && { bool = inStream.readBoolean(); bool }() != null) {
                    shoppingListAdapter.addList(ShoppingList(name, bool))
                    println("Loaded : "+shoppingListAdapter.getShoppingList(shoppingListAdapter.itemCount-1))
                }
            }catch(e: Exception){
                println("Loading exception -> ")
                e.printStackTrace()
            }finally {
                inStream.close()
                file.close()
            }
            inStream.close()
            file.close()
        } catch (e: Exception) {
            e.printStackTrace()
            println("Could not load file")
        }
    }

}
