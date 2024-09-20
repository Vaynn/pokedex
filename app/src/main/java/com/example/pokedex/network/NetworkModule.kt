package com.example.pokedex.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    //Base API URL
    @Provides
    fun provideBaseUrl() = "https://pokeapi.co/api/v2/"

    //Provide Retrofit Instance
    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String):Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //Provide PokeApiService Instance
    @Provides
    @Singleton
    fun providePokeApiService(retrofit: Retrofit): PokeApiService{
        return retrofit.create(PokeApiService::class.java)
    }
}