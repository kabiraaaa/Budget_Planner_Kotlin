package com.sample.budgetplanner.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.sample.budgetplanner.OnBoardingViewPagerAnim
import com.sample.budgetplanner.R
import com.sample.budgetplanner.databinding.FragmentIntroBinding
import com.sample.budgetplanner.fragments.onBoardingSlides.OnBoardingSlideOneFragment
import com.sample.budgetplanner.fragments.onBoardingSlides.OnBoardingSlideThreeFragment
import com.sample.budgetplanner.fragments.onBoardingSlides.OnBoardingSlideTwoFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class IntroFragment : Fragment(R.layout.fragment_intro) {

    private lateinit var binding: FragmentIntroBinding
    private val TAG = "IntroFragment"
    private var currentPage = 0
    private val delay: Long = 1500
    private var job: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIntroBinding.bind(view)

        binding.btnGetStarted.setOnClickListener {
            findNavController().navigate(R.id.introLoginBottomSheet)
        }

        setUpViewPager()
        autoSlideViewPager()
    }

    private fun setUpViewPager() {
        val pagerAdapter = OnBoardingPagerAdapter(requireActivity())
        binding.viewPager.apply {
            adapter = pagerAdapter
            setPageTransformer(OnBoardingViewPagerAnim())

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    currentPage = position
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    if (state == ViewPager2.SCROLL_STATE_IDLE) {
                        if (currentPage == pagerAdapter.getItemCount() - 1) {
                            binding.viewPager.setCurrentItem(1, false)
                        } else if (currentPage == 0) {
                            binding.viewPager.setCurrentItem(pagerAdapter.getItemCount() - 2, false)
                        }
                    }
                }
            })
        }

        binding.viewPager.setCurrentItem(1, false)
    }

    private fun autoSlideViewPager() {
        job = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                delay(delay)
                binding.viewPager.setCurrentItem(++currentPage, true)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job?.cancel()
    }

    private inner class OnBoardingPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 5

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> OnBoardingSlideOneFragment()
                1 -> OnBoardingSlideTwoFragment()
                2 -> OnBoardingSlideThreeFragment()
                3 -> OnBoardingSlideOneFragment()
                4 -> OnBoardingSlideTwoFragment()
                else -> throw IllegalStateException("Invalid position")
            }
        }
    }
}