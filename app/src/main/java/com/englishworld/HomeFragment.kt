package com.englishworld

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.cardview.widget.CardView
import java.util.Calendar

class HomeFragment : Fragment() {

    private var navigateListener: ((Int) -> Unit)? = null

    fun setOnNavigateListener(listener: (Int) -> Unit) {
        navigateListener = listener
    }

    // 每日一句（真实英语名言，按日期轮换）
    private val dailyQuotes = listOf(
        Pair("Practice makes perfect.", "熟能生巧"),
        Pair("Where there is a will, there is a way.", "有志者事竟成"),
        Pair("Actions speak louder than words.", "事实胜于雄辩"),
        Pair("Knowledge is power.", "知识就是力量"),
        Pair("Time waits for no one.", "时不我待"),
        Pair("The early bird catches the worm.", "早起的鸟儿有虫吃"),
        Pair("Never too old to learn.", "活到老，学到老"),
        Pair("Every coin has two sides.", "凡事皆有两面性"),
        Pair("Rome was not built in a day.", "冰冻三尺，非一日之寒"),
        Pair("Honesty is the best policy.", "诚实为上策"),
        Pair("No pain, no gain.", "不劳无获"),
        Pair("All roads lead to Rome.", "条条大路通罗马"),
        Pair("Well begun is half done.", "良好的开端是成功的一半"),
        Pair("Reading makes a full man.", "读书使人充实"),
        Pair("A friend in need is a friend indeed.", "患难见真情")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDailyQuote(view)
        setupFeatureCards(view)
        setupProgress(view)
    }

    /** 每日一句：按日期从真实名言库中轮换 */
    private fun setupDailyQuote(view: View) {
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val quote = dailyQuotes[(dayOfYear - 1) % dailyQuotes.size]
        view.findViewById<TextView>(R.id.textDailyQuote).text = quote.first
        view.findViewById<TextView>(R.id.textDailyQuoteCn).text = quote.second
    }

    /** 四个功能卡片全部可点击 */
    private fun setupFeatureCards(view: View) {
        view.findViewById<CardView>(R.id.cardWordSearch).setOnClickListener {
            navigateListener?.invoke(1) // 查词
        }
        view.findViewById<CardView>(R.id.cardDailyLearn).setOnClickListener {
            navigateListener?.invoke(2) // 学习
        }
        view.findViewById<CardView>(R.id.cardReview).setOnClickListener {
            // 复习巩固：打开收藏单词列表（真实收藏数据）
            val context = requireContext()
            val userData = UserDataManager(context)
            val favorites = WordDatabase(context).getAllWords()
                .filter { it.word in userData.getFavorites() }
            WordDialogs.showWordListDialog(
                context,
                "我的收藏（${favorites.size}）",
                favorites,
                userData
            )
        }
        view.findViewById<CardView>(R.id.cardTest).setOnClickListener {
            // 测试练习：随机出题，真实判分
            WordDialogs.showTest(requireContext(), WordDatabase(requireContext()), UserDataManager(requireContext()))
        }
    }

    /** 学习进度：全部基于真实数据（词库数量 + 用户已学记录） */
    private fun setupProgress(view: View) {
        val context = requireContext()
        val db = WordDatabase(context)
        val userData = UserDataManager(context)

        val learned = userData.getLearned()
        val total = db.getTotalCount()
        val learnedCount = learned.count { it in db.getAllWords().map { w -> w.word } }

        val textLearnedCount = view.findViewById<TextView>(R.id.textLearnedCount)
        val progressLearned = view.findViewById<ProgressBar>(R.id.progressLearned)
        val textPrimary = view.findViewById<TextView>(R.id.textPrimaryProgress)
        val textMiddle = view.findViewById<TextView>(R.id.textMiddleProgress)
        val textHigh = view.findViewById<TextView>(R.id.textHighProgress)

        textLearnedCount.text = "已学单词：$learnedCount / $total"
        progressLearned.progress = if (total == 0) 0 else (learnedCount * 100 / total)

        fun levelProgress(level: String): Pair<Int, Int> {
            val words = db.getWordsByLevel(level)
            val count = words.count { it.word in learned }
            return Pair(count, words.size)
        }

        val (pLearned, pTotal) = levelProgress(WordDatabase.LEVEL_PRIMARY)
        val (mLearned, mTotal) = levelProgress(WordDatabase.LEVEL_MIDDLE)
        val (hLearned, hTotal) = levelProgress(WordDatabase.LEVEL_HIGH)

        textPrimary.text = "小学：$pLearned/$pTotal"
        textMiddle.text = "初中：$mLearned/$mTotal"
        textHigh.text = "高中：$hLearned/$hTotal"
    }
}
