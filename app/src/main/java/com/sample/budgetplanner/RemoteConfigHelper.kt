package com.sample.budgetplanner

import android.util.Log
import com.google.firebase.remoteconfig.BuildConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import org.json.JSONObject

object RemoteConfigHelper {
    private val TAG = "RemoteConfigHelper"

    fun initializeRemoteConfig() {
        val currentTime = System.currentTimeMillis()
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(if (BuildConfig.DEBUG) 0 else 0).build()
        remoteConfig.setConfigSettingsAsync(configSettings)
//        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateAdsControlWizard()
                } else {
                    Log.d(TAG, "initializeRemoteConfig: Failed")
                }
            }
    }

    lateinit var adsData: JSONObject
    private fun updateAdsControlWizard() {
        val adsControlWizard = FirebaseRemoteConfig.getInstance().getString("serverid")
        Log.d(TAG, adsControlWizard)
        try {
            adsData = JSONObject(adsControlWizard)
            Log.d(TAG, adsData.toString())
        } catch (e: Exception) {
            Log.e("AdsControlError", e.toString())
        }
    }


}
