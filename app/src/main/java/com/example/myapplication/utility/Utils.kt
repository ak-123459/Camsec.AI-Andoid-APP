package com.example.myapplication.utility

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import androidx.core.content.edit
import com.example.myapplication.local.db.DatabaseProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


object SecurePrefsManager {

    private const val PREF_NAME = "TOKEN"
    private const val KEY_TOKEN = "token"

    private fun getEncryptedPrefs(context: Context): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            PREF_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveToken(context: Context, token: String) {
        val prefs = getEncryptedPrefs(context)
        prefs.edit { putString(KEY_TOKEN, token) }
    }


    fun saveEmail(context: Context, email: String) {
        val prefs = getEncryptedPrefs(context)
        prefs.edit { putString("email", email) }
    }

    fun saveFullName(context: Context, name: String) {
        val prefs = getEncryptedPrefs(context)
        prefs.edit { putString("fullName", name) }
    }

    fun getFullName(context: Context) : String?{
        val prefs = getEncryptedPrefs(context)
        return prefs.getString("fullName", "No Name")
    }

    fun getEmail(context: Context): String? {
        val prefs = getEncryptedPrefs(context)
        return prefs.getString("email", null)
    }


    fun getToken(context: Context): String? {
        val prefs = getEncryptedPrefs(context)
        return prefs.getString(KEY_TOKEN, null)
    }

    fun removeToken(context: Context) {
        val prefs = getEncryptedPrefs(context)
        prefs.edit { remove(KEY_TOKEN) }
    }

    fun clearAll(context: Context) {
        val prefs = getEncryptedPrefs(context)
        prefs.edit { clear() }
    }
}


// This function can be part of your AuthenticationViewModel or a data manager class
fun logoutUser(context: Context) {
    // 1. Get a reference to the database
    val db = DatabaseProvider.getDatabase(context)
    val secSF = SecurePrefsManager

    secSF.clearAll(context)
    // 2. Launch a coroutine on the I/O dispatcher
    CoroutineScope(Dispatchers.IO).launch {
        try {
            // 3. Call clearAllTables() to delete all data
            db.clearAllTables()
        } catch (e: Exception) {
            // Handle any exceptions during the clear process
            e.printStackTrace()
        }
    }
}

