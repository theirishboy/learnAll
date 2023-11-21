package com.example.learnwithpierre.dao

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)