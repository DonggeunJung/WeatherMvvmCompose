package com.example.weathermvvmcompose.domain

import android.util.Log
import com.example.weathermvvmcompose.data.state.State
import com.example.weathermvvmcompose.data.state.StateApi
import com.example.weathermvvmcompose.data.state.StateDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

class StateRepository @Inject constructor(private var api: StateApi, private val db: StateDatabase) {

    suspend fun reqStates(): List<State>? {
        val job = CoroutineScope(Dispatchers.IO).async {
            // Search State list in SQLite
            var states = db.stateDao().getAll()
            if(states.isNullOrEmpty()) {
                try {
                    // Request State list to server when SQLite is empty
                    states = api.states()
                    // Save state list in SQLite
                    states.forEach { db.stateDao().insert(it) }
                } catch (e: Exception) {
                    Log.d("Tag", "${e.message}")
                }
            }
            states
        }
        return job.await()
    }

}