package com.attri.swissarmy.feature.dictionary.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Free Dictionary API Response Models
data class FreeDictionaryResponse(
    val word: String?,
    val phonetic: String?,
    val phonetics: List<Phonetic>?,
    val meanings: List<Meaning>?,
    val sourceUrls: List<String>?
)

data class Phonetic(
    val text: String?,
    val audio: String?
)

data class Meaning(
    val partOfSpeech: String?,
    val definitions: List<Definition>?,
    val synonyms: List<String>?,
    val antonyms: List<String>?
)

data class Definition(
    val definition: String?,
    val example: String?,
    val synonyms: List<String>?,
    val antonyms: List<String>?
)

// Datamuse API Response Model
data class DatamuseWord(
    val word: String?,
    val score: Int?
)

// MyMemory Translation API Response Model
data class MyMemoryResponse(
    val responseData: TranslationData?,
    val responseStatus: Int?
)

data class TranslationData(
    val translatedText: String?,
    val match: Double?
)

// API Services
interface FreeDictionaryApiService {
    @GET("api/v2/entries/en/{word}")
    suspend fun getDefinition(@Path("word") word: String): List<FreeDictionaryResponse>
}

interface DatamuseApiService {
    @GET("words")
    suspend fun getSynonyms(@Query("rel_syn") word: String): List<DatamuseWord>

    @GET("words")
    suspend fun getAntonyms(@Query("rel_ant") word: String): List<DatamuseWord>
}

interface MyMemoryApiService {
    @GET("get")
    suspend fun translate(
        @Query("q") text: String,
        @Query("langpair") langPair: String // e.g., "en|hi" or "hi|en"
    ): MyMemoryResponse
}
