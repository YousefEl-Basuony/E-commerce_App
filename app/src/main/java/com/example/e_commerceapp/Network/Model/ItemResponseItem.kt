package com.example.minicommerce.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

 data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating
)

data class Rating(
    val rate: Double,
    val count: Int
)

 @Entity(tableName = "cart_table")
data class CartItem(
    @PrimaryKey val id: Int,
    val title: String,
    val price: Double,
    val image: String,
    val quantity: Int = 1
)