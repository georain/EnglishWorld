package com.englishworld

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.englishworld.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userData: UserDataManager

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

        userData = UserDataManager(this)

        setupViewPager()
        setupTabLayout()
        setupFragmentListeners()

        // 首次启动弹出隐私政策与用户协议
        binding.root.post {
            if (!userData.isPrivacyAccepted) showPolicyDialog()
        }
    }

    /** 供首页等功能卡片跳转到指定 Tab */
    fun navigateToTab(position: Int) {
        if (position in 0 until fragments.size) {
            binding.viewPager.setCurrentItem(position, true)
        }
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

    private fun setupFragmentListeners() {
        val homeFragment = fragments[0] as HomeFragment
        homeFragment.setOnNavigateListener { position -> navigateToTab(position) }
    }

    /** 首次启动协议弹窗 */
    private fun showPolicyDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("欢迎使用英语单词学习")
            .setMessage(
                "感谢您使用英语单词学习应用！\n\n" +
                    "在使用本应用前，请您仔细阅读并同意《用户协议》和《隐私政策》。\n\n" +
                    "本应用完全免费、纯本地运行，不收集任何个人信息，您的学习数据仅保存在您的设备中。"
            )
            .setNeutralButton("查看用户协议") { _, _ ->
                startActivity(
                    Intent(this, PolicyActivity::class.java)
                        .putExtra(PolicyActivity.EXTRA_IS_PRIVACY, false)
                )
            }
            .setPositiveButton("同意并继续") { _, _ ->
                userData.acceptPrivacy()
            }
            .setNegativeButton("不同意") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }
}
