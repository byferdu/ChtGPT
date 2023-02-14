package com.ferdu.chtgpt.ui.chat;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ferdu.chtgpt.R;
import com.ferdu.chtgpt.databinding.FragmentChatBinding;
import com.ferdu.chtgpt.models.ReqModel;
import com.ferdu.chtgpt.models.ResponseModel2;
import com.ferdu.chtgpt.models.data.ChatModel;
import com.ferdu.chtgpt.models.data.ChatThread;
import com.ferdu.chtgpt.ui.home.BackPressedListener;
import com.ferdu.chtgpt.ui.home.HistoryFragment;
import com.ferdu.chtgpt.util.MyUtil;
import com.ferdu.chtgpt.viewmodel.MyViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    private static final String TAG = "TransTag";
    private static final String TAG2 = "ProgressTag";
    private AppCompatImageButton button;
    private boolean isExecute = true;
    private boolean isExecute2 = true;
    StringBuilder savePrompts = new StringBuilder();
    public static TreadChanged treadChanged;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "exampleId";
    private static final String ARG_PARAM2 = "param2";
    private MyViewModel myViewModel;

    //private final Handler handler = new Handler(Looper.getMainLooper());
    private SharedPreferences sharedPreferences;
    private List<ChatModel> list;
    private String texts = "Something wrong! Please try again later!";
    int exampleId = -1;

    private int threadIdField = -1;
    private ProgressBar progressBar;
    private FragmentChatBinding binding;

    public List<ChatModel> getList() {
        return list;
    }

    public static BackPressedListener getBackPressedListener() {
        return backPressedListener;
    }


    static BackPressedListener backPressedListener;
    private AITextAdapter myAdapter;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(int param1) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        if (HistoryFragment.recyclerViewOnClickData == null) {
            HistoryFragment.recyclerViewOnClickData = new HashMap<>();
        }
        Log.d("QIGUAI", "Fragment onCreate: ");
        // HistoryFragment.recyclerViewOnClickData = new HashMap<>();
        exampleId = requireActivity().getIntent().getIntExtra("exampleId", -1);
        threadIdField = requireActivity().getIntent().getIntExtra("threadId", -1);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // FragmentDashboardBinding.inflate(inflater, container, false);
        binding = FragmentChatBinding.inflate(inflater, container, false);
        SharedPreferences sharedPreferences2 = PreferenceManager.getDefaultSharedPreferences(requireContext());
        //String name = sharedPreferences2.getString("mode", "");
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
       // HttpClient.setContext(requireContext());
        Handler handler = new Handler(Looper.getMainLooper());
        sharedPreferences = requireActivity().getSharedPreferences("key_1", MODE_PRIVATE);
        RecyclerView recyclerView = binding.recyclerView;
        progressBar = binding.progressBar;
        button = binding.button;
        Log.d("QIGUAI", "Fragment onCreateView: ");
        myAdapter = new AITextAdapter(binding.getRoot().getContext(), list);
        binding.startConversation.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_stop_24));
        binding.probText.setOrientation(LinearLayout.HORIZONTAL);
        myAdapter.setRecyclerView(recyclerView);
        binding.startConversation.setOnClickListener(v -> {
            if (binding.startConversation.getTag().toString().equals("play")) {
                binding.startConversation.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_stop_24));
                binding.startConversation.setTag("stop");
            } else {
                binding.startConversation.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_play_arrow_24));
                binding.startConversation.setTag("play");
                savePrompts.setLength(0);
            }
        });

        treadChanged = thread -> {
            DrawerLayout viewById = requireActivity().findViewById(R.id.chat_active);
            myViewModel.getChatModels(thread.getId()).observe(getViewLifecycleOwner(), chatModels -> {
                if (chatModels != null && viewById.isOpen()) {
                    threadIdField = thread.getId();
                    int size = list.size();
                    list.clear();
                    myAdapter.notifyItemRangeRemoved(0, size);
                    list.addAll(chatModels);
                    if (0 < list.size()) {
                        binding.textView.setVisibility(View.GONE);
                    }
                    myAdapter.notifyItemRangeChanged(0, list.size());

                }
            });
        };
        binding.chatMaterialToolbar.setNavigationOnClickListener(view -> {
            DrawerLayout viewById = requireActivity().findViewById(R.id.chat_active);
            viewById.open();
        });
        int exampleId = requireActivity().getIntent().getIntExtra("exampleId", -1);
        View contentView = LayoutInflater.from(requireContext()).inflate(R.layout.layout_tune, binding.getRoot(), false);
        Dialog dialog = new Play_Fragment().tunePaneInit(myViewModel, getViewLifecycleOwner(), contentView, sharedPreferences2, exampleId);
        binding.settingImage.setOnClickListener(view -> {
            if (!dialog.isShowing()) {
                dialog.show();
                MyUtil.tuneModel.setOpen(true);
            }
        });
        if (!dialog.isShowing()) {
            MyUtil.tuneModel.setOpen(false);
        }
        binding.addConnectTextView.setOnClickListener(v -> {
            List<Integer> removesList = new ArrayList<>();
            HistoryFragment.recyclerViewOnClickData.forEach((p, b) -> {
                if (b) {
                    removesList.add(p);
                }
            });
            Collections.sort(removesList);
            savePrompts.setLength(0);
            for (Integer integer : removesList) {
                ChatModel chatModel = list.get(integer);
                savePrompts.append("\nUser:").append(chatModel.getMeText()).append("\nChatGPT:")
                        .append(chatModel.getAIText()).append("\n");
            }
            Toast.makeText(requireContext(), "已添加" + removesList.size() + "个对话", Toast.LENGTH_SHORT).show();
            cancelSelected();
        });
        binding.deleteButton.setOnClickListener(v -> {
            if (HistoryFragment.recyclerViewOnClickData.size() < 1) {
                Toast.makeText(requireContext(), "删除前需选择条目", Toast.LENGTH_SHORT).show();
                return;
            }
            List<Integer> removesList = new ArrayList<>();
            HistoryFragment.recyclerViewOnClickData.forEach((p, b) -> {
                if (b) {
                    removesList.add(p);
                }
            });
            if (removesList.size() < 1) {
                Toast.makeText(requireContext(), "删除前需选择条目", Toast.LENGTH_SHORT).show();
                return;
            }
            Collections.sort(removesList);
            ArrayList<ChatModel> chatModels = new ArrayList<>(myAdapter.getList());
            for (int i = 0; i < removesList.size(); i++) {
                if (i == 0) {
                    myAdapter.removeNotes(removesList.get(i));
                } else {
                    myAdapter.removeNotes(removesList.get(i) - i);
                }
            }
            AtomicBoolean isUndo = new AtomicBoolean(false);
            Toast.makeText(requireContext(), "已添加" + removesList.size() + "个对话", Toast.LENGTH_SHORT).show();
            cancelSelected();
            Snackbar snackbar = Snackbar.make(binding.getRoot(), "删除了" + removesList.size() + "个对话", 4000)
                    .setAction("撤销", view12 -> {
                        isUndo.set(true);
                        list.clear();
                        list.addAll(chatModels);
                        myAdapter.notifyItemRangeChanged(0, chatModels.size());
                        HistoryFragment.recyclerViewOnClickData.clear();
                    });
            snackbar.show();
            handler.postDelayed(() -> {
                if (!snackbar.isShownOrQueued() && !isUndo.get() && removesList.size() > 0) {
                    for (int i = 0; i < removesList.size(); i++) {
                        ChatModel chatThread1 = chatModels.get(removesList.get(i));
                        myViewModel.deleteChatModel(chatThread1);
                        //
                    }
                    //  if (myAdapter.getItemCount()<1) myViewModel.deleteByTreadId(threadIdField);
                    // Toast.makeText(requireContext(), "删除成功！🤗", Toast.LENGTH_SHORT).show();
                }
                removesList.clear();

            }, 5000);

        });
        EditText editText = binding.probText.getEditText();
        if (sharedPreferences.getString("key", "").isEmpty()) {
            button.setEnabled(false);
        }
        if (threadIdField == -1 && list.size() == 0) {
            binding.textView.setVisibility(View.VISIBLE);
        }


        recyclerView.setAdapter(myAdapter);

        myViewModel.getChatModels(threadIdField).observe(getViewLifecycleOwner(), chatModels -> {
            if (chatModels != null) {
                if (list.size() == 0) {
                    list.addAll(chatModels);
                    myAdapter.notifyItemRangeChanged(0, list.size());
                }
            }
        });
        myViewModel.getChatModels(threadIdField).removeObservers(getViewLifecycleOwner());
        myAdapter.setItemLongClick((position) -> {
            binding.selectAllCheck.setVisibility(View.VISIBLE);
            binding.deleteButton.setVisibility(View.VISIBLE);
            binding.settingImage.setVisibility(View.GONE);
            binding.startConversation.setVisibility(View.GONE);
            binding.probText.setVisibility(View.GONE);
            binding.button.setVisibility(View.GONE);
            binding.addConnectTextView.setVisibility(View.VISIBLE);
        });
        myAdapter.setClickListener(((chatModel, adro, position) -> {
            if (AITextAdapter.isMultiSelectMode) {
                if (HistoryFragment.recyclerViewOnClickData.containsKey(position))
                    HistoryFragment.recyclerViewOnClickData.replace(position, adro);
                else HistoryFragment.recyclerViewOnClickData.put(position, adro);
                //    myAdapter.notifyItemChanged(position);
                //threadAdapter.notifyDataSetChanged();
            }
        }));
        String prompt = requireActivity().getIntent().getStringExtra("prompt");
        if (threadIdField == -1 && editText != null) {
            handler.postDelayed(() -> {
                if (prompt != null && !prompt.isEmpty()) {
                    editText.setText(prompt);
                    editText.setSelection(prompt.length());
                }
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }, 100);
        }

        button.setOnClickListener(view -> {
            //开始计时
            Log.d(TAG2, "onCreateView: Button Clicked");
            if (editText == null) {
                return;
            }
            isExecute = true;
            if (editText.getText().toString().trim().isEmpty()) {
                editText.setError("输入框不能为空！");
                return;
            }
           // button.setEnabled(false);
            showOrCancelProgress(progressBar, button, true);
            Log.d(TAG2, "onCreateView: progressBar.show");
            String s = editText.getText().toString();
            ReqModel reqModel = new ReqModel();
            String md = "";
            boolean md_out = sharedPreferences2.getBoolean("md_out", false);
            if (md_out) {
                md = " in md format";
            }
            reqModel.setPrompt("User:" + s + md + "\nChatGPT:");
            if (binding.startConversation.getTag().toString().equals("stop")) {
                reqModel.setPrompt(savePrompts.toString() + "User:" + s + md + "\nChatGPT:");
            }
            String model = sharedPreferences2.getString("model", "text-davinci-003");
            float temperature = sharedPreferences2.getFloat("temperature1", 0.9f);
            float top_p = sharedPreferences2.getFloat("top_p1", 0.5f);
            float frequency_penalty = sharedPreferences2.getFloat("frequency_penalty1", 0.1f);
            float presence_penalty = sharedPreferences2.getFloat("presence_penalty1", 0.5f);
            int maximum_length = sharedPreferences2.getInt("maximum_length", 10);
            reqModel.setModel(model);
            reqModel.setTemperature(temperature);
            reqModel.setMax_tokens(maximum_length);
            reqModel.setFrequency_penalty(frequency_penalty);
            reqModel.setPresence_penalty(presence_penalty);
            reqModel.setTop_p(top_p);
            String key = sharedPreferences.getString("key", "");

            Log.d(TAG2, "onCreateView: reqModel setup");
            SomeActions someActions = resModel -> {
                Log.d(TAG2, "someActions: someActions start");
                String text = resModel.getChoices().get(0).getText();
                editText.setText("");
                if (threadIdField == -1) {
                    myViewModel.insertChatThread(new ChatThread(s, text));
                    Log.d(TAG2, "someActions: insertChatThread");
                    handler.postDelayed(() -> myViewModel.selectChatThreadInsertId().observe(getViewLifecycleOwner(), i ->
                    {
                        if (i != null) {
                            threadIdField = i;
                            savePrompts(binding.startConversation.getTag().toString(), s, text);
                            insertChat(s, myAdapter, recyclerView, resModel, threadIdField, reqModel.getModel());
                            Log.d(TAG2, "someActions: insert chat \n\n\n");
                        }
                    }), 200);
                } else {
                    savePrompts(binding.startConversation.getTag().toString(), s, text);
                    insertChat(s, myAdapter, recyclerView, resModel, threadIdField, reqModel.getModel());
                }
                if (!MyUtil.tuneModel.getInjStart().isEmpty()) {
                    editText.setText(MyUtil.tuneModel.getInjStart());
                }
                binding.textView.setVisibility(View.GONE);
                Log.d(TAG2, "someActions: someActions setup");
            };
            if (exampleId != -1) {
                myViewModel.getExample(exampleId).observe(getViewLifecycleOwner(), example2 -> {
                    if (example2 != null) {
                        reqModel.setModel(example2.getModel());
                        reqModel.setTemperature(example2.getTemperature());
                        reqModel.setFrequency_penalty(example2.getFrequency_penalty());
                        reqModel.setPresence_penalty(example2.getFrequency_penalty());
                        reqModel.setMax_tokens(example2.getMax_tokens());
                        reqModel.setTop_p(example2.getTop_p());
                    }
                    // if (isOpenTune) {
                    onOpenTune(key, reqModel, someActions);
                    // }else getResponseText(key, reqModel, someActions);
                });
                Log.d(TAG2, "onCreateView: exampleId != -1");
            } else {
                //if (isOpenTune) {
                onOpenTune(key, reqModel, someActions);
                //  }else getResponseText(key, reqModel,someActions);
                Log.d(TAG, "onCreateView: After Trans Prompt 2:" + reqModel.getPrompt());
                Log.d(TAG2, "onCreateView: exampleId == -1");
            }

        });
        binding.selectAllCheck.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            AITextAdapter.selectAll = isChecked ? 1 : 2;
            HistoryFragment.recyclerViewOnClickData.clear();
            if (isChecked) {
                for (int i = 0; i < myAdapter.getItemCount(); i++) {
                    HistoryFragment.recyclerViewOnClickData.put(i, true);
                }
            }
            myAdapter.notifyDataSetChanged();
        }));


        String types = requireActivity().getIntent().getStringExtra("types");

        if (getArguments() != null && getArguments().getBoolean("backToChat", false)) {
            binding.exampleImage.setVisibility(View.VISIBLE);
            myViewModel.getExample(exampleId).observe(getViewLifecycleOwner(), example2 -> {
                if (example2 != null) {
                    binding.chatMaterialToolbar.setTitle(example2.getTitle());
                    if (editText != null && list.size() < 1) {
                        editText.setText(example2.getPrompt());
                        editText.setSelection(example2.getPrompt().length());
                        handler.postDelayed(() -> {
                            editText.requestFocus();
                            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(INPUT_METHOD_SERVICE);
                            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                        }, 100);   //0.1秒
                    }
                    myAdapter.notifyItemChanged(0);
                }
            });

            binding.exampleImage.setOnClickListener(view -> {
                myViewModel.getExample(exampleId).observe(getViewLifecycleOwner(), example2 -> {
                    if (example2 != null) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("exam_detail", example2);
                        bundle.putStringArray("types", types.split(","));
                        NavController navController = Navigation.findNavController(view);
                        navController.navigate(R.id.action_chatFragment_to_exampleDetailFragment, bundle);
                    }
                });
            });
        }
        return binding.getRoot();
    }

    private void savePrompts(String tag, String prompts, String response) {
        if (tag.equals("stop")) {
            savePrompts.append("User:").append(prompts).append("\nChatGPT:").append(response);
        } else savePrompts.delete(0, savePrompts.length());
    }

    private void insertChat(String s, AITextAdapter myAdapter, RecyclerView recyclerView, ResponseModel2 resModel, int treadId, String model) {
        if (isExecute) {
            String text = resModel.getChoices().get(0).getText();
            Log.d(TAG, "insertChat: "+resModel.getModel()+"\n\n"+model);
            ChatModel chatModel1 = new ChatModel(treadId, s, text
                    , resModel.getUsage().getPromptTokens(), resModel.getUsage().getCompletionTokens()
                    , resModel.getUsage().getTotalTokens(),resModel.getModel());

            myViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
                if (isExecute2) {
                    if (user != null) {
                        int total_tokens = resModel.getUsage().getTotalTokens();
                        int tokens = user.getTokens();
                        int token = tokens + total_tokens;
                        user.setTokens(token);
                        double xx = 0.0;
                        // Handle the case where model is not one of the expected values
                        if (model.contains("ada")) {
                            xx = 0.0004;
                        } else if (model.contains("davinci")) {
                            xx = 0.02;
                            if (model.length()>22) {
                                xx = 0.0;
                            }
                        } else if (model.contains("babbage")) {
                            xx = 0.0005;
                        } else if (model.contains("curie")) {
                            xx = 0.002;
                        }
                        float fark = sharedPreferences.getFloat("fark", 0.0f);
                        BigDecimal b = new BigDecimal(String.valueOf(fark));
                        double d = b.doubleValue();
                        double v = 18 - token / 1000.0 * xx + d;
                        user.setMoney(v);
                        if (fark != 0.0f) {
                            sharedPreferences.edit().putFloat("fark", 0.0f).apply();
                        }
                        myViewModel.updateUser(user);
                        isExecute2 = false;
                    }
                }
            });
            list.add(chatModel1);
            myViewModel.insertChatModel(chatModel1);
            myViewModel.selectChatThreadInsertId().removeObservers(this);
            myViewModel.getChatModels(treadId).removeObservers(this);
            myAdapter.notifyItemInserted(list.size() - 1);
            recyclerView.scrollToPosition(list.size() - 1);
            isExecute = false;
        }
        isExecute2 = true;
    }

    private void onOpenTune(String key, ReqModel reqModel, SomeActions someActions) {
        Log.d(TAG2, "onCreateView: openTune start");
        if (MyUtil.tuneModel.isOpen()) {
            reqModel.setModel(MyUtil.tuneModel.getModel());
            reqModel.setTemperature(MyUtil.tuneModel.getTemperature());
            reqModel.setMax_tokens(MyUtil.tuneModel.getMax_tokens());
            reqModel.setTop_p(MyUtil.tuneModel.getTop_p());
        }
        if (MyUtil.tuneModel.getStop() != null && !MyUtil.tuneModel.getStop().isEmpty()) {
            String stop1 = MyUtil.tuneModel.getStop();
            if (stop1.contains(",")) {
                reqModel.setStop(stop1.split(","));
            } else {
                reqModel.setStop(new String[]{stop1});
            }
        }
        Log.d(TAG, "onCreateView: Before Trans Prompt:" + reqModel.getPrompt());
        if (MyUtil.tuneModel.isTrans()) {
            AtomicReference<String> prompt = new AtomicReference<>(reqModel.getPrompt());
            myViewModel.getTrans(prompt.get(), 1).observe(getViewLifecycleOwner(), transModel -> {
                Log.d(TAG2, "onCreateView: getTrans start");
                if (transModel != null) {
                    Log.d(TAG2, "onCreateView: transModel != null  :" + transModel.getText());
                    reqModel.setPrompt(transModel.getText());
                    Log.d(TAG, "onCreateView: After Trans Prompt in:" + reqModel.getPrompt());
                    getResponseText(key, reqModel, someActions);

                }
                //Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
            });
            Log.d(TAG, "onCreateView: After Trans Prompt 1:" + reqModel.getPrompt());
        } else {
            getResponseText(key, reqModel, someActions);
        }
    }

    private void getResponseText(String key, ReqModel reqModel, SomeActions someActions) {
        Log.d(TAG2, "getResponseText: getResponseText start\n " + reqModel.toString());
        myViewModel.getTex(key, reqModel).observe(getViewLifecycleOwner(), resModel -> {
            Log.d(TAG2, "getResponseText: myViewModel.getTex" + resModel);
            if (resModel.getErrorMessage() == null) {
                Log.d(TAG2, "getResponseText: resModel != null");
                if (resModel.getErrorParent() != null) {
                    Toast.makeText(getContext(), resModel.getErrorParent().getError().getType(), Toast.LENGTH_SHORT).show();
                    showOrCancelProgress(progressBar, button, false);
                    return;
                }
                if (resModel.getChoices() != null) {
                    Log.d(TAG2, "getResponseText: resModel.choices != null");
                    if (MyUtil.tuneModel.getInjRestart() != null) {
                        texts = MyUtil.tuneModel.getInjRestart() + resModel.getChoices().get(0).getText();
                    } else {
                        texts = resModel.getChoices().get(0).getText();
                    }
                    Log.d(TAG2, "getResponseText: text:" + texts);
                    String temp = texts.length() >= 3 ? texts.substring(0, 2) : "";
                    if (temp.contains("\n")) {
                        texts = texts.trim();
                    }
                    resModel.getChoices().get(0).setText(texts);
                    if (MyUtil.tuneModel.isTrans()) {
                        myViewModel.getTrans(texts, 2).observe(getViewLifecycleOwner(), transModel -> {
                            if (transModel != null) {
                                Log.d(TAG2, "getResponseText: transModel != null");
                                texts = transModel.getText();
                                resModel.getChoices().get(0).setText(texts);
                                Log.d(TAG2, "getResponseText: Trans text in:" + texts);
                                Toast.makeText(getContext(), "翻译成功！🤗", Toast.LENGTH_SHORT).show();
                                someActions.Action(resModel);
                            }
                        });
                    } else {
                        Log.d(TAG2, "getResponseText: !MyUtil.tuneModel.isTrans()");
                        someActions.Action(resModel);
                    }

                } else texts = "Something wrong! Please try again later!";
            } else {
                Toast.makeText(requireContext(), resModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
            Log.d(TAG2, "getResponseText: After trans:" + texts);
            showOrCancelProgress(progressBar, button, false);
        });
        Log.d(TAG2, "getResponseText: After getResponseText:" + texts);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getArguments() != null && getArguments().getBoolean("backToChat", false)
        if (exampleId != -1 && getArguments() == null) {
            NavController navController = Navigation.findNavController(view);
            Bundle bundle = new Bundle();
            bundle.putInt("exampleId", requireActivity().getIntent().getIntExtra("exampleId", -1));
            navController.navigate(R.id.action_chatFragment_to_play_Fragment, bundle);
            exampleId = -1;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        myViewModel.selectChatThreadInsertId().removeObservers(getViewLifecycleOwner());
        myViewModel.selectChatThreadInsertId().removeObservers(requireActivity());
        if (ChatActivity.drawerLayout != null) {
            ChatActivity.drawerLayout.close();
        }
        AITextAdapter.isMultiSelectMode = false;
        // chatFragment = null;
        Log.d("QIGUAI", "Fragment onDestroyView: ");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        exampleId = -1;
        threadIdField = -1;
        // list = null;
        savePrompts.setLength(0);
        Log.d("QIGUAI", "Fragment onDestroy: ");
        HistoryFragment.recyclerViewOnClickData.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        backPressedListener = () -> {
            if (AITextAdapter.isMultiSelectMode && binding != null && myAdapter != null) {
                cancelSelected();
                return true;
            }
            return false;
        };
        Log.d("QIGUAI", "Fragment onResume: ");
    }


    @SuppressLint("NotifyDataSetChanged")
    private void cancelSelected() {
        binding.settingImage.setVisibility(View.VISIBLE);
        binding.startConversation.setVisibility(View.VISIBLE);
        binding.selectAllCheck.setVisibility(View.GONE);
        binding.deleteButton.setVisibility(View.GONE);
        binding.addConnectTextView.setVisibility(View.GONE);
        binding.probText.setVisibility(View.VISIBLE);
        binding.button.setVisibility(View.VISIBLE);
        AITextAdapter.isMultiSelectMode = false;
        AITextAdapter.selectAll = 0;
        HistoryFragment.recyclerViewOnClickData.clear();
        myAdapter.notifyDataSetChanged();
        Log.d("NOACTIVITY", "cancelSelected: ");
    }

    private void showOrCancelProgress(ProgressBar progressBar, AppCompatImageButton button,boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            button.setEnabled(false);
            button.setAlpha(0.4f);
        } else {
            button.setEnabled(true);
            progressBar.setVisibility(View.GONE);
            button.setAlpha(1f);
        }
    }

}