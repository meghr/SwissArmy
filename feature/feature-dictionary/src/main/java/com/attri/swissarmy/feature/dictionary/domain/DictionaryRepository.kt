package com.attri.swissarmy.feature.dictionary.domain

import javax.inject.Inject
import javax.inject.Singleton

data class DictionaryEntry(
    val english: String,
    val hindi: String,
    val type: String, // Noun, Verb, etc.
    val synonyms: List<String> = emptyList(),
    val antonyms: List<String> = emptyList(),
    val example: String? = null
)

@Singleton
class DictionaryRepository @Inject constructor() {

    // A small sample database for offline demonstration
    private val database = listOf(
        DictionaryEntry(
            english = "Love",
            hindi = "प्रेम (Prem)",
            type = "Noun",
            synonyms = listOf("Affection", "Devotion", "स्नेह (Sneh)"),
            antonyms = listOf("Hate", "घृणा (Ghrina)"),
            example = "Love conquers all."
        ),
        DictionaryEntry(
            english = "Peace",
            hindi = "शांति (Shanti)",
            type = "Noun",
            synonyms = listOf("Calm", "Harmony", "सुकून (Sukun)"),
            antonyms = listOf("War", "Conflict", "अशांति (Ashanti)"),
            example = "We pray for world peace."
        ),
        DictionaryEntry(
            english = "Work",
            hindi = "काम (Kaam)",
            type = "Verb/Noun",
            synonyms = listOf("Task", "Labor", "कार्य (Karya)"),
            antonyms = listOf("Rest", "आराम (Aaram)"),
            example = "Hard work pays off."
        ),
        DictionaryEntry(
            english = "Success",
            hindi = "सफलता (Safalta)",
            type = "Noun",
            synonyms = listOf("Victory", "Achievement", "जीत (Jeet)"),
            antonyms = listOf("Failure", "विफलता (Vifalta)"),
            example = "Success is a journey, not a destination."
        ),
        DictionaryEntry(
            english = "Knowledge",
            hindi = "ज्ञान (Gyaan)",
            type = "Noun",
            synonyms = listOf("Wisdom", "Learning", "विद्या (Vidya)"),
            antonyms = listOf("Ignorance", "अज्ञान (Agyaan)"),
            example = "Knowledge is power."
        ),
        DictionaryEntry(
            english = "Friend",
            hindi = "मित्र (Mitra)",
            type = "Noun",
            synonyms = listOf("Companion", "Buddy", "दोस्त (Dost)"),
            antonyms = listOf("Enemy", "शत्रु (Shatru)"),
            example = "A friend in need is a friend indeed."
        ),
         DictionaryEntry(
            english = "Beautiful",
            hindi = "सुंदर (Sundar)",
            type = "Adjective",
            synonyms = listOf("Pretty", "Lovely", "खूबसूरत (Khoobsurat)"),
            antonyms = listOf("Ugly", "बदसूरत (Badsurat)"),
            example = "Nature is beautiful."
        )
    )

    fun search(query: String): List<DictionaryEntry> {
        val q = query.trim()
        if (q.isBlank()) return emptyList()

        return database.filter { 
            it.english.contains(q, ignoreCase = true) || 
            it.hindi.contains(q, ignoreCase = true) 
        }
    }
}
