package com.farmassist.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmassist.R
import com.farmassist.ui.theme.*
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
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🌾", fontSize = 60.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(stringResource(R.string.welcome), fontSize = 26.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary)
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.who_is_using), color = FarmTextSecondary, fontSize = 15.sp)

        Spacer(modifier = Modifier.height(48.dp))

        if (isRegistered) {
            Button(
                onClick = onProfileSelected,
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FarmGreenPrimary)
            ) {
                Text(
                    text = "${stringResource(R.string.login_as)} $userName (${stringResource(R.string.farmer_label)})",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Button(
                onClick = onCreateProfile,
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FarmGreenPrimary)
            ) {
                Text(stringResource(R.string.create_profile), fontSize = 17.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(stringResource(R.string.or), color = FarmTextSecondary, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onGuestSelected,
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = FarmGreenPrimary)
        ) {
            Text(stringResource(R.string.browse_as_guest), fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
    }
}
