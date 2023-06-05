package com.example.learnwithpierre.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

//it's our Table in the database with primaryKey and Column Info
@Entity
data class Data(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "recto") val recto: String,
    @ColumnInfo(name = "verso") val verso: String,
    @ColumnInfo(name = "isRecto") val isRecto : Boolean,
    @ColumnInfo(name = "category") val category : String,
    @ColumnInfo(name = "score") val score : Int,
    @ColumnInfo(name = "dateModification") val dateModification : Date,
)
