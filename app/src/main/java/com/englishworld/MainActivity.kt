package com.englishworld

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.englishworld.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    
    private val fragments = listOf(
        HomeFragment(),
        SearchFragment(),
        LearnFragment(),
        MineFragment()
    )
    
    private val tabTitles = listOf(
        R.string.tab_home,
        R.string.tab_search,
        R.string.tab_learn,
        R.string.tab_mine
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupViewPager()
        setupTabLayout()
    }
    
    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(this, fragments)
        binding.viewPager.adapter = adapter
    }
    
    private fun setupTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setText(tabTitles[position])
        }.attach()
        
        // 设置Tab图标
        binding.tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_home)
        binding.tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_search)
        binding.tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_learn)
        binding.tabLayout.getTabAt(3)?.setIcon(R.drawable.ic_mine)
    }
}
