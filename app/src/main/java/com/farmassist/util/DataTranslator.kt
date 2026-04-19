package com.farmassist.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

object DataTranslator {
    @Composable
    fun translate(key: String?): String {
        if (key.isNullOrEmpty()) return ""
        val context = LocalContext.current
        
        // Clean up the key to match valid XML resource name rules:
        // 1. Remove common bullet points or symbols at start
        // 2. Convert to lowercase
        // 3. Replace all non-alphanumeric chars with underscore
        // 4. Collapse multiple underscores and trim
        val sanitizedKey = key.trim()
            .removePrefix("•")
            .trim()
            .lowercase()
            .replace(Regex("[^a-z0-9]"), "_")
            .replace(Regex("_+"), "_")
            .trim('_')
            
        val resId = context.resources.getIdentifier("data_$sanitizedKey", "string", context.packageName)
        
        return if (resId != 0) {
            stringResource(resId)
        } else {
            key // Return original text if no translation is found
        }
    }
}
