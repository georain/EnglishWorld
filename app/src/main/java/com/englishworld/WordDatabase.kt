package com.englishworld

import android.content.Context

class WordDatabase(private val context: Context) {
    
    // 小学英语单词（部分示例）
    private val primarySchoolWords = listOf(
        Word("apple", "/ˈæpl/", "苹果", "I eat an apple every day.", "小学"),
        Word("book", "/bʊk/", "书", "This is a good book.", "小学"),
        Word("cat", "/kæt/", "猫", "The cat is cute.", "小学"),
        Word("dog", "/dɒɡ/", "狗", "I have a pet dog.", "小学"),
        Word("egg", "/eɡ/", "蛋", "I have an egg for breakfast.", "小学"),
        Word("fish", "/fɪʃ/", "鱼", "Fish can swim.", "小学"),
        Word("girl", "/ɡɜːl/", "女孩", "She is a nice girl.", "小学"),
        Word("hand", "/hænd/", "手", "Raise your hand.", "小学"),
        Word("ice", "/aɪs/", "冰", "The ice is cold.", "小学"),
        Word("juice", "/dʒuːs/", "果汁", "I drink orange juice.", "小学"),
        Word("key", "/kiː/", "钥匙", "This is my key.", "小学"),
        Word("lion", "/ˈlaɪən/", "狮子", "The lion is strong.", "小学"),
        Word("moon", "/muːn/", "月亮", "Look at the moon.", "小学"),
        Word("nose", "/nəʊz/", "鼻子", "I have a small nose.", "小学"),
        Word("orange", "/ˈɒrɪndʒ/", "橙子；橙色", "I like oranges.", "小学"),
        Word("pen", "/pen/", "钢笔", "Use a pen to write.", "小学"),
        Word("queen", "/kwiːn/", "女王", "The queen is kind.", "小学"),
        Word("rain", "/reɪn/", "雨", "It is raining.", "小学"),
        Word("sun", "/sʌn/", "太阳", "The sun is bright.", "小学"),
        Word("tree", "/triː/", "树", "Plant more trees.", "小学"),
        Word("umbrella", "/ʌmˈbrelə/", "雨伞", "Take an umbrella.", "小学"),
        Word("violin", "/ˌvaɪəˈlɪn/", "小提琴", "She plays the violin.", "小学"),
        Word("water", "/ˈwɔːtə(r)/", "水", "Drink more water.", "小学"),
        Word("box", "/bɒks/", "盒子", "Put it in the box.", "小学"),
        Word("yellow", "/ˈjeləʊ/", "黄色", "The banana is yellow.", "小学"),
        Word("zoo", "/zuː/", "动物园", "Let's go to the zoo.", "小学"),
        Word("school", "/skuːl/", "学校", "I go to school by bus.", "小学"),
        Word("teacher", "/ˈtiːtʃə(r)/", "老师", "Our teacher is nice.", "小学"),
        Word("student", "/ˈstjuːdnt/", "学生", "He is a good student.", "小学"),
        Word("friend", "/frend/", "朋友", "She is my best friend.", "小学"),
        Word("family", "/ˈfæmɪli/", "家庭", "I love my family.", "小学"),
        Word("mother", "/ˈmʌðə(r)/", "妈妈", "My mother cooks well.", "小学"),
        Word("father", "/ˈfɑːðə(r)/", "爸爸", "My father is tall.", "小学"),
        Word("happy", "/ˈhæpi/", "快乐的", "I am very happy.", "小学"),
        Word("big", "/bɪɡ/", "大的", "The elephant is big.", "小学"),
        Word("small", "/smɔːl/", "小的", "The cat is small.", "小学"),
        Word("good", "/ɡʊd/", "好的", "You are a good boy.", "小学"),
        Word("bad", "/bæd/", "坏的", "Don't be bad.", "小学"),
        Word("red", "/red/", "红色", "The apple is red.", "小学"),
        Word("blue", "/bluː/", "蓝色", "The sky is blue.", "小学"),
        Word("green", "/ɡriːn/", "绿色", "Grass is green.", "小学"),
        Word("one", "/wʌn/", "一", "I have one book.", "小学"),
        Word("two", "/tuː/", "二", "I have two hands.", "小学"),
        Word("three", "/θriː/", "三", "Count to three.", "小学")
    )
    
