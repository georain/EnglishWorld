package com.englishworld

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.englishworld.databinding.FragmentMineBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MineFragment : Fragment() {

    private var _binding: FragmentMineBinding? = null
    private val binding get() = _binding!!

    private lateinit var userData: UserDataManager
    private lateinit var wordDatabase: WordDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userData = UserDataManager(requireContext())
        wordDatabase = WordDatabase(requireContext())
        setupUserInfo()
        setupClickListeners()
    }

    private fun setupUserInfo() {
        binding.textStudyDays.text = "已学习 ${userData.studyDays} 天"
        try {
            val packageInfo = requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
            binding.textVersion.text = "版本 v${packageInfo.versionName}"
        } catch (e: Exception) {
            binding.textVersion.text = "版本 v1.0.0"
        }
    }

    private fun setupClickListeners() {
        binding.cardFavorite.setOnClickListener {
            val favorites = wordDatabase.getAllWords()
                .filter { it.word in userData.getFavorites() }
            WordDialogs.showWordListDialog(
                requireContext(),
                "我的收藏（${favorites.size}）",
                favorites,
                userData
            ) {
                (activity as? MainActivity)?.navigateToTab(2)
            }
        }

        binding.cardHistory.setOnClickListener {
            val historyWords = userData.getHistory().mapNotNull { word ->
                wordDatabase.findByWord(word)
            }
            WordDialogs.showWordListDialog(
                requireContext(),
                "查询历史（${historyWords.size}）",
                historyWords,
                userData
            ) {
                (activity as? MainActivity)?.navigateToTab(1)
            }
        }

        binding.cardSettings.setOnClickListener {
            showSettingsDialog()
        }

        binding.cardWebsite.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://georain.github.io/EnglishWorld"))
            startActivity(intent)
        }

        binding.cardAbout.setOnClickListener {
            showAboutDialog()
        }

        binding.cardPrivacy.setOnClickListener {
            startActivity(
                Intent(requireContext(), PolicyActivity::class.java)
                    .putExtra(PolicyActivity.EXTRA_IS_PRIVACY, true)
            )
        }

        binding.cardTerms.setOnClickListener {
            startActivity(
                Intent(requireContext(), PolicyActivity::class.java)
                    .putExtra(PolicyActivity.EXTRA_IS_PRIVACY, false)
            )
        }
    }

    private fun showSettingsDialog() {
        val options = arrayOf(
            "清除查询历史",
            "清除已学记录",
            "清除收藏单词",
            "清除全部数据"
        )
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("设置")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("确认清除")
                            .setMessage("确定要清除所有查询历史吗？")
                            .setPositiveButton("确定") { _, _ ->
                                userData.clearHistory()
                                Toast.makeText(requireContext(), "已清除查询历史", Toast.LENGTH_SHORT).show()
                            }
                            .setNegativeButton("取消", null)
                            .show()
                    }
                    1 -> {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("确认清除")
                            .setMessage("确定要清除所有已学记录吗？")
                            .setPositiveButton("确定") { _, _ ->
                                userData.clearLearned()
                                Toast.makeText(requireContext(), "已清除已学记录", Toast.LENGTH_SHORT).show()
                            }
                            .setNegativeButton("取消", null)
                            .show()
                    }
                    2 -> {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("确认清除")
                            .setMessage("确定要清除所有收藏单词吗？")
                            .setPositiveButton("确定") { _, _ ->
                                userData.clearFavorites()
                                Toast.makeText(requireContext(), "已清除收藏单词", Toast.LENGTH_SHORT).show()
                            }
                            .setNegativeButton("取消", null)
                            .show()
                    }
                    3 -> {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("确认清除")
                            .setMessage("确定要清除全部数据吗？此操作不可恢复！")
                            .setPositiveButton("确定") { _, _ ->
                                userData.clearAll()
                                Toast.makeText(requireContext(), "已清除全部数据", Toast.LENGTH_SHORT).show()
                            }
                            .setNegativeButton("取消", null)
                            .show()
                    }
                }
            }
            .setNegativeButton("关闭", null)
            .show()
    }

    private fun showAboutDialog() {
        val versionName = try {
            requireContext().packageManager.getPackageInfo(requireContext().packageName, 0).versionName
        } catch (e: Exception) {
            "1.0.0"
        }
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("关于英语天地")
            .setMessage(
                "应用名称：英语天地\n" +
                "版本号：v$versionName\n\n" +
                "英语天地是一款专注于中小学英语学习的免费应用，" +
                "提供单词查询、分级学习、收藏管理、测试练习等功能。\n\n" +
                "本应用完全免费、纯本地运行，不收集任何个人信息。\n\n" +
                "官网：https://georain.github.io/EnglishWorld"
            )
            .setPositiveButton("访问官网") { _, _ ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://georain.github.io/EnglishWorld"))
                startActivity(intent)
            }
            .setNegativeButton("关闭", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
