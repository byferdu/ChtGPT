package com.ferdu.chtgpt.ui;

import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;

import com.ferdu.chtgpt.R;

public class ParamFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.param_pref, rootKey);
        ListPreference models = findPreference("model");
        SeekBarPreference maximum_length = findPreference("maximum_length");
        if (models != null && maximum_length!=null) {
            if (models.getTitle().length() == 0) {
                models.setTitle("选择");
            }
            models.setTitle(models.getEntry());
            models.setDefaultValue("text-chat-davinci-002-20230126");
            models.setOnPreferenceChangeListener((preference, newValue) -> {
                models.setTitle(models.getEntries()[models.findIndexOfValue((String) newValue)]);
                if (newValue.toString().contains("davinci")&&!newValue.toString().contains("curie")) {
                    maximum_length.setMax(4000);
                }else maximum_length.setMax(2048);
                return true;
            });
            models.setSummaryProvider((Preference.SummaryProvider<ListPreference>) preference -> {
                int index = preference.findIndexOfValue(preference.getValue());
                CharSequence[] summaries = {
                        "ChatGPT语言模型",
                        "GPT-3系列中性能最好的型号。可以执行其他GPT-3模型可以执行的任何任务，通常具有更高的质量，更长的输出和更好的指令遵循。",
                        "GPT-3系列中功能最强大的型号的旧版本。可以执行其他GPT-3模型可以执行的任何任务，通常上下文更少。",
                         "PT-3系列的第二代型号。可以执行早期GPT-3模型可以执行的任何任务，通常上下文较少。每个请求最多可以处理4000个令牌。",
                        "Codex系列中功能最强大的模型，可以理解和生成代码，包括将自然语言翻译成代码。每个请求最多可以处理4000个令牌。",
                        "Codex系列中功能最强大的模型，可以理解和生成代码，包括将自然语言翻译成代码。每个请求最多可以处理4000个令牌。",
                        "几乎和codedavinci-002一样强大，但速度略快。Codex系列的一部分，可以理解和生成代码。",
                        "这种型号是我们原始的GPT-3系列的一部分。我们建议使用我们最新的GPT-3模型。",
                        "这种型号是我们原始的GPT-3系列的一部分。我们建议使用我们最新的GPT-3模型。",
                        "非常强大，但比text-davinci-003更快，成本更低。",
                        "这种型号是我们原始的GPT-3系列的一部分。我们建议使用我们最新的GPT-3模型。",
                        "能够直接的任务，非常快，和低成本",
                        "如果你想从英语翻译成API不熟悉的语言，你需要为它提供更多的例子，甚至微调一个模型来流利地做到这一点。",
                        "能够完成简单的任务，通常是GPT-3系列中速度最快的型号，成本最低。"
                };
                if (index >= 0 && summaries.length>index) {
                    return summaries[index];
                } else {
                    return "No size selected";
                }
            });
        }
    }
}