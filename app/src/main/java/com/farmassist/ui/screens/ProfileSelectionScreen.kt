package com.farmassist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmassist.util.SessionManager

@Composable
fun ProfileSelectionScreen(
    sessionManager: SessionManager,
    onGuestSelected: () -> Unit,
    onProfileSelected: () -> Unit,
    onCreateProfile: () -> Unit
) {
    val isRegistered = sessionManager.isRegistered()
    val userName = sessionManager.getUserName()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to Farm Assist", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Who is using the app today?", color = MaterialTheme.colorScheme.onSurfaceVariant)
        
        Spacer(modifier = Modifier.height(48.dp))

        if (isRegistered) {
            Button(
                onClick = onProfileSelected,
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text(text = "Login as " + userName + " (Farmer)", fontSize = 18.sp)
            }
        } else {
            Button(
                onClick = onCreateProfile,
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Create Farmer Profile", fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("OR", color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onGuestSelected,
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Text("Browse as Guest", fontSize = 18.sp)
        }
    }
}
