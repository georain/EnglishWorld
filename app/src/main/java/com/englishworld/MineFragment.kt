package com.englishworld

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.englishworld.databinding.FragmentMineBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MineFragment : Fragment() {

    private var _binding: FragmentMineBinding? = null
    private val binding get() = _binding!!

    private lateinit var userData: UserDataManager
    private lateinit var wordDatabase: WordDatabase

    // 隐藏的开发者调试入口：连点版本号 10 次触发，无任何提示
    private var versionTapCount = 0
    private var lastVersionTapTime = 0L

    // 选择 APK 文件的启动器：选中后复制到缓存并拉起系统安装器（覆盖安装本应用）
    private val pickApkLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) handlePickedApk(uri)
        }

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

        // 隐藏入口：连续点击版本号 10 次打开开发者调试信息页（无任何提示）
        binding.textVersion.setOnClickListener {
            val now = System.currentTimeMillis()
            // 两次点击间隔超过 2 秒则重新计数，避免误触
            versionTapCount = if (now - lastVersionTapTime <= 2000L) versionTapCount + 1 else 1
            lastVersionTapTime = now
            if (versionTapCount >= 10) {
                versionTapCount = 0
                showDeveloperInfoDialog()
            }
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

    private fun showDeveloperInfoDialog() {
        val ctx = requireContext()
        val pInfo = try {
            ctx.packageManager.getPackageInfo(ctx.packageName, 0)
        } catch (e: Exception) {
            null
        }
        val versionName = pInfo?.versionName ?: "1.0.0"
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pInfo?.longVersionCode?.toString() ?: "-"
        } else {
            @Suppress("DEPRECATION")
            pInfo?.versionCode?.toString() ?: "-"
        }
        val installTime = pInfo?.let {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(it.lastUpdateTime))
        } ?: "-"

        val allWords = wordDatabase.getAllWords()
        val primaryCount = allWords.count { it.level == "小学" }
        val middleCount = allWords.count { it.level == "初中" }
        val highCount = allWords.count { it.level == "高中" }

        val info = buildString {
            appendLine("【应用信息】")
            appendLine("包名：${ctx.packageName}")
            appendLine("版本名：v$versionName")
            appendLine("版本号：$versionCode")
            appendLine("安装/更新时间：$installTime")
            appendLine()
            appendLine("【词库统计】")
            appendLine("总词数：${allWords.size}")
            appendLine("小学：$primaryCount  初中：$middleCount  高中：$highCount")
            appendLine()
            appendLine("【设备信息】")
            appendLine("型号：${Build.MANUFACTURER} ${Build.MODEL}")
            appendLine("Android：${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})")
            appendLine("ABI：${Build.SUPPORTED_ABIS.joinToString(", ")}")
            appendLine()
            appendLine("【用户数据】")
            appendLine("已学习天数：${userData.studyDays}")
            appendLine("收藏数：${userData.getFavorites().size}")
            appendLine("历史数：${userData.getHistory().size}")
        }

        MaterialAlertDialogBuilder(ctx)
            .setTitle("开发者调试信息")
            .setMessage(info)
            .setPositiveButton("选择安装包更新本应用") { _, _ ->
                startPickApkForUpdate()
            }
            .setNeutralButton("复制") { _, _ ->
                val clipboard = ctx.getSystemService(android.content.Context.CLIPBOARD_SERVICE)
                    as android.content.ClipboardManager
                clipboard.setPrimaryClip(android.content.ClipData.newPlainText("debug_info", info))
                Toast.makeText(ctx, "已复制到剪贴板", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("关闭", null)
            .show()
    }

    /** 打开系统文件选择器挑选 APK（仅限 .apk / application 类型） */
    private fun startPickApkForUpdate() {
        try {
            pickApkLauncher.launch(arrayOf("application/vnd.android.package-archive", "application/octet-stream"))
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "无法打开文件选择器", Toast.LENGTH_SHORT).show()
        }
    }

    /** 处理选中的 APK：校验为本应用安装包 -> 复制到缓存 -> 拉起系统安装器 */
    private fun handlePickedApk(uri: Uri) {
        val ctx = requireContext()
        // 1. 复制到应用缓存目录（SAF 的 content Uri 系统安装器通常读不了，需转成自有文件）
        val apkFile = copyUriToCache(uri)
        if (apkFile == null) {
            Toast.makeText(ctx, "读取安装包失败", Toast.LENGTH_SHORT).show()
            return
        }
        // 2. 校验包名：仅允许本应用自己的安装包（更新本应用），拒绝任意外部应用
        val pkgName = ctx.packageManager
            .getPackageArchiveInfo(apkFile.absolutePath, 0)?.packageName
        if (pkgName == null) {
            Toast.makeText(ctx, "无效的安装包（无法解析）", Toast.LENGTH_LONG).show()
            apkFile.delete()
            return
        }
        if (pkgName != ctx.packageName) {
            Toast.makeText(
                ctx,
                "这不是本应用的安装包（包名：$pkgName），仅支持更新本应用",
                Toast.LENGTH_LONG
            ).show()
            apkFile.delete()
            return
        }
        // 3. 检查安装权限（Android 8+ 需“允许安装未知应用”），不足则引导开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            && !ctx.packageManager.canRequestPackageInstalls()
        ) {
            promptEnableInstallPermission()
            return
        }
        // 4. 拉起系统安装器（用户需在系统弹窗确认）
        launchSystemInstaller(apkFile)
    }

    /** 把选中的 content Uri 复制到 cacheDir/apk/update.apk */
    private fun copyUriToCache(uri: Uri): File? {
        return try {
            val ctx = requireContext()
            val dir = File(ctx.cacheDir, "apk").apply { mkdirs() }
            val out = File(dir, "update.apk")
            if (out.exists()) out.delete()
            ctx.contentResolver.openInputStream(uri)?.use { input ->
                out.outputStream().use { output -> input.copyTo(output) }
            } ?: return null
            if (out.length() <= 0L) null else out
        } catch (e: Exception) {
            null
        }
    }

    /** 引导用户到系统设置开启“允许安装未知应用” */
    private fun promptEnableInstallPermission() {
        val ctx = requireContext()
        MaterialAlertDialogBuilder(ctx)
            .setTitle("需要安装权限")
            .setMessage("系统尚未允许本应用安装安装包。请在接下来的设置页开启“允许安装未知应用”，然后重新选择安装包。")
            .setPositiveButton("去设置") { _, _ ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
                        startActivity(
                            Intent(
                                Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                                Uri.parse("package:${ctx.packageName}")
                            )
                        )
                    } catch (e: Exception) {
                        startActivity(Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES))
                    }
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }

    /** 通过 FileProvider 生成 Uri 并拉起系统安装器 */
    private fun launchSystemInstaller(apkFile: File) {
        val ctx = requireContext()
        try {
            val apkUri = FileProvider.getUriForFile(
                ctx, "${ctx.packageName}.fileprovider", apkFile
            )
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(apkUri, "application/vnd.android.package-archive")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(ctx, "无法启动安装：${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
