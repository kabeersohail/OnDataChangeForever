package com.example.ondatachangeforever.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LaunchViewModel: ViewModel() {

    fun getRootDatabaseReference(): DatabaseReference = FirebaseDatabase.getInstance().getReference("rootNode")

}