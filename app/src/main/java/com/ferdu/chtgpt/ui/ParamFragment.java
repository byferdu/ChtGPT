package com.ferdu.chtgpt.ui;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;

import com.ferdu.chtgpt.R;
import com.ferdu.chtgpt.models.data.Model;
import com.ferdu.chtgpt.viewmodel.MyViewModel;

import java.util.ArrayList;
import java.util.List;

public class ParamFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.param_pref, rootKey);
        ListPreference models = findPreference("model");
        List<Model> modelList = new ArrayList<>();
        MyViewModel myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        SeekBarPreference maximum_length = findPreference("maximum_length");
        if (maximum_length != null && models != null) {

            myViewModel.getModels().observe(requireActivity(), models1 -> {
                if (models1 == null) {
                    models1 = new ArrayList<>();
                }
                modelList.addAll(models1);
                CharSequence[] entries = new CharSequence[models1.size()];
                CharSequence[] entryValues = new CharSequence[models1.size()];
                for (int i = 0; i < models1.size(); i++) {
                    entries[i] = models1.get(i).getModel();
                    entryValues[i] = models1.get(i).getModel();
                }
                models.setEntries(entries);
                models.setEntryValues(entryValues);

                if (models1.size() < 1) {
                    models.setTitle("text-davinci-003");
                    models.setValue("text-davinci-003");
                    return;
                }
                if (models.getValue() == null || models.getValue().isEmpty()) {
                    models.setValueIndex(0);
                    models.setTitle(models1.get(0).getModel());
                } else {
                    models.setTitle(models.getValue());
                }

            });
            models.setOnPreferenceChangeListener((preference, newValue) -> {
                models.setTitle(models.getEntries()[models.findIndexOfValue((String) newValue)]);
                if (newValue.toString().contains("davinci") && !newValue.toString().contains("curie")) {
                    maximum_length.setMax(4000);
                } else maximum_length.setMax(2048);
                return true;
            });
            models.setSummaryProvider((Preference.SummaryProvider<ListPreference>) preference -> {
                int index = preference.findIndexOfValue(preference.getValue());
                if (index >= 0 && modelList.size() > index) {
                    return modelList.get(index).getSummary();
                }
                return "未选择状态";
            });
        }
    }
}