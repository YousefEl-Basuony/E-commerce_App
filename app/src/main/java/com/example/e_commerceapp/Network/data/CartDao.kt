package com.example.minicommerce.data.local

import android.content.Context
import androidx.room.*
import com.example.minicommerce.model.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_table")
    fun getCartItems(): Flow<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(item: CartItem)

    @Query("DELETE FROM cart_table WHERE id = :id")
    suspend fun removeFromCart(id: Int)

    @Query("UPDATE cart_table SET quantity = :quantity WHERE id = :id")
    suspend fun updateQuantity(id: Int, quantity: Int)

     @Query("SELECT * FROM cart_table WHERE id = :id LIMIT 1")
    suspend fun getCartItemById(id: Int): CartItem?
}

@Database(entities = [CartItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ecommerce_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}