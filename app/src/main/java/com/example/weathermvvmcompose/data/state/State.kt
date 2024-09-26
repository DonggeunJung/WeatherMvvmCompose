package com.example.weathermvvmcompose.data.state

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName="State")
data class State(val name: String, val abbreviation: String) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}