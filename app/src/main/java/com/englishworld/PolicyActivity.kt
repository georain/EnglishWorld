package com.englishworld

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * 隐私政策 / 用户协议 展示页
 */
class PolicyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policy)

        val isPrivacy = intent.getBooleanExtra(EXTRA_IS_PRIVACY, true)

        findViewById<TextView>(R.id.textPolicyTitle).text =
            if (isPrivacy) "隐私政策" else "用户协议"

        findViewById<TextView>(R.id.textPolicyContent).text =
            if (isPrivacy) PRIVACY_POLICY else USER_AGREEMENT
    }

    companion object {
        const val EXTRA_IS_PRIVACY = "is_privacy"

        val PRIVACY_POLICY = """
《英语单词学习隐私政策》

更新日期：2026年8月14日
生效日期：2026年8月14日

欢迎使用"英语单词学习"应用（以下简称"本应用"）。我们深知个人信息对您的重要性，并会尽全力保护您的个人信息安全。本隐私政策将帮助您了解本应用如何收集、使用、存储和保护您的信息。

一、我们收集的信息
1. 本应用是一款纯本地运行的英语学习工具，无需注册、登录即可使用全部功能。
2. 本应用不收集任何可用于识别您个人身份的信息，包括但不限于姓名、手机号、邮箱、位置、通讯录、相册等。
3. 您在使用过程中产生的学习数据（如收藏单词、查询历史、已学单词、学习天数）仅保存在您设备本地的应用存储空间中。

二、信息的使用
1. 本应用不会将您的任何数据上传至服务器，也不会与任何第三方共享您的数据。
2. 本应用仅在您主动点击"访问官网"时使用网络连接打开官网页面，此过程不会传输您的学习数据。

三、信息的存储与保护
1. 您的学习数据以本地文件形式存储于设备中，卸载应用或清除应用数据后将被删除。
2. 由于所有数据均保存在本地，不经过网络传输，因此不存在被服务商泄露的风险。

四、未成年人保护
1. 本应用面向各年龄段的学习者。由于本应用不收集任何个人信息，无需监护人额外授权。
2. 我们建议未成年人在监护人指导下使用本应用。

五、权限说明
1. 本应用不需要任何敏感权限（如相机、麦克风、通讯录、定位等）。
2. 本应用仅可能需要网络权限，用于在您主动访问官网时加载网页内容。

六、隐私政策的更新
1. 我们可能会适时修订本隐私政策。更新后的政策将在官网公布，并在应用内提供查看入口。
2. 重大变更时，我们将在应用内显著提示。

七、联系我们
如您对本隐私政策有任何疑问、意见或建议，请通过官网（https://georain.github.io/EnglishWorld）与我们联系。
        """.trimIndent()

        val USER_AGREEMENT = """
《英语单词学习用户协议》

更新日期：2026年8月14日
生效日期：2026年8月14日

欢迎使用"英语单词学习"应用（以下简称"本应用"）。请您在使用本应用前仔细阅读本用户协议（以下简称"本协议"）。您开始使用本应用，即视为您已阅读并同意本协议的全部内容。

一、服务说明
1. 本应用提供英语单词查询、分级学习、收藏、测试练习等学习功能。
2. 本应用为免费学习工具，全部功能均可正常使用。

二、用户行为规范
1. 您应遵守法律法规，不得利用本应用从事任何违法违规活动。
2. 您不得对本应用进行反向工程、篡改、破解或以其他方式破坏其正常运行。
3. 您应妥善保管您的设备，因设备丢失、损坏导致的学习数据丢失，本应用不承担责任。

三、知识产权
1. 本应用的界面设计、程序代码、文字内容等均受相关法律保护。
2. 本应用收录的单词及释义仅为语言学习用途，相关版权归其原作者所有。

四、免责声明
1. 本应用提供的词库内容力求准确，但难免存在疏漏，仅作为学习参考，不构成任何学术或考试依据。
2. 因不可抗力、设备故障、网络异常等原因导致服务中断或数据丢失，本应用不承担由此产生的损失。
3. 本应用为个人开发者维护的免费项目，不提供商业担保。

五、协议的变更
本应用有权根据业务发展需要适时修订本协议，修订后的协议将在官网公布。您继续使用本应用即视为接受修订后的协议。

六、联系我们
如您对本协议有任何疑问，请通过官网（https://georain.github.io/EnglishWorld）与我们联系。
        """.trimIndent()
    }
}
