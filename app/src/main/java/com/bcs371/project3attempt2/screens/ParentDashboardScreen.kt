package com.bcs371.project3attempt2.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bcs371.project3attempt2.game.GameStateManager
import com.bcs371.project3attempt2.data.Progress
import com.bcs371.project3attempt2.viewmodel.ProgressViewModel
import com.bcs371.project3attempt2.viewmodel.UserViewModel

@Composable
fun ParentDashboardScreen(
    onClose: () -> Unit,
    progressViewModel: ProgressViewModel,
    userId: Int,
    viewModel: UserViewModel
) {
    val progress by progressViewModel.progress.collectAsState()
    
    val easyAttempts = progress.filter { !it.isHardMode }
                              .map { it.attempts }
                              .take(8)
                              .reversed()
    val hardAttempts = progress.filter { it.isHardMode }
                              .map { it.attempts }
                              .take(8)
                              .reversed()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Parent Dashboard",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            //Easy Mode Column
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Easy Mode",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Most Recent Attempts",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                if (easyAttempts.isEmpty()) {
                    Text("No attempts recorded")
                } else {
                    easyAttempts.forEachIndexed { index, attempts ->
                        Text(
                            text = "Attempt ${index + 1}: ${attempts} ${if (attempts == 1) "try" else "tries"}",
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }

            //Hard Mode Column
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Hard Mode",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Most Recent Attempts",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                if (hardAttempts.isEmpty()) {
                    Text("No attempts recorded")
                } else {
                    hardAttempts.forEachIndexed { index, attempts ->
                        Text(
                            text = "Attempt ${index + 1}: ${attempts} ${if (attempts == 1) "try" else "tries"}",
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                viewModel.clearCurrentUser()
                onClose()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Logout")
        }
    }
} 