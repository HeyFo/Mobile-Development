package com.bangkit.heyfo.data.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
class Food(
    @PrimaryKey(autoGenerate = false)
    var name: String = "",
    var imageUrl: String? = null,
    var uuid: String? = null
): Parcelable