package com.ferdu.chtgpt.util;

import androidx.preference.ListPreference;
import androidx.preference.Preference;

public class ModeSummaryProvider implements Preference.SummaryProvider<ListPreference> {
    @Override
    public CharSequence provideSummary(ListPreference preference) {
        int index = preference.findIndexOfValue(preference.getValue());
        CharSequence[] summaries = {
                "该 API 非常擅长与人类甚至自身进行对话。只需几行指令，我们就看到 API 可以作为客户服务聊天机器人执行，它可以智能地回答问题而不会感到慌乱，或者是一个明智的对话伙伴，可以开玩笑和双关语。关键是告诉 API 它应该如何运行，然后提供一些示例。",
                "虽然所有提示都会导致完成，但在您希望 API 从上次中断的地方继续的情况下，将文本完成视为自己的任务可能会很有帮助。例如，如果给出此提示，API 将继续思考垂直农业。您可以降低温度设置以使 API 更专注于提示的意图，也可以提高温度设置以使其偏离正切线。",
                "API 能够掌握文本的上下文并以不同的方式改写它。在这个例子中，我们创建了一个解释，一个孩子可以从更长、更复杂的文本段落中理解。这说明 API 对语言有深入的掌握。",
                "在此示例中，我们将电影名称转换为表情符号。这显示了 API 对拾取模式和处理其他字符的适应性。",
                "如果你想从英语翻译成API不熟悉的语言，你需要为它提供更多的例子，甚至微调一个模型来流利地做到这一点。",
                "API 有很多知识，它是从训练它的数据中学习的。它还能够提供听起来非常真实但实际上是编造的响应。有两种方法可以限制 API 组成答案的可能性。"
        };
        if (index >= 0 && summaries.length>index) {
            return summaries[index];
        } else {
            return "No size selected";
        }
    }
}
