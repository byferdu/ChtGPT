package com.ferdu.chtgpt.ui;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.ferdu.chtgpt.R;
import com.ferdu.chtgpt.util.ModeSummaryProvider;
import com.ferdu.chtgpt.util.MyUtil;
import com.ferdu.chtgpt.util.seek.FloatSeekBarPreference;
import com.ferdu.chtgpt.util.update.AppUtils;
import com.ferdu.chtgpt.util.update.AppVersionInfoBean;
import com.ferdu.chtgpt.util.update.UpdateApk;
import com.ferdu.chtgpt.util.update.impl.UpdateVersionShowDialog;
import com.ferdu.chtgpt.viewmodel.MyViewModel;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;
import java.util.Objects;

import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.LCUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        for (int i = 0; i < getResources().getStringArray(R.array.model_entries).length; i++) {
            getResources().getStringArray(R.array.model_entries);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        MaterialToolbar toolbar = findViewById(R.id.materialToolbar2);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            MyViewModel myViewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
            FloatSeekBarPreference brightnessPreference = findPreference("temperature1");
            Preference check_version = findPreference("check_version");
            EditTextPreference updateMoney = findPreference("user_money");
            Preference set_param = findPreference("set_param");
            Preference logout = findPreference("logout");
            SwitchPreference isTrans = findPreference("is_trans");
            SwitchPreference mdOut = findPreference("md_out");

            SharedPreferences shared = MyUtil.getShared(requireContext());
            if (shared.getString("key", "").isEmpty()) {
                Objects.requireNonNull(updateMoney).setEnabled(false);
                Objects.requireNonNull(set_param).setEnabled(false);
                Objects.requireNonNull(mdOut).setEnabled(false);
            }
            Objects.requireNonNull(isTrans).setEnabled(false);
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
            SharedPreferences.Editor edit = shared.edit();
            Objects.requireNonNull(updateMoney).setOnBindEditTextListener(
                    editText -> {
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER
                            | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);

                        myViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
                            if (user != null) {
                                String s = String.valueOf(user.getMoney());
                                editText.setText(s);
                                editText.setSelection(s.length());
                            }
                        });
                    });
            if (brightnessPreference != null) {
                brightnessPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                    //  MyUtil.ShowSetKeyDialog(requireActivity(),getView(), myViewModel,false);
                    return true;
                });
                brightnessPreference.setDefaultValue(0.5f);
            }
            if (logout != null) {
                if (LCUser.currentUser() != null) {
                    logout.setSummary(LCUser.getCurrentUser().getUsername());

                    logout.setVisible(true);
                }
                logout.setOnPreferenceClickListener(preference -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.AlertDialog)
                            .setTitle("确定吗？")
                            .setMessage("即将退出登录点击确认")
                            .setPositiveButton("确定", (dialog, which) -> {
                                LCUser.logOut();
                                logout.setVisible(false);
                            }).setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
                    builder.show();
                    return true;
                });
            }
            Preference signaturePreference = findPreference("signature");
            ListPreference listPreference = findPreference("mode");


            final LCObject[] lcObject = new LCObject[1];
            if (check_version != null) {
                check_version.setOnPreferenceClickListener(preference -> {
                    if (MyUtil.isNotFastClick(1000 * 3 * 60)) {
                        MyUtil.saveLastClick(System.currentTimeMillis());
                        LCQuery<LCObject> query = new LCQuery<>("UpdateApk");
                        query.findInBackground().subscribe(new Observer<List<LCObject>>() {
                            @Override
                            public void onSubscribe(Disposable disposable) {
                            }

                            @Override
                            public void onNext(List<LCObject> lcObjects) {
                                lcObject[0] = lcObjects.get(lcObjects.size() - 1);
                                String versionCode = Objects.requireNonNull(lcObject[0].getServerData().get(UpdateApk.VERSIONCODE)).toString();
                                String v2 = String.valueOf(AppUtils.getVersionCode(requireContext()));
                                if (1 == AppUtils.compareVersion(versionCode, v2)) {
                                    UpdateVersionShowDialog.show(requireActivity(), AppVersionInfoBean.parse(lcObject[0]));
                                    edit.putBoolean("isCheckVersion", false);
                                } else {
                                    //  SharedPreferences.Editor edit = keyShared.edit();
                                    Toast.makeText(getContext(), "当前是最新版本！🤗", Toast.LENGTH_SHORT).show();
                                    edit.putBoolean("isCheckVersion", true);
                                }
                                edit.apply();
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                Toast.makeText(getContext(), throwable.getMessage() + "😟", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                    } else if (MyUtil.isNotFastClick2(1000) && shared.getBoolean("isCheckVersion", false)) {
                        Toast.makeText(getContext(), "当前是最新版本！🤗", Toast.LENGTH_SHORT).show();
                    } else if (MyUtil.isNotFastClick2(1000)) {
                        UpdateVersionShowDialog.show(requireActivity(), AppVersionInfoBean.parse(lcObject[0]));
                    }
                    return true;
                });
            }
            if (listPreference != null) {
                listPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                    listPreference.setTitle(listPreference.getEntries()[listPreference.findIndexOfValue((String) newValue)]);
                    return true;
                });
                listPreference.setSummaryProvider(new ModeSummaryProvider());
            }
            String text = updateMoney.getText();
            updateMoney.setOnPreferenceChangeListener((preference, newValue) -> {
                final boolean[] isSet = {true};
                myViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
                    if (user != null && isSet[0]) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.AlertDialog)
                                .setTitle("确定吗？");
                        builder.setMessage("余额即将从" + user.getMoney() + "改为" + newValue.toString())
                                .setPositiveButton("确定", (dialog, which) -> {
                                    double v = Double.parseDouble(String.valueOf(newValue));
                                    double v1 = v - user.getMoney();
                                    if (v < 18.0d) {
                                        double token = (18-v-v1)/0.02*1000;
                                        user.setTokens((int) Math.round(token));
                                    }
                                    user.setMoney(v);
                                    edit.putFloat("fark", (float) v1);
                                    edit.apply();
                                    myViewModel.updateUser(user);
                                    dialog.dismiss();
                                    isSet[0] = false;
                                }).setNegativeButton("取消", (dialog, which) -> {
                                    SharedPreferences.Editor edit1 = defaultSharedPreferences.edit();
                                    edit1.putString("user_money", text);
                                    edit1.apply();
                                }).setOnCancelListener(dialog -> {
                                    SharedPreferences.Editor edit1 = defaultSharedPreferences.edit();
                                    edit1.putString("user_money", text);
                                    edit1.apply();
                                });
                        AlertDialog alertDialog = builder.create();
                        if (!alertDialog.isShowing()) {
                            alertDialog.show();
                        }
                    }
                });
                return true;
            });

            if (signaturePreference != null) {
                signaturePreference.setOnPreferenceClickListener(preference -> {
                    MyUtil.ShowSetKeyDialog(requireActivity(), getView(), myViewModel, false);
                    return true;
                });
            }

        }
    }
}