package com.attri.swissarmy.feature.dictionary.di

import com.attri.swissarmy.feature.dictionary.data.DatamuseApiService
import com.attri.swissarmy.feature.dictionary.data.FreeDictionaryApiService
import com.attri.swissarmy.feature.dictionary.data.MyMemoryApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DictionaryNetworkModule {

    @Provides
    @Singleton
    @Named("FreeDictionary")
    fun provideFreeDictionaryRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.dictionaryapi.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("Datamuse")
    fun provideDatamuseRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.datamuse.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("MyMemory")
    fun provideMyMemoryRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.mymemory.translated.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideFreeDictionaryApiService(@Named("FreeDictionary") retrofit: Retrofit): FreeDictionaryApiService {
        return retrofit.create(FreeDictionaryApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatamuseApiService(@Named("Datamuse") retrofit: Retrofit): DatamuseApiService {
        return retrofit.create(DatamuseApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMyMemoryApiService(@Named("MyMemory") retrofit: Retrofit): MyMemoryApiService {
        return retrofit.create(MyMemoryApiService::class.java)
    }
}
