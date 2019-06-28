/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.sunflower

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.google.samples.apps.sunflower.adapters.MY_GARDEN_PAGE_INDEX
import com.google.samples.apps.sunflower.adapters.PLANT_LIST_PAGE_INDEX
import com.google.samples.apps.sunflower.adapters.SunflowerPagerAdapter
import com.google.samples.apps.sunflower.databinding.FragmentViewPagerBinding

class ViewPagerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        val viewPager = binding.viewPager

        viewPager.adapter = SunflowerPagerAdapter(this)

        // Temporary workaround to programmatically set the menu visibility when changing tabs.
        // See https://issuetracker.google.com/issues/124183800 for more details.
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    MY_GARDEN_PAGE_INDEX -> setAllChildFragmentMenuVisibility(false)
                    PLANT_LIST_PAGE_INDEX -> setAllChildFragmentMenuVisibility(true)
                    else -> Unit
                }
            }
        })

        TabLayoutMediator(binding.tabs, viewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        return binding.root
    }

    private fun getTabTitle(position: Int): CharSequence? {
        return when (position) {
            MY_GARDEN_PAGE_INDEX -> getString(R.string.my_garden_title)
            PLANT_LIST_PAGE_INDEX -> getString(R.string.plant_list_title)
            else -> null
        }
    }

    private fun setAllChildFragmentMenuVisibility(isVisible: Boolean) {
        childFragmentManager.fragments.forEach { fragment ->
            fragment.setMenuVisibility(isVisible)
        }
    }
}