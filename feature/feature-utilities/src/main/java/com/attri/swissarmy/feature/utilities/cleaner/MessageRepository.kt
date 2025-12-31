package com.attri.swissarmy.feature.utilities.cleaner

import android.content.Context
import android.net.Uri
import android.provider.Telephony
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

data class MessageGroup(
    val sender: String,
    val count: Int,
    val lastMessage: String,
    val isOtp: Boolean
)

@Singleton
class MessageRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun analyzeMessages(): MessageAnalysis = withContext(Dispatchers.IO) {
        val messages = mutableListOf<MessageGroup>()
        var totalJunk = 0
        var totalMessages = 0
        
        try {
            val cursor = context.contentResolver.query(
                Telephony.Sms.CONTENT_URI,
                arrayOf(Telephony.Sms.ADDRESS, Telephony.Sms.BODY, Telephony.Sms.DATE),
                null,
                null,
                Telephony.Sms.DATE + " DESC"
            )
            
            cursor?.use {
                val addressIdx = it.getColumnIndex(Telephony.Sms.ADDRESS)
                val bodyIdx = it.getColumnIndex(Telephony.Sms.BODY)
                
                // Temporary map to group by sender
                val senderMap = mutableMapOf<String, MutableList<String>>()
                
                while (it.moveToNext()) {
                    totalMessages++
                    val address = it.getString(addressIdx) ?: "Unknown"
                    val body = it.getString(bodyIdx) ?: ""
                    
                    senderMap.putIfAbsent(address, mutableListOf())
                    senderMap[address]?.add(body)
                }
                
                // simple heuristics for "Analysis"
                senderMap.forEach { (sender, bodies) ->
                    val lastBody = bodies.firstOrNull() ?: ""
                    if (isJunkSender(sender, lastBody)) {
                        totalJunk += bodies.size
                        messages.add(
                            MessageGroup(
                                sender = sender,
                                count = bodies.size,
                                lastMessage = lastBody,
                                isOtp = lastBody.contains("OTP", ignoreCase = true)
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return@withContext MessageAnalysis(
            totalMessages = totalMessages,
            junkMessages = totalJunk,
            junkGroups = messages.sortedByDescending { it.count }
        )
    }

    suspend fun deleteJunkMessages(): Int = withContext(Dispatchers.IO) {
        var deletedTotal = 0
        try {
            val analysis = analyzeMessages()
            val junkSenders = analysis.junkGroups.map { it.sender }.filter { it.isNotBlank() }
            
            if (junkSenders.isEmpty()) return@withContext 0

            // Collect all unique thread IDs first
            val threadIds = mutableSetOf<Long>()
            
            junkSenders.forEach { sender ->
                // Query SMS for thread IDs
                context.contentResolver.query(
                    Telephony.Sms.CONTENT_URI,
                    arrayOf(Telephony.Sms.THREAD_ID),
                    "${Telephony.Sms.ADDRESS} LIKE ?",
                    arrayOf("%$sender%"),
                    null
                )?.use { cursor ->
                    val idx = cursor.getColumnIndex(Telephony.Sms.THREAD_ID)
                    while (cursor.moveToNext()) {
                        val tid = cursor.getLong(idx)
                        if (tid > 0) threadIds.add(tid)
                    }
                }
            }

            // Delete using the canonical MmsSms threads URI - this is the most reliable
            // This URI works for SMS, MMS, and RCS combined
            val THREADS_URI = Uri.parse("content://mms-sms/conversations?simple=true")
            
            threadIds.forEach { threadId ->
                try {
                    // Method 1: Delete from combined MMS-SMS provider
                    val deleted1 = context.contentResolver.delete(
                        Uri.parse("content://mms-sms/conversations/$threadId"),
                        null, null
                    )
                    if (deleted1 > 0) deletedTotal += deleted1
                } catch (e: Exception) { }

                try {
                    // Method 2: Delete from SMS conversations
                    val deleted2 = context.contentResolver.delete(
                        Uri.parse("content://sms/conversations/$threadId"),
                        null, null
                    )
                    if (deleted2 > 0) deletedTotal += deleted2
                } catch (e: Exception) { }
            }
            
            // Fallback: Direct SMS deletion by address
            if (deletedTotal == 0) {
                junkSenders.forEach { sender ->
                    try {
                        val deleted = context.contentResolver.delete(
                            Telephony.Sms.CONTENT_URI,
                            "${Telephony.Sms.ADDRESS} LIKE ?",
                            arrayOf("%$sender%")
                        )
                        deletedTotal += deleted
                    } catch (e: Exception) { }
                }
            }
            
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext -1
        }
        return@withContext deletedTotal
    }

    private fun isJunkSender(address: String, lastBody: String): Boolean {
        val isLikelyOtp = lastBody.contains("OTP", ignoreCase = true) || 
                         lastBody.contains("code", ignoreCase = true) || 
                         lastBody.contains("verification", ignoreCase = true)
                         
        val isBusinessSender = address.length <= 11 && !address.startsWith("+") && 
                             (address.any { it.isLetter() } || address.length <= 8)
                             
        return isLikelyOtp || isBusinessSender
    }
}

data class MessageAnalysis(
    val totalMessages: Int,
    val junkMessages: Int,
    val junkGroups: List<MessageGroup>
)
