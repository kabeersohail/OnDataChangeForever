package com.example.ondatachangeforever.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.work.*
import com.example.ondatachangeforever.databinding.FragmentLaunchBinding
import com.example.ondatachangeforever.services.ListenToFirebaseService
import com.example.ondatachangeforever.workers.WriteToFirebaseForEveryFourHours
import java.util.concurrent.TimeUnit

class LaunchFragment : Fragment() {

    private lateinit var binding: FragmentLaunchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLaunchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        binding.write.setOnClickListener {

            val writeToFirebaseForEveryFourHoursWorkRequest: PeriodicWorkRequest =
                PeriodicWorkRequestBuilder<WriteToFirebaseForEveryFourHours>(4,
                    TimeUnit.HOURS)
                    .setConstraints(constraints)
                    .build()

            WorkManager.getInstance(requireContext())
                .enqueueUniquePeriodicWork("ThisIsUnique",
                    ExistingPeriodicWorkPolicy.KEEP,
                    writeToFirebaseForEveryFourHoursWorkRequest)

            Toast.makeText(requireContext(), "Enqueued", Toast.LENGTH_SHORT).show()

        }

        binding.listen.setOnClickListener {
            requireActivity().startService(Intent(requireContext(), ListenToFirebaseService::class.java))
        }

    }


}