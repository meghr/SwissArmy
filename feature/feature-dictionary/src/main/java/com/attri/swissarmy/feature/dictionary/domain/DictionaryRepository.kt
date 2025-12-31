package com.attri.swissarmy.feature.dictionary.domain

import com.attri.swissarmy.feature.dictionary.data.DatamuseApiService
import com.attri.swissarmy.feature.dictionary.data.FreeDictionaryApiService
import com.attri.swissarmy.feature.dictionary.data.MyMemoryApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

data class DictionaryEntry(
    val english: String,
    val hindi: String,
    val type: String,
    val phonetic: String? = null,
    val definition: String? = null,
    val synonyms: List<String> = emptyList(),
    val antonyms: List<String> = emptyList(),
    val example: String? = null
)

@Singleton
class DictionaryRepository @Inject constructor(
    private val freeDictionaryApi: FreeDictionaryApiService,
    private val datamuseApi: DatamuseApiService,
    private val myMemoryApi: MyMemoryApiService
) {

    suspend fun search(query: String): List<DictionaryEntry> = withContext(Dispatchers.IO) {
        val q = query.trim()
        if (q.isBlank()) return@withContext emptyList()

        val results = mutableListOf<DictionaryEntry>()

        // Check if input is Hindi (contains Devanagari characters)
        val isHindi = q.any { it.code in 0x0900..0x097F }

        if (isHindi) {
            // Hindi to English translation
            try {
                val translation = myMemoryApi.translate(q, "hi|en")
                val englishWord = translation.responseData?.translatedText ?: ""
                
                if (englishWord.isNotBlank()) {
                    // Now get definition for the English word
                    val entry = getEnglishDefinition(englishWord, q)
                    if (entry != null) results.add(entry)
                }
            } catch (e: Exception) {
                // Translation failed, return empty
            }
        } else {
            // English word - get definition and translate to Hindi
            val entry = getEnglishDefinition(q, null)
            if (entry != null) results.add(entry)
        }

        return@withContext results
    }

    private suspend fun getEnglishDefinition(englishWord: String, originalHindi: String?): DictionaryEntry? {
        return withContext(Dispatchers.IO) {
            try {
                // Parallel API calls
                val definitionDeferred = async { 
                    try { freeDictionaryApi.getDefinition(englishWord) } catch (e: Exception) { null }
                }
                val synonymsDeferred = async { 
                    try { datamuseApi.getSynonyms(englishWord) } catch (e: Exception) { emptyList() }
                }
                val antonymsDeferred = async { 
                    try { datamuseApi.getAntonyms(englishWord) } catch (e: Exception) { emptyList() }
                }
                val hindiDeferred = async {
                    if (originalHindi != null) {
                        originalHindi
                    } else {
                        try {
                            val result = myMemoryApi.translate(englishWord, "en|hi")
                            result.responseData?.translatedText ?: ""
                        } catch (e: Exception) { "" }
                    }
                }

                val definitions = definitionDeferred.await()
                val synonyms = synonymsDeferred.await()
                val antonyms = antonymsDeferred.await()
                val hindiTranslation = hindiDeferred.await()

                if (definitions.isNullOrEmpty()) {
                    // No definition found, but we might have translation
                    if (hindiTranslation?.isNotBlank() == true) {
                        return@withContext DictionaryEntry(
                            english = englishWord.replaceFirstChar { it.uppercase() },
                            hindi = hindiTranslation,
                            type = "Word",
                            synonyms = synonyms?.take(5)?.mapNotNull { it.word } ?: emptyList(),
                            antonyms = antonyms?.take(5)?.mapNotNull { it.word } ?: emptyList()
                        )
                    }
                    return@withContext null
                }

                val firstResult = definitions.first()
                val firstMeaning = firstResult.meanings?.firstOrNull()
                val firstDefinition = firstMeaning?.definitions?.firstOrNull()

                // Combine synonyms from Free Dictionary API and Datamuse
                val allSynonyms = mutableListOf<String>()
                firstMeaning?.synonyms?.take(3)?.let { allSynonyms.addAll(it) }
                firstDefinition?.synonyms?.take(2)?.let { allSynonyms.addAll(it) }
                synonyms?.take(5)?.mapNotNull { it.word }?.let { allSynonyms.addAll(it) }

                // Combine antonyms
                val allAntonyms = mutableListOf<String>()
                firstMeaning?.antonyms?.take(3)?.let { allAntonyms.addAll(it) }
                firstDefinition?.antonyms?.take(2)?.let { allAntonyms.addAll(it) }
                antonyms?.take(5)?.mapNotNull { it.word }?.let { allAntonyms.addAll(it) }

                DictionaryEntry(
                    english = firstResult.word?.replaceFirstChar { it.uppercase() } ?: englishWord,
                    hindi = hindiTranslation ?: "",
                    type = firstMeaning?.partOfSpeech?.replaceFirstChar { it.uppercase() } ?: "Word",
                    phonetic = firstResult.phonetic ?: firstResult.phonetics?.firstOrNull()?.text,
                    definition = firstDefinition?.definition,
                    synonyms = allSynonyms.distinct().take(8),
                    antonyms = allAntonyms.distinct().take(8),
                    example = firstDefinition?.example
                )
            } catch (e: Exception) {
                null
            }
        }
    }
}
