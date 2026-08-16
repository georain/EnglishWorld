package com.englishworld

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * 共享对话框工具：单词详情、单词列表（收藏/历史）、测试练习。
 * 所有对话框均为真实可交互功能，无任何模拟数据。
 */
object WordDialogs {

    /** 单词详情对话框：展示音标/释义/例句/等级，支持收藏与标记已学 */
    fun showWordDetail(
        context: Context,
        word: Word,
        userData: UserDataManager,
        onChanged: (() -> Unit)? = null
    ) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_word_detail, null)
        val tvWord = view.findViewById<android.widget.TextView>(R.id.tvDetailWord)
        val tvPhonetic = view.findViewById<android.widget.TextView>(R.id.tvDetailPhonetic)
        val tvMeaning = view.findViewById<android.widget.TextView>(R.id.tvDetailMeaning)
        val tvExample = view.findViewById<android.widget.TextView>(R.id.tvDetailExample)
        val tvLevel = view.findViewById<com.google.android.material.chip.Chip>(R.id.tvDetailLevel)
        val btnFavorite = view.findViewById<android.widget.TextView>(R.id.btnDetailFavorite)
        val btnLearned = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnDetailLearned)

        tvWord.text = word.word
        tvPhonetic.text = word.phonetic
        tvMeaning.text = word.meaning
        tvExample.text = word.example
        tvLevel.text = "难度等级：${word.level}"

        fun refresh() {
            btnFavorite.text = if (userData.isFavorite(word.word)) "★ 已收藏" else "☆ 收藏"
            btnFavorite.setTextColor(if (userData.isFavorite(word.word)) Color.parseColor("#E6A23C") else Color.parseColor("#666666"))
            btnLearned.text = if (userData.isLearned(word.word)) "✓ 已学会" else "标记为已学"
            onChanged?.invoke()
        }

        btnFavorite.setOnClickListener {
            val added = userData.toggleFavorite(word.word)
            Toast.makeText(context, if (added) "已收藏" else "已取消收藏", Toast.LENGTH_SHORT).show()
            refresh()
        }

        btnLearned.setOnClickListener {
            if (userData.isLearned(word.word)) {
                userData.unmarkLearned(word.word)
                Toast.makeText(context, "已取消标记", Toast.LENGTH_SHORT).show()
            } else {
                userData.markLearned(word.word)
                Toast.makeText(context, "已标记为已学", Toast.LENGTH_SHORT).show()
            }
            refresh()
        }

        refresh()
        MaterialAlertDialogBuilder(context)
            .setView(view)
            .setPositiveButton("关闭", null)
            .show()
    }

    /** 通用单词列表对话框（用于收藏/历史），点击条目查看详情 */
    fun showWordListDialog(
        context: Context,
        title: String,
        words: List<Word>,
        userData: UserDataManager,
        onEmptyAction: (() -> Unit)? = null
    ) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_word_list, null)
        val tvTitle = view.findViewById<android.widget.TextView>(R.id.tvListTitle)
        val tvEmpty = view.findViewById<android.widget.TextView>(R.id.tvListEmpty)
        val rv = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvWordList)

        tvTitle.text = title

        val dialog = MaterialAlertDialogBuilder(context)
            .setView(view)
            .setPositiveButton("关闭", null)
            .show()

        if (words.isEmpty()) {
            rv.visibility = View.GONE
            tvEmpty.visibility = View.VISIBLE
            tvEmpty.text = "暂无数据\n${if (onEmptyAction != null) "点击下方按钮去添加吧" else "去学习/查词页面添加吧"}"
            return
        }

        val adapter = WordListAdapter { word ->
            showWordDetail(context, word, userData) {
                // 收藏变化后刷新列表
                val updated = WordDatabase(context).getAllWords()
                    .filter { it.word in userData.getFavorites() }
                if (words.any { it.word !in updated.map { w -> w.word } }) {
                    dialog.dismiss()
                    showWordListDialog(context, title, updated, userData, onEmptyAction)
                }
            }
        }
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = adapter
        adapter.submitList(words)
    }

    /** 测试练习对话框：从词库随机出 10 道选择题，实时判分 */
    fun showTest(context: Context, db: WordDatabase, userData: UserDataManager) {
        val allWords = db.getAllWords()
        if (allWords.size < 4) {
            Toast.makeText(context, "词库数据不足，无法出题", Toast.LENGTH_SHORT).show()
            return
        }

        val questions = allWords.shuffled().take(10)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_test, null)
        val tvProgress = view.findViewById<android.widget.TextView>(R.id.tvTestProgress)
        val tvScore = view.findViewById<android.widget.TextView>(R.id.tvTestScore)
        val tvQuestion = view.findViewById<android.widget.TextView>(R.id.tvTestQuestion)
        val tvResult = view.findViewById<android.widget.TextView>(R.id.tvTestResult)
        val btnNext = view.findViewById<MaterialButton>(R.id.btnTestNext)
        val optionButtons = listOf(
            view.findViewById<MaterialButton>(R.id.btnTestOption1),
            view.findViewById<MaterialButton>(R.id.btnTestOption2),
            view.findViewById<MaterialButton>(R.id.btnTestOption3),
            view.findViewById<MaterialButton>(R.id.btnTestOption4)
        )

        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle("测试练习")
            .setView(view)
            .show()

        var index = 0
        var score = 0
        var answered = false

        fun resetButton(btn: MaterialButton) {
            btn.isEnabled = true
            btn.setBackgroundColor(Color.TRANSPARENT)
            btn.setTextColor(Color.parseColor("#333333"))
        }

        fun loadQuestion() {
            answered = false
            tvResult.visibility = View.GONE
            btnNext.visibility = View.GONE
            optionButtons.forEach { resetButton(it) }

            if (index >= questions.size) {
                tvQuestion.text = "测试完成！"
                tvProgress.text = "共 10 题"
                tvScore.text = "得分：$score"
                tvResult.visibility = View.VISIBLE
                tvResult.text = if (score >= 8) "太棒了！继续加油！" else if (score >= 6) "不错，继续保持！" else "坚持练习，会越来越好！"
                optionButtons.forEach { it.visibility = View.GONE }
                btnNext.text = "完成"
                btnNext.visibility = View.VISIBLE
                btnNext.setOnClickListener { dialog.dismiss() }
                return
            }

            val current = questions[index]
            val options = (listOf(current.word) +
                allWords.filter { it.word != current.word }.shuffled().take(3).map { it.word }).shuffled()

            tvProgress.text = "第 ${index + 1} / ${questions.size} 题"
            tvScore.text = "得分：$score"
            tvQuestion.text = current.meaning.split("；").first()

            options.forEachIndexed { i, opt ->
                optionButtons[i].text = opt
                optionButtons[i].setOnClickListener {
                    if (answered) return@setOnClickListener
                    answered = true
                    val correctBtn = optionButtons[options.indexOfFirst { it == current.word }]

                    if (opt == current.word) {
                        score++
                        tvScore.text = "得分：$score"
                        optionButtons[i].setBackgroundColor(Color.parseColor("#4CAF50"))
                        optionButtons[i].setTextColor(Color.WHITE)
                        tvResult.text = "回答正确！${current.word} ${current.meaning}"
                    } else {
                        optionButtons[i].setBackgroundColor(Color.parseColor("#F44336"))
                        optionButtons[i].setTextColor(Color.WHITE)
                        correctBtn.setBackgroundColor(Color.parseColor("#4CAF50"))
                        correctBtn.setTextColor(Color.WHITE)
                        tvResult.text = "回答错误！正确答案：${current.word} ${current.meaning}"
                    }
                    optionButtons.forEach { it.isEnabled = false }
                    tvResult.visibility = View.VISIBLE
                    btnNext.visibility = View.VISIBLE
                }
            }
            btnNext.setOnClickListener {
                index++
                loadQuestion()
            }
        }

        loadQuestion()
    }
}
