package com.englishworld

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.englishworld.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var wordDatabase: WordDatabase
    private lateinit var userData: UserDataManager
    private var currentWord: Word? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wordDatabase = WordDatabase(requireContext())
        userData = UserDataManager(requireContext())
        setupSearchView()
        setupClickListener()
    }

    private fun setupSearchView() {
        binding.editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                if (query.isNotEmpty()) {
                    searchWord(query)
                } else {
                    clearResult()
                }
            }
        })
    }

    private fun searchWord(word: String) {
        val result = wordDatabase.searchWord(word)
        if (result != null) {
            userData.addHistory(result.word)
            displayWordResult(result)
        } else {
            binding.textNoResult.visibility = View.VISIBLE
            binding.cardResult.visibility = View.GONE
            currentWord = null
        }
    }

    private fun displayWordResult(result: Word) {
        currentWord = result
        binding.cardResult.visibility = View.VISIBLE
        binding.textNoResult.visibility = View.GONE

        binding.textWord.text = result.word
        binding.textPhonetic.text = result.phonetic
        binding.textMeaning.text = result.meaning
        binding.textExample.text = result.example
        binding.textLevel.text = "难度等级：${result.level}"

        refreshFavoriteButton()

        binding.btnFavorite.setOnClickListener {
            currentWord?.let { word ->
                val added = userData.toggleFavorite(word.word)
                Toast.makeText(
                    requireContext(),
                    if (added) "已收藏" else "已取消收藏",
                    Toast.LENGTH_SHORT
                ).show()
                refreshFavoriteButton()
            }
        }

        binding.cardResult.setOnClickListener {
            currentWord?.let { word ->
                WordDialogs.showWordDetail(requireContext(), word, userData) {
                    refreshFavoriteButton()
                }
            }
        }
    }

    private fun refreshFavoriteButton() {
        currentWord?.let { word ->
            if (userData.isFavorite(word.word)) {
                binding.btnFavorite.text = "★ 已收藏"
                binding.btnFavorite.setTextColor(Color.parseColor("#E6A23C"))
            } else {
                binding.btnFavorite.text = "☆ 收藏"
                binding.btnFavorite.setTextColor(Color.parseColor("#4A90D9"))
            }
        }
    }

    private fun clearResult() {
        binding.cardResult.visibility = View.GONE
        binding.textNoResult.visibility = View.GONE
        currentWord = null
    }

    private fun setupClickListener() {
        binding.btnSearch.setOnClickListener {
            val word = binding.editSearch.text.toString().trim()
            if (word.isNotEmpty()) {
                searchWord(word)
            } else {
                Toast.makeText(context, "请输入要查询的单词", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        currentWord?.let {
            refreshFavoriteButton()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
