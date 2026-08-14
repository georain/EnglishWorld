package com.englishworld

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.englishworld.databinding.FragmentLearnBinding

class LearnFragment : Fragment() {

    private var _binding: FragmentLearnBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var wordDatabase: WordDatabase
    private lateinit var adapter: WordListAdapter
    
    private var currentLevel = "小学"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLearnBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        wordDatabase = WordDatabase(requireContext())
        setupRecyclerView()
        setupLevelSelector()
        loadWordsByLevel(currentLevel)
    }
    
    private fun setupRecyclerView() {
        adapter = WordListAdapter { word ->
            // 点击单词事件，可以跳转到详情或播放发音
        }
        binding.recyclerViewWords.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewWords.adapter = adapter
    }
    
    private fun setupLevelSelector() {
        binding.chipPrimary.setOnClickListener { 
            currentLevel = "小学"
            loadWordsByLevel(currentLevel) 
        }
        binding.chipMiddle.setOnClickListener { 
            currentLevel = "初中"
            loadWordsByLevel(currentLevel) 
        }
        binding.chipHigh.setOnClickListener { 
            currentLevel = "高中"
            loadWordsByLevel(currentLevel) 
        }
    }
    
    private fun loadWordsByLevel(level: String) {
        val words = wordDatabase.getWordsByLevel(level)
        adapter.submitList(words)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
