package com.sample.budgetplanner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.sample.budgetplanner.utils.DataStoreHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        if (savedInstanceState == null) {
            // Only run navigation logic if it's the first time onCreate is called
            CoroutineScope(Dispatchers.Main).launch {
                decideInitialDestination()
            }
        }
    }

    private suspend fun decideInitialDestination() {
        val isOnboardingComplete = DataStoreHelper.getOnBoardingStatus(this@MainActivity).first()
        val userName = DataStoreHelper.getUserName(this@MainActivity).first()
        val userAge = DataStoreHelper.getUserAge(this@MainActivity).first()
        val userGender = DataStoreHelper.getUserGender(this@MainActivity).first()
        val userGoals = DataStoreHelper.getUserGoals(this@MainActivity).first()
        val monthlySpend = DataStoreHelper.getMonthlySpend(this@MainActivity).first()
        val monthlySalary = DataStoreHelper.getMonthlySalary(this@MainActivity).first()

        when {
            !isOnboardingComplete -> {
                // Navigate to OnboardingFragment if onboarding is incomplete
                navController.navigate(R.id.introFragment)
            }
            userName.isEmpty() || userAge.isEmpty() || userGender.isEmpty() -> {
                navController.navigate(R.id.introIncomeFragment)
            }
            userGoals.isEmpty() -> {
                navController.navigate(R.id.introInvestmentFragment)
            }
            monthlySpend == 0.0 || monthlySalary == 0.0 -> {
                navController.navigate(R.id.introBudgetFragment)
            }
            else -> {
                // If all data is available, navigate to HomeFragment
                navController.navigate(R.id.homeFragment)
            }
        }
    }
}