    // 初中英语单词（部分示例）
    private val middleSchoolWords = listOf(
        Word("ability", "/əˈbɪləti/", "能力，才能", "She has the ability to solve problems.", "初中"),
        Word("achieve", "/əˈtʃiːv/", "达到，完成", "Work hard to achieve your goals.", "初中"),
        Word("advantage", "/ədˈvɑːntɪdʒ/", "优势，优点", "What are the advantages?", "初中"),
        Word("beautiful", "/ˈbjuːtɪfl/", "美丽的", "The flower is beautiful.", "初中"),
        Word("believe", "/bɪˈliːv/", "相信", "I believe in you.", "初中"),
        Word("challenge", "/ˈtʃælɪndʒ/", "挑战", "Face the challenge bravely.", "初中"),
        Word("communicate", "/kəˈmjuːnɪkeɪt/", "交流，沟通", "We need to communicate more.", "初中"),
        Word("decision", "/dɪˈsɪʒn/", "决定", "Make a wise decision.", "初中"),
        Word("environment", "/ɪnˈvaɪrənmənt/", "环境", "Protect our environment.", "初中"),
        Word("experience", "/ɪkˈspɪəriəns/", "经验，经历", "Learn from experience.", "初中"),
        Word("famous", "/ˈfeɪməs/", "著名的", "He is a famous writer.", "初中"),
        Word("government", "/ˈɡʌvənmənt/", "政府", "The government helps people.", "初中"),
        Word("happiness", "/ˈhæpinəs/", "幸福，快乐", "Money cannot buy happiness.", "初中"),
        Word("important", "/ɪmˈpɔːtnt/", "重要的", "Health is important.", "初中"),
        Word("knowledge", "/ˈnɒlɪdʒ/", "知识", "Knowledge is power.", "初中"),
        Word("language", "/ˈlæŋɡwɪdʒ/", "语言", "English is a global language.", "初中"),
        Word("memory", "/ˈmeməri/", "记忆，记忆力", "I have a good memory.", "初中"),
        Word("necessary", "/ˈnesəsəri/", "必要的", "It is necessary to study hard.", "初中"),
        Word("opportunity", "/ˌɒpəˈtjuːnəti/", "机会", "Seize every opportunity.", "初中"),
        Word("practice", "/ˈpræktɪs/", "练习", "Practice makes perfect.", "初中"),
        Word("question", "/ˈkwestʃən/", "问题", "Do you have any questions?", "初中"),
        Word("remember", "/rɪˈmembə(r)/", "记住", "Remember to call me.", "初中"),
        Word("success", "/səkˈses/", "成功", "Wish you success!", "初中"),
        Word("technology", "/tekˈnɒlədʒi/", "技术", "Technology changes our lives.", "初中"),
        Word("understand", "/ˌʌndəˈstænd/", "理解", "I understand your feeling.", "初中"),
        Word("volunteer", "/ˌvɒlənˈtɪə(r)/", "志愿者", "She works as a volunteer.", "初中"),
        Word("weather", "/ˈweðə(r)/", "天气", "How's the weather today?", "初中"),
        Word("excellent", "/ˈeksələnt/", "优秀的", "Your work is excellent.", "初中"),
        Word("favorite", "/ˈfeɪvərɪt/", "最喜欢的", "What's your favorite color?", "初中"),
        Word("different", "/ˈdɪfrənt/", "不同的", "We are different but equal.", "初中"),
        Word("difficult", "/ˈdɪfɪkəlt/", "困难的", "The exam was difficult.", "初中"),
        Word("possible", "/ˈpɒsəbl/", "可能的", "Everything is possible.", "初中"),
        Word("impossible", "/ɪmˈpɒsəbl/", "不可能的", "Nothing is impossible.", "初中"),
        Word("comfortable", "/ˈkʌmftəbl/", "舒适的", "The chair is comfortable.", "初中"),
        Word("popular", "/ˈpɒpjələ(r)/", "流行的", "This song is very popular.", "初中"),
        Word("dangerous", "/ˈdeɪndʒərəs/", "危险的", "It's dangerous to swim alone.", "初中"),
        Word("expensive", "/ɪkˈspensɪv/", "昂贵的", "This car is too expensive.", "初中"),
        Word("interesting", "/ˈɪntrəstɪŋ/", "有趣的", "The story is interesting.", "初中"),
        Word("wonderful", "/ˈwʌndəfl/", "精彩的", "We had a wonderful time.", "初中"),
        Word("attention", "/əˈtenʃn/", "注意", "Pay attention to the teacher.", "初中"),
        Word("information", "/ˌɪnfəˈmeɪʃn/", "信息", "Can I get some information?", "初中"),
        Word("education", "/ˌedʒuˈkeɪʃn/", "教育", "Education is important.", "初中"),
        Word("development", "/dɪˈveləpmənt/", "发展", "China's development is fast.", "初中"),
        Word("traditional", "/trəˈdɪʃənl/", "传统的", "It's a traditional festival.", "初中"),
        Word("international", "/ˌɪntəˈnæʃənl/", "国际的", "It's an international airport.", "初中")
    )

    fun searchWord(query: String): Word? {
        val allWords = primarySchoolWords + middleSchoolWords
        return allWords.find { 
            it.word.equals(query, ignoreCase = true) || 
            it.word.startsWith(query, ignoreCase = true)
        }
    }
    
    fun searchWords(query: String): List<Word> {
        val allWords = primarySchoolWords + middleSchoolWords
        return allWords.filter { 
            it.word.contains(query, ignoreCase = true) ||
            it.meaning.contains(query)
        }.take(20)
    }
    
    fun getWordsByLevel(level: String): List<Word> {
        return when (level) {
            "小学" -> primarySchoolWords
            "初中" -> middleSchoolWords
            else -> primarySchoolWords + middleSchoolWords
        }
    }
    
    fun getAllWords(): List<Word> {
        return primarySchoolWords + middleSchoolWords
    }
}
