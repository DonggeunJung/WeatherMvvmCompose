package com.example.weathermvvmcompose.data.state

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface StateDao {

    @Insert
    fun insert(state: State)

    @Update
    fun update(state: State)

    @Delete
    fun delete(state: State)

    @Query("SELECT * FROM State") // Get all data from table
    fun getAll(): List<State>

    @Query("DELETE FROM State WHERE name = :name") // Delete data by 'name'
    fun deleteStateByName(name: String)
}