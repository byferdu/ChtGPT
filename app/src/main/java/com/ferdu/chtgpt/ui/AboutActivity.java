package com.ferdu.chtgpt.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ferdu.chtgpt.R;
import com.ferdu.chtgpt.util.MyUtil;
import com.google.android.material.appbar.MaterialToolbar;

public class AboutActivity extends AppCompatActivity {
    String md = "# 关于ChatGPT\n" +
            "\n" +
            "## ChatGPT是什么？\n" +
            "\n" +
            "ChatGPT是优化对话的语言模型。OpenAI训练了一个名为ChatGPT的模型，它以对话的方式进行交互。对话形式使ChatGPT能够回答后续问题，承认错误，质疑不正确的前提，并拒绝不适当的请求。ChatGPT是InstructGPT的兄弟模型，后者经过训练，以遵循提示中的指令并提供详细的响应。\n" +
            "<br>\n" +
            "<br>**一句话就是：你提任何问题它都能回应并让你比较满意**\n" +
            "<br>为详细了解访问[官方介绍](https://openai.com/blog/chatgpt/) 或者 [公众号文章](https://mp.weixin.qq.com/s/ol__ugwMqDcXANYiBKCf-g)\n" +
            "\n" +
            "## 特点\n" +
            "1. 新增代码理解和生成能力，对输入的理解能力和包容度高，能在绝大部分知识领域给出专业回答。\n" +
            "\n" +
            "2. 加入道德原则。即ChatGPT能够识别恶意信息，识别后拒绝给出有效回答。\n" +
            "\n" +
            "3. 支持连续对话。ChatGPT具有记忆能力，提高了模型的交互体验。\n" +
            "\n" +
            "> **注意： 因官方提供的接口限制，此App暂不支持连续对话**\n" +
            "\n" +
            "<br>\n" +
            "\n" +
            "\n" +
            "## 怎么使用？\n" +
            "\n" +
            "<br>\n" +
            "\n" +
            "> 不管什么方法，需要先 **注册↓↓**\n" +
            "\n" +
            "<br>\n" +
            "\n" +
            "### 方法一\n" +
            "\n" +
            "<br>\n" +
            "\n" +
            "\n" +
            "- 使用此App\n" +
            "\n" +
            "    - 优点：直接使用，不用注册，不用翻q，方便快捷。\n" +
            "\n" +
            "    - 缺点：不能连续对话，不能编辑内容，不能评价AI的回答。\n" +
            "\n" +
            "### 方法二\n" +
            "\n" +
            "<br>\n" +
            "\n" +
            "- 访问官方提供的地址 <https://chat.openai.com/chat>\n" +
            "\n" +
            "    - 优点：能够连续对话，能够编辑内容，能够评价AI的回答。\n" +
            "\n" +
            "    - 缺点：因ChatGPT对中国暂未开放原因，注册麻烦，需要翻q，繁琐的重登、验证你是否机器人。\n" +
            "\n" +
            "<br>\n" +
            "\n" +
            "## 怎么注册\n" +
            "\n" +
            "<br>\n" +
            "\n" +
            "### 注册教程\n" +
            "\n" +
            "* 联系 [QQ](mqqwpa://im/chat?chat_type=wpa&uin=2623476654)\n" +
            "* [知乎上的教程1](https://zhuanlan.zhihu.com/p/589983587)\n" +
            "* [知乎上的教程2](https://zhuanlan.zhihu.com/p/589642999)\n" +
            "* [上淘宝买账号](https://m.tb.cn/h.Ukz10Nu?tk=26led4vW2z0)\n" +
            "<br>\n" +
            "\n" +
            "<br>\n" +
            "\n" +
            "\n" +
            "# 关于此应用\n" +
            "\n" +
            "此应用是通过官方提供的API实现了ChatGPT的问答功能。你可以问任何问题。\n" +
            "<br>APP的界面跟官方没有太大差异，还支持[Markdown](https://baike.baidu.com/item/markdown/3245829)格式的输出，包括代码语法突出显示，带来更好的阅读体验。\n" +
            "\n" +
            ">  作者qq：[2623476654](mqqwpa://im/chat?chat_type=wpa&uin=2623476654) ;  vx: `Al3mdar` \n";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView mdTextView = findViewById(R.id.mdText);
        TextView copyWe = findViewById(R.id.copy_wechat);
        MaterialToolbar materialToolbar = findViewById(R.id.mToolbarAbout);
        MyUtil.getMarkDown(this).setMarkdown(mdTextView, md);
        materialToolbar.setNavigationOnClickListener(v -> onBackPressed());
        copyWe.setOnClickListener(v -> {
            ClipboardManager cm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText("text", "Al3mdar"));//text也可以是"null"
            if (cm.hasPrimaryClip()) {
                cm.getPrimaryClip().getItemAt(0).getText();
            }
            Toast.makeText(this, "Al3mdar ,复制成功 🤗", Toast.LENGTH_SHORT).show();

        });
    }
}