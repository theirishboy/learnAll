package com.example.learnwithpierre.dao

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//it's our Table in the database with primaryKey and Column Info
@Entity

data class Data(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "recto") val recto: String,
    @ColumnInfo(name = "verso") var verso: String,
    @ColumnInfo(name = "isRecto") val isRecto: Boolean,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "score") val score: Int,
    @ColumnInfo(name = "dateModification")
    val dateModification: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(name = "dateTraining")
    val dateTraining: LocalDateTime = LocalDateTime.now(),

    )

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}