package com.example.ondatachangeforever.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.Exception

class WriteToFirebaseForEveryFourHours(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {

        println("SOHAIL Work scheduled")

        CoroutineScope(Dispatchers.IO).launch {
            getCurrentValueOfTheNode().collectLatest {
                println("SOHAIL $it")
                FirebaseDatabase.getInstance().getReference("WritingNode").child("key")
                    .setValue(it + 1)
                this.cancel()
            }
        }

        return Result.success()
    }


    private fun getCurrentValueOfTheNode(): Flow<Int> = callbackFlow {

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val currentValue: Int =
                    try {
                        (snapshot.value as Map<*, *>).values.first().toString().toInt()
                    } catch (e: Exception) {
                        0
                    }

                trySend(currentValue)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("SOHAIL", "$error")
            }
        }

        FirebaseDatabase.getInstance().getReference("WritingNode")
            .addValueEventListener(valueEventListener)

        awaitClose {
            FirebaseDatabase.getInstance().getReference("WritingNode")
                .removeEventListener(valueEventListener)
        }
    }


}