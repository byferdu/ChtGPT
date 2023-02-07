package com.ferdu.chtgpt.ui.chat;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import com.ferdu.chtgpt.R;
import com.ferdu.chtgpt.databinding.FragmentPlayBinding;
import com.ferdu.chtgpt.models.ReqModel;
import com.ferdu.chtgpt.models.TuneModel;
import com.ferdu.chtgpt.ui.SettingsActivity;
import com.ferdu.chtgpt.viewmodel.MyViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Play_Fragment extends Fragment {
    private final TuneModel tuneModels = new TuneModel();
    private SharedPreferences sharedPreferences;
    private boolean isExecute = true;
    private String texts;
    private boolean isOpenTune = false;

    public Play_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // default to enabled
        OnBackPressedCallback callback = new OnBackPressedCallback(
                true // default to enabled
        ) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(
                this, callback);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentPlayBinding binding = FragmentPlayBinding.inflate(inflater, container, false);
        binding.materialToolbar.setNavigationOnClickListener(view -> requireActivity().finish());

        addLayoutListener(binding.getRoot(), binding.constraintLayout);


        SharedPreferences sharedPreferences2 = PreferenceManager.getDefaultSharedPreferences(requireContext());

        sharedPreferences = requireActivity().getSharedPreferences("key_1", MODE_PRIVATE);
        MyViewModel myViewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        int exampleId = -1;
        if (getArguments() != null) {
            exampleId = getArguments().getInt("exampleId", -1);
        }

        int finalExampleId = exampleId;
        View contentView = LayoutInflater.from(requireContext()).inflate(R.layout.layout_tune, binding.getRoot(), false);

        Dialog dialog = tunePaneInit(myViewModel, getViewLifecycleOwner(), contentView, sharedPreferences2, exampleId);
        binding.settingImage.setOnClickListener(view -> {
            if (!dialog.isShowing()) {
                dialog.show();
                isOpenTune = true;
            }
        });
        myViewModel.getExample(finalExampleId).observe(getViewLifecycleOwner(), example2 -> {
            if (example2 != null&&binding.inpuLayout.getEditText()!=null) {
                binding.inpuLayout.getEditText().setText(example2.getPrompt());
                if (example2.getInjStart()!=null&&!example2.getInjStart().isEmpty()) {
                    binding.injStartText.setVisibility(View.VISIBLE);
                    binding.injStartText.setOnClickListener(v -> {
                        binding.inpuLayout.getEditText().append(example2.getInjStart());
                    });
                }
                if (example2.getInjRestart() != null && !example2.getInjRestart().isEmpty()) {
                    binding.injEndText.setVisibility(View.VISIBLE);
                    binding.injEndText.setOnClickListener(v -> {
                        binding.inpuLayout.getEditText().append(example2.getInjRestart());
                    });
                }
            }
        });
        binding.imageView5.setOnClickListener(view1 -> {
            // requireActivity().onBackPressed();
            Bundle bundle = new Bundle();
            bundle.putBoolean("backToChat", true);
            NavController navController = Navigation.findNavController(view1);
            navController.navigate(R.id.action_play_Fragment_to_chatFragment, bundle);
        });
        View.OnClickListener listener = view -> {
            //开始计时
            if (binding.inpuLayout.getEditText() == null) {
                return;
            }
            isExecute = true;
            EditText editText = binding.inpuLayout.getEditText();

            if (editText.getText().toString().trim().isEmpty()) {
                editText.setError("输入框不能为空！");
                return;
            }
            //progressBar.setVisibility(View.VISIBLE);
            binding.buttonSubmit.setEnabled(false);
            String s = editText.getText().toString();

            ReqModel reqModel = new ReqModel();

            reqModel.setPrompt(s);

            String key = sharedPreferences.getString("key", "");

            myViewModel.getExample(finalExampleId).observe(requireActivity(), example2 -> {
                if (example2 != null) {
                    reqModel.setModel(example2.getModel());
                    reqModel.setTemperature(example2.getTemperature());
                    reqModel.setFrequency_penalty(example2.getFrequency_penalty());
                    reqModel.setPresence_penalty(example2.getFrequency_penalty());
                    reqModel.setMax_tokens(example2.getMax_tokens());
                    reqModel.setTop_p(example2.getTop_p());
                    if (example2.getStop() != null && !example2.getStop().isEmpty()) {
                        String stop = example2.getStop();
                        if (stop.contains(",")) {
                            reqModel.setStop(stop.split(","));
                        } else {
                            reqModel.setStop(new String[]{stop});
                        }
                    }
                    if (isOpenTune) {
                        reqModel.setModel(tuneModels.getModel());
                        reqModel.setTemperature(tuneModels.getTemperature());
                        reqModel.setMax_tokens(tuneModels.getMax_tokens());
                        reqModel.setTop_p(tuneModels.getTop_p());
                        if (tuneModels.getStop() != null && !tuneModels.getStop().isEmpty()) {
                            String stop1 = tuneModels.getStop();
                            if (stop1.contains(",")) {
                                reqModel.setStop(stop1.split(","));
                            } else {
                                reqModel.setStop(new String[]{stop1});
                            }
                        }
                    }

                    String model1 = reqModel.getModel();
                    System.out.println(model1);
                    myViewModel.getTex(key, reqModel).observe(getViewLifecycleOwner(), resModel -> {
                        if (resModel.getErrorMessage() == null) {
                            if (resModel.getError() != null) {
                                Toast.makeText(getContext(), resModel.getError().getError().getType(), Toast.LENGTH_SHORT).show();
                                binding.buttonSubmit.setEnabled(true);
                                return;
                            }
                            if (resModel.getChoices() != null) {
                                //
                                texts = resModel.getChoices().get(0).getText();
                                // String string = binding.inpuLayout.getEditText().getText().toString();
                                if (reqModel.getStop() != null) {
                                    texts += "\n\n" + example2.getInjRestart();
                                }
                                //    Log.d("TAG2", "onCreate: " + texts);
                                binding.editTextInInputLayout.getEditableText().append(texts);
                            } else {
                                texts = "Something wrong! Please try again later!";
                                Toast.makeText(requireContext(), texts, Toast.LENGTH_SHORT).show();
                            }
                            //     list.add(new PrompModel("", texts, resModel.getUsage()));
                            myViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
                                if (isExecute) {
                                    if (user != null) {
                                        int token = user.getTokens() + resModel.getUsage().getTotalTokens();
                                        user.setTokens(token);
                                        double xx = 0.0;
                                        // Handle the case where model is not one of the expected values
                                        if (model1.contains("ada")) {
                                            xx = 0.0004;
                                        } else if (model1.contains("davinci")) {
                                            xx = 0.02;
                                        } else if (model1.contains("babbage")) {
                                            xx = 0.0005;
                                        } else if (model1.contains("curie")) {
                                            xx = 0.002;
                                        }
                                        float fark = sharedPreferences.getFloat("fark", 0.0f);
                                        BigDecimal b = new BigDecimal(String.valueOf(fark));
                                        double d = b.doubleValue();
                                        user.setMoney(18 - token / 1000.0 * xx + d);
                                        if (fark != 0.0f) {
                                            sharedPreferences.edit().putFloat("fark", 0.0f).apply();
                                        }
                                        myViewModel.updateUser(user);
                                        isExecute = false;
                                    }
                                }
                            });
                            editText.setSelection(editText.getText().length());
                        } else {
                            Toast.makeText(requireContext(), resModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                        binding.buttonSubmit.setEnabled(true);
                    });
                }
            });
        };

        binding.buttonSubmit.setOnClickListener(listener);
        String types = requireActivity().getIntent().getStringExtra("types");
        if (exampleId != -1) {
            binding.exampleImage.setVisibility(View.VISIBLE);
            binding.exampleImage.setOnClickListener(view -> myViewModel.getExample(finalExampleId).observe(getViewLifecycleOwner(), example2 -> {
                if (example2 != null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("exam_detail", example2);
                    bundle.putStringArray("types", types.split(","));
                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.action_play_Fragment_to_exampleDetailFragment, bundle);
                }
            }));
        }


        return binding.getRoot();
    }


    private static int getSelectPosition(List<String> strings, String select) {
        for (int i = 0; i < strings.size(); i++) {
            if (select.equals(strings.get(i))) {
                return i;
            }
        }
        return 0;
    }

    /**
     * 设置键盘不遮挡按钮
     *
     * @param main:根布局
     * @param scroll   需要显示的最下方View
     */
    public static void addLayoutListener(final View main, final View scroll) {
        main.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect rect = new Rect();
            main.getWindowVisibleDisplayFrame(rect);
            int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;
            if (mainInvisibleHeight > 100) {
                int[] location = new int[2];
                scroll.getLocationInWindow(location);
                int srollHeight = (location[1] + scroll.getHeight()) - rect.bottom;
                if (isKeyboardShown(main)) {
                    main.scrollTo(0, srollHeight);
                } else {
                    main.scrollTo(0, 0);
                }
            } else {
                main.scrollTo(0, 0);
            }
        });
    }

    public Dialog tunePaneInit(MyViewModel myViewModel, LifecycleOwner lifecycleOwner, View contentView
            , SharedPreferences sharedPreferences2, int exampleId) {
        Context context = contentView.getContext();
        Dialog bottomDialog = new Dialog(context, R.style.TopSheet);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels - contentView.getWidth();
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.TOP);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        Button cancelButton = contentView.findViewById(R.id.cancelButton);
        Button confirmsBtn = contentView.findViewById(R.id.confrimButton);
        ImageView setImage = contentView.findViewById(R.id.imageSetting);
        AppCompatSpinner spinner = contentView.findViewById(R.id.spinner2);
        SeekBar topPSeek = contentView.findViewById(R.id.topPSeek);
        SeekBar maxTokenSeek = contentView.findViewById(R.id.maxTokenSeek);
        SeekBar temperatureSeek = contentView.findViewById(R.id.tempratorSeek);
        TextInputEditText stopSquEdittext = contentView.findViewById(R.id.stopSquEdittext);
        TextInputEditText injectFront = contentView.findViewById(R.id.injectFront);
        TextInputEditText injectBack = contentView.findViewById(R.id.injectBack);
        SwitchCompat isTranslateSwitch = contentView.findViewById(R.id.isTranslateSwitch);
        TextView proTextMsx = contentView.findViewById(R.id.progTextMsx);
        TextView proTextTemp = contentView.findViewById(R.id.progTextTemp);
        TextView proTextTopP = contentView.findViewById(R.id.progTextTopP);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            maxTokenSeek.setMin(5);
        }

        SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String format = new DecimalFormat("###,###,###.##").format(progress * 0.01f);
                switch ((String) seekBar.getTag()) {
                    case "temp":
                        proTextTemp.setText(format);
                        break;
                    case "top":
                        proTextTopP.setText(format);
                        break;
                    case "max":
                        proTextMsx.setText(String.valueOf(progress));
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        topPSeek.setOnSeekBarChangeListener(onSeekBarChangeListener);
        maxTokenSeek.setOnSeekBarChangeListener(onSeekBarChangeListener);
        temperatureSeek.setOnSeekBarChangeListener(onSeekBarChangeListener);
        boolean isTrans = sharedPreferences2.getBoolean("is_trans", false);
        String model = sharedPreferences2.getString("model", "text-davinci-003");
        float temperature = sharedPreferences2.getFloat("temperature1", 0.9f);
        float top_p = sharedPreferences2.getFloat("top_p1", 0.4f);
        int maximum_length = sharedPreferences2.getInt("maximum_length", 10);
        String stops = sharedPreferences2.getString("stop", "");
        List<String> strings=Arrays.asList("text-chat-davinci-002-20230126","text-davinci-003", "text-davinci-001", "text-davinci-002", "code-davinci-002", "code-cushman-001", "text-curie-001", "text-babbage-001", "text-ada-001", "davinci", "ada", "curie", "babbage");
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(context
                , android.R.layout.simple_list_item_1
                , strings);
        spinner.setAdapter(stringArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (strings.get(position).contains("davinci")) {
                    maxTokenSeek.setMax(4000);
                }else maxTokenSeek.setMax(2048);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        myViewModel.getExample(exampleId).observe(lifecycleOwner, example2 -> {
            String mo = model;
            String stop = stops;
            String injStart = "";
            String injRestart = "";
            float temp = temperature;
            float tp = top_p;
            int maxt = maximum_length;
            if (example2 != null) {
                temp = example2.getTemperature();
                tp = example2.getTop_p();
                mo = example2.getModel();
                maxt = example2.getMax_tokens();
                stop = example2.getStop();
                injStart = example2.getInjStart();
                injRestart = example2.getInjRestart();
            }
            spinner.setSelection(getSelectPosition(strings, mo));
            temperatureSeek.setProgress((int) (temp * 100));
            topPSeek.setProgress((int) (tp * 100));
            maxTokenSeek.setProgress(maxt);
            stopSquEdittext.setText(stop);
            injectFront.setText(injStart);
            injectBack.setText(injRestart);
            isTranslateSwitch.setChecked(isTrans);
        });
        confirmsBtn.setOnClickListener(v1 -> {
            tuneModels.setModel(spinner.getSelectedItem().toString());
            tuneModels.setTemperature(temperatureSeek.getProgress() * 0.01f);
            tuneModels.setTop_p(topPSeek.getProgress() * 0.01f);
            tuneModels.setMax_tokens(maxTokenSeek.getProgress());
            tuneModels.setStop(String.valueOf(stopSquEdittext.getText()));
            tuneModels.setInjStart(String.valueOf(injectFront.getText()));
            tuneModels.setInjRestart(String.valueOf(injectBack.getText()));
            tuneModels.setTrans(isTranslateSwitch.isChecked());
            bottomDialog.dismiss();
        });
        isTranslateSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor edit = sharedPreferences2.edit();
            edit.putBoolean("is_trans", isChecked);
            edit.apply();
        });
        setImage.setOnClickListener(v1 -> {
            context.startActivity(new Intent(context, SettingsActivity.class));
            bottomDialog.dismiss();
        });
        cancelButton.setOnClickListener(view1 -> bottomDialog.dismiss());
        return bottomDialog;
    }

    /**
     * 判断软键盘是否弹起
     */
    public static boolean isKeyboardShown(View rootView) {
        try {
            final int softKeyboardHeight = 120;
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
            int heightDiff = rootView.getBottom() - r.bottom;
            return heightDiff > softKeyboardHeight * dm.density;
        } catch (Exception e) {
            return false;
        }
    }
}