package com.androminor.mangaapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Varun Singh
 */
@Entity(tableName = "mangas")
data class MangaEntity(
@PrimaryKey val id : String,
    val title:String,
    val subTitle:String,
    val status:String,
    val thumb:String,
    val summary:String,
    val authors:String,
    val genres:String,
    val isNsfw:Boolean,
    val type:String,
    val totalChapters:Int,
    val createdAt:Long,
    val updatedAt:Long,
    val page:Int // which page manga belongs to


)