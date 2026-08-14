package com.englishworld

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.englishworld.databinding.FragmentMineBinding

class MineFragment : Fragment() {

    private var _binding: FragmentMineBinding? = null
    private val binding get() = _binding!!

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
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        binding.cardFavorite.setOnClickListener {
            // 收藏的单词
        }
        
        binding.cardHistory.setOnClickListener {
            // 查询历史
        }
        
        binding.cardSettings.setOnClickListener {
            // 设置
        }
        
        binding.cardWebsite.setOnClickListener {
            // 访问官网
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://englishworld.app"))
            startActivity(intent)
        }
        
        binding.cardAbout.setOnClickListener {
            // 关于
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
