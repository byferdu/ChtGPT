package com.ferdu.chtgpt.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.ferdu.chtgpt.R;
import com.ferdu.chtgpt.databinding.AddPoupeWindowBinding;
import com.ferdu.chtgpt.databinding.FragmentHomeBinding;
import com.ferdu.chtgpt.ui.chat.ChatActivity;
import com.ferdu.chtgpt.util.MyUtil;
import com.ferdu.chtgpt.util.update.AppUtils;
import com.ferdu.chtgpt.util.update.AppVersionInfoBean;
import com.ferdu.chtgpt.util.update.UpdateApk;
import com.ferdu.chtgpt.util.update.impl.UpdateVersionShowDialog;
import com.ferdu.chtgpt.viewmodel.MyViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static boolean isFirst = true;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private boolean scrollingUp = false;
    private boolean isSearching = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static final List<PromptModel> lists = new ArrayList<>();
    private PromptsAdapter promptsAdapter;
    private com.ferdu.chtgpt.databinding.FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        MyViewModel myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        SharedPreferences keyShared = MyUtil.getShared(requireContext());
        SharedPreferences.Editor edit = keyShared.edit();
        binding.view.setOnClickListener(v -> {
            if (!keyShared.getString("key", "").isEmpty()) {
                startActivity(new Intent(requireActivity(), ChatActivity.class));
            } else
                MyUtil.ShowSetKeyDialog(requireActivity(), binding.getRoot(), myViewModel, false);
        });
        promptsAdapter = new PromptsAdapter(lists);

        if (AppUtils.judgmentDate(System.currentTimeMillis(), keyShared.getLong("versionDate", 1000))) {
            edit.putLong("versionDate", System.currentTimeMillis());
            edit.putBoolean("isCheckVersion", false);
            edit.apply();
        }
        if (lists.size() < 1)
            myViewModel.getPrompts().observe(getViewLifecycleOwner(), promptModels -> {
                if (promptModels != null) {
                    lists.addAll(promptModels);
                    if (isFirst) {
                        Collections.shuffle(lists);
                        isFirst = false;
                    }
                    promptsAdapter.updateData(lists, false);
                }
            });
        else promptsAdapter.updateData(lists, false);

        // binding.promptRecycler.setItemAnimator(new DefaultItemAnimator());
        //设置添加、移除时间为1s
        //Objects.requireNonNull(binding.promptRecycler.getItemAnimator()).setAddDuration(650);
        //  binding.promptRecycler.getItemAnimator().setRemoveDuration(650);
        binding.gideCard.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_historyFragment));
        if (!keyShared.getBoolean("isCheckVersion", false)) {
            LCQuery<LCObject> query = new LCQuery<>("UpdateApk");
            query.findInBackground().subscribe(new Observer<List<LCObject>>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                }

                @Override
                public void onNext(List<LCObject> lcObjects) {
                    LCObject lcObject = lcObjects.get(lcObjects.size() - 1);
                    String versionCode = Objects.requireNonNull(lcObject.getServerData().get(UpdateApk.VERSIONCODE)).toString();
                    String v2 = String.valueOf(AppUtils.getVersionCode(requireContext()));
                    if (1 == AppUtils.compareVersion(versionCode, v2)) {
                        UpdateVersionShowDialog.show(requireActivity(), AppVersionInfoBean.parse(lcObject));
                        edit.putBoolean("isCheckVersion", false);
                    } else {
                        edit.putBoolean("isCheckVersion", true);
                    }
                    edit.apply();
                }

                @Override
                public void onError(Throwable throwable) {
                    Toast.makeText(requireContext(), throwable.getMessage() + "😟", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onComplete() {

                }
            });
            Date date = new Date();
            date.setTime(keyShared.getLong("versionDate", 1000));
            LCQuery<LCObject> query2 = new LCQuery<>("Prompt").limit(200).whereGreaterThan("updatedAt", date);
            query2.findInBackground().subscribe(new Observer<List<LCObject>>() {
                @Override
                public void onSubscribe(Disposable d) {
                }
                @Override
                public void onNext(List<LCObject> lcObjects) {
                    List<PromptModel> promptModels = new ArrayList<>();

                    for (int i = 0; i < lcObjects.size(); i++) {
                        promptModels.add(new PromptModel(lcObjects.get(i).getObjectId(), lcObjects.get(i).get("act").toString()
                                , lcObjects.get(i).get("prompt").toString()));
                        Log.d("TAGupdate", "onNext: " + lcObjects.get(i).getUpdatedAt());
                        if (lcObjects.get(i).getUpdatedAt().after(date)) {
                            if (myViewModel.getAtPrompts(lcObjects.get(i).getObjectId()) != null) {
                                myViewModel.updatePrompts(promptModels.get(i));
                            } else{
                                lists.add(promptModels.get(i));
                                myViewModel.insertPrompts(promptModels.get(i));}
                        }
                    }

                   // lists.addAll(promptModels);
                    if (isFirst) {
                        Collections.shuffle(lists);
                        // generateRandomData();
                        isFirst=false;
                    }
                    promptsAdapter.updateData(lists, false);
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
        }
        binding.keys.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse("https://m.tb.cn/h.Umrrw8k?tk=2Ss1d6v6FPy");
            intent.setData(content_url);
            startActivity(intent);
        });
        binding.shareCard.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse("https://sharegpt.com/explore");
            intent.setData(content_url);
            startActivity(intent);
        });
        binding.searchPrompt.setOnSearchClickListener(v -> {
            binding.flow.setVisibility(View.GONE);
            binding.startText.setVisibility(View.GONE);
            binding.view.setVisibility(View.GONE);
            isSearching = true;
            binding.addIcon.setVisibility(View.INVISIBLE);
            binding.addIcon.setEnabled(false);
            binding.searchPrompt.setBackgroundResource(R.drawable.edit_back);

            // promptsAdapter.updateData(list);
            requireActivity().findViewById(R.id.nav_view).setVisibility(View.GONE);
        });
        binding.addIcon.setOnClickListener(this::initPopup);
        binding.searchPrompt.setOnCloseListener(() -> {
            binding.flow.setVisibility(View.VISIBLE);
            binding.startText.setVisibility(View.VISIBLE);
            binding.view.setVisibility(View.VISIBLE);
            requireActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
            binding.searchPrompt.setBackgroundColor(Color.TRANSPARENT);
            isSearching = false;
            binding.addIcon.setEnabled(true);
            binding.addIcon.setVisibility(View.VISIBLE);
            promptsAdapter.updateData(lists, false);
            binding.searchPrompt.setQuery("", false);
            binding.searchPrompt.clearFocus();
            // 隐藏键盘
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.searchPrompt.getWindowToken(), 0);
            //promptsAdapter.updateData(generateRandomData());
            return false;
        });
        binding.searchPrompt.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {

                    promptsAdapter.updateData(lists, false);
                    return true;
                }
                myViewModel.getPromptSearch(newText).observe(getViewLifecycleOwner(), promptModels -> {
                    if (promptModels != null) {
                        // promptsAdapter.updateData(list);
                        promptsAdapter.updateData(promptModels, true);
                    }
                });
                return true;
            }
        });
        binding.promptRecycler.setAdapter(promptsAdapter);

        binding.nestedScroll.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY && !scrollingUp) {
                // Scrolling up
                scrollingUp = true;
                //  Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_left);
                binding.view.animate()
                        .translationX(-(float) binding.view.getWidth())
                        .setDuration(200)
                        .start();
                binding.startText.animate()
                        .translationX(-(float) binding.view.getWidth())
                        .setDuration(200)
                        .start();
                //   binding.view.setVisibility(View.GONE);
            } else if (scrollY < oldScrollY && scrollingUp) {
                // Scrolling down
                scrollingUp = false;
                //  Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
                binding.startText.animate()
                        .translationX(0f)
                        .setDuration(200)
                        .start();
                binding.view.animate()
                        .translationX(0f)
                        .setDuration(200)
                        .start();
                // binding.view.setVisibility(View.VISIBLE);
            }
        });

        promptsAdapter.setClickListener((model, adro, position) -> {
            if (!keyShared.getString("key", "").isEmpty()) {
                Intent intent = new Intent(requireActivity(), ChatActivity.class);
                intent.putExtra("prompt", model.getPrompt());
                startActivity(intent);
            } else
                MyUtil.ShowSetKeyDialog(requireActivity(), binding.getRoot(), myViewModel, false);
        });
        return binding.getRoot();
    }

    private void initPopup(View parentView) {
        View contentView = LayoutInflater.from(requireContext()).inflate(R.layout.add_poupe_window, null,false);
        PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //  popupWindow.showAtLocation(parentView, , (int) (parentView.getX()), (int) (parentView.getY()+parentView.getHeight()*2));
        popupWindow.showAsDropDown(parentView);
        AddPoupeWindowBinding binding1 = AddPoupeWindowBinding.bind(contentView);
        binding1.tvAddTip.setOnClickListener(v -> {
            // 添加提示的点击事件
            Bundle bundle = new Bundle();
            bundle.putInt("insertType", 0);
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_home_to_addExampleFragment, bundle);
            popupWindow.dismiss();
        });

        binding1.tvAddModel.setOnClickListener(v -> {
            // 添加模型的点击事件
            Bundle bundle = new Bundle();
            bundle.putInt("insertType", 1);
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_home_to_addExampleFragment, bundle);


            popupWindow.dismiss();
        });
        binding1.tvAddKey.setOnClickListener(v -> {
            // 添加密钥的点击事件
            Bundle bundle = new Bundle();
            bundle.putInt("insertType", 2);
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_home_to_addExampleFragment, bundle);
            popupWindow.dismiss();
        });
    }

    private Date getDateWithDateString(String dateString) throws ParseException {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA);
        return df.parse(dateString);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}