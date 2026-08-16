package com.englishworld

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.englishworld.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userData: UserDataManager

    private val tabTitles = listOf(
        R.string.tab_home,
        R.string.tab_search,
        R.string.tab_learn,
        R.string.tab_mine
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            userData = UserDataManager(this)

            setupViewPager()
            setupTabLayout()

            binding.root.post {
                if (!userData.isPrivacyAccepted) showPolicyDialog()
            }
        } catch (e: Throwable) {
            // 捕获所有错误，直接显示在屏幕上
            val tv = TextView(this).apply {
                text = android.util.Log.getStackTraceString(e)
                setPadding(40, 80, 40, 80)
                textSize = 14f
                setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Small)
            }
            setContentView(tv)
        }
    }

    fun navigateToTab(position: Int) {
        if (position in 0 until 4) {
            binding.viewPager.setCurrentItem(position, true)
        }
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = ViewPagerAdapter(this)
    }

    private fun setupTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setText(tabTitles[position])
        }.attach()

        binding.tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_home)
        binding.tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_search)
        binding.tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_learn)
        binding.tabLayout.getTabAt(3)?.setIcon(R.drawable.ic_mine)
    }

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
