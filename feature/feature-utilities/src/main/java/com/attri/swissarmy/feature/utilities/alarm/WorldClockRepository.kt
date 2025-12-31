package com.attri.swissarmy.feature.utilities.alarm

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorldClockRepository @Inject constructor() {

    fun getWorldTimeHierarchy(): Map<String, List<String>> {
        val allZones = ZoneId.getAvailableZoneIds()
        
        // Group by Region (First part of the ID)
        val hierarchy = allZones
            .filter { it.contains("/") } // Filter out short IDs like 'CET' for consistency
            .groupBy { id -> 
                id.split("/").first() 
            }
            .mapValues { entry -> 
                entry.value.sorted()
            }
            
        return hierarchy
    }

    fun getTimeForZone(zoneId: String): String {
        val zone = ZoneId.of(zoneId)
        val now = LocalDateTime.now(zone)
        val formatter = DateTimeFormatter.ofPattern("HH:mm") // 24h format
        return now.format(formatter)
    }
    
    fun getDateForZone(zoneId: String): String {
        val zone = ZoneId.of(zoneId)
        val now = LocalDateTime.now(zone)
        val formatter = DateTimeFormatter.ofPattern("EEE, d MMM")
        return now.format(formatter)
    }
}
