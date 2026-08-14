package com.englishworld

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.englishworld.databinding.ItemWordBinding

class WordListAdapter(
    private val onItemClick: (Word) -> Unit
) : RecyclerView.Adapter<WordListAdapter.WordViewHolder>() {

    private var words = emptyList<Word>()

    inner class WordViewHolder(val binding: ItemWordBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = ItemWordBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = words[position]
        
        holder.binding.textWord.text = word.word
        holder.binding.textPhonetic.text = word.phonetic
        holder.binding.textMeaning.text = word.meaning
        holder.binding.textLevel.text = word.level
        
        holder.itemView.setOnClickListener {
            onItemClick(word)
        }
    }

    override fun getItemCount(): Int = words.size
    
    fun submitList(newWords: List<Word>) {
        words = newWords
        notifyDataSetChanged()
    }
}
