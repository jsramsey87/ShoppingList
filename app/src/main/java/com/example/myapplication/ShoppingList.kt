package com.example.myapplication

import java.io.Serializable

data class ShoppingList (
    val title: String,
    var isChecked: Boolean = false
) : Serializable