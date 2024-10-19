package com.sample.budgetplanner.utils

import android.os.Bundle
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

object AnalyticsHelper {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun initGA() {
        firebaseAnalytics = Firebase.analytics
    }

    fun logEvent(eventName: String, eventParameters: Map<String, String?>? = null) {
        val bundle = Bundle()
        if (eventParameters != null) {
            for ((key, value) in eventParameters) {
                bundle.putString(key, value)
            }
        }
        firebaseAnalytics.logEvent(eventName, bundle)
    }
}