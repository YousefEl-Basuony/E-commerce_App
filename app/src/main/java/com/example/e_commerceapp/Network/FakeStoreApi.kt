package com.example.minicommerce.data.remote

import com.example.minicommerce.model.Product
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface FakeStoreApi {
    @GET("products")
    suspend fun getAllProducts(): List<Product>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Product

    companion object {
         private const val BASE_URL = "https://fakestoreapi.com/"

        fun create(): FakeStoreApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FakeStoreApi::class.java)
        }
    }
}