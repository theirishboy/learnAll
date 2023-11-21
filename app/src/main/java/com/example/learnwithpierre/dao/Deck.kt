package com.example.learnwithpierre.dao

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "decks",
    foreignKeys = [ForeignKey(entity = User::class,
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("userId"),
        onDelete = ForeignKey.CASCADE
    )])
data class Deck(
    @PrimaryKey(autoGenerate = true) val deckId: Long = 0,
    val userId: Long,
    val name: String,
    val description: String?,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)