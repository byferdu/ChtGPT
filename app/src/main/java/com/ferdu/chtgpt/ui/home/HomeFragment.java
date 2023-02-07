package com.ferdu.chtgpt.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ferdu.chtgpt.databinding.FragmentHomeBinding;
import com.ferdu.chtgpt.models.data.ChatThread;
import com.ferdu.chtgpt.ui.chat.ChatActivity;
import com.ferdu.chtgpt.util.MyUtil;
import com.ferdu.chtgpt.util.update.AppUtils;
import com.ferdu.chtgpt.util.update.AppVersionInfoBean;
import com.ferdu.chtgpt.util.update.UpdateApk;
import com.ferdu.chtgpt.util.update.impl.UpdateVersionShowDialog;
import com.ferdu.chtgpt.viewmodel.MyViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class HomeFragment extends Fragment implements BackPressedListener {

    private FragmentHomeBinding binding;
    public static Map<Integer, Boolean> recyclerViewOnClickData=new HashMap<>();
    Handler handler = new Handler(Looper.getMainLooper());

    public static HomeFragment instance;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        MyViewModel myViewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        SharedPreferences keyShared = MyUtil.getShared(requireContext());
        List<ChatThread> chatThreadsList = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        binding.recyclerThread.setLayoutManager(gridLayoutManager);

        SharedPreferences.Editor edit = keyShared.edit();
        if (AppUtils.judgmentDate(System.currentTimeMillis(), keyShared.getLong("versionDate", 1000))) {
            edit.putLong("versionDate", System.currentTimeMillis());
            edit.putBoolean("isCheckVersion", false);
            edit.apply();
        }
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
        }
        binding.recyclerThread.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                int column = position % 2;
                if (column == 0) { //第一列
                    outRect.left = 120 / 2;
                    outRect.right = 40 / 2;
                } else if (column == 1) { //第二列
                    outRect.left = 80 / 2;
                    outRect.right = 20 / 2;
                }
                outRect.top = 40;
                outRect.bottom = 40;
            }
        });
        ThreadAdapter threadAdapter = new ThreadAdapter(chatThreadsList);
        binding.recyclerThread.setAdapter(threadAdapter);
        myViewModel.getChatThreadsAll().observe(requireActivity(), chatThreads -> {
            if (chatThreads != null) {
                chatThreadsList.clear();
                chatThreadsList.addAll(chatThreads);
                threadAdapter.notifyDataSetChanged();
            }
        });
        threadAdapter.setClickListener((chatThread, adro, position) -> {
            if (!ThreadAdapter.isLongClick) {
                Intent intent = new Intent(requireActivity(), ChatActivity.class);
                int id = chatThread.getId();
                intent.putExtra("threadId", id);
                startActivity(intent);
            } else {
                if (recyclerViewOnClickData.containsKey(position)) recyclerViewOnClickData.replace(position, adro);
                else  recyclerViewOnClickData.put(position, adro);
                threadAdapter.notifyItemChanged(position);
                //threadAdapter.notifyDataSetChanged();
            }

        });
        threadAdapter.setOnItemLongClickListener((int pos) -> {
            binding.searchChats.setVisibility(View.GONE);
            binding.selectAllCheck.setVisibility(View.VISIBLE);
            binding.deleteButton.setVisibility(View.VISIBLE);
            binding.cancelText.setVisibility(View.VISIBLE);
        });
        binding.searchChats.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                myViewModel.getCTQuery(s).observe(getViewLifecycleOwner(), chatThreads -> {
                    if (chatThreads != null) {
                        int itemCount = threadAdapter.getItemCount();
                        if (itemCount != chatThreads.size()) {
                            chatThreadsList.clear();
                            chatThreadsList.addAll(chatThreads);
                            threadAdapter.notifyDataSetChanged();
                        }
                    }
                });
                return true;
            }
        });
        binding.selectAllCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            recyclerViewOnClickData.clear();
            if (isChecked) {
                ThreadAdapter.selectAll = 1;

                for (int i = 0; i < threadAdapter.getChatThreads().size(); i++) {
                    recyclerViewOnClickData.put(i, true);
                }
            } else {
                ThreadAdapter.selectAll = 2;
            }
            threadAdapter.notifyDataSetChanged();
        });
        binding.cancelText.setOnClickListener(v -> {
            binding.searchChats.setVisibility(View.VISIBLE);
            binding.selectAllCheck.setVisibility(View.GONE);
            binding.deleteButton.setVisibility(View.GONE);
            binding.cancelText.setVisibility(View.GONE);
            ThreadAdapter.isLongClick = false;
            recyclerViewOnClickData.clear();
            threadAdapter.notifyDataSetChanged();
        });

        binding.deleteButton.setOnClickListener(v -> {
            List<Integer> removesList = new ArrayList<>();
                recyclerViewOnClickData.forEach((p,b)->{
                    if (b) {
                        removesList.add(p);
                    }
                });
            Collections.sort(removesList);
            ArrayList<ChatThread> chatThreads = new ArrayList<>(threadAdapter.getChatThreads());

            for (int i = 0; i < removesList.size(); i++) {
                if (i == 0) {
                    threadAdapter.removeNotes(removesList.get(i));
                } else {
                    threadAdapter.removeNotes(removesList.get(i) - i);
                }
            }
            AtomicBoolean isUndo = new AtomicBoolean(false);
//            StringBuilder pos = new StringBuilder();
//            for (Integer integer : removesList) {
//                pos.append(integer).append(",");
//            }
            binding.cancelText.callOnClick();
            Snackbar snackbar = Snackbar.make(binding.getRoot(), "删除了" + removesList.size() + "个(含内容的)聊天室", 4000)
                    .setAction("撤销", view12 -> {
                        isUndo.set(true);
                        chatThreadsList.clear();
                        chatThreadsList.addAll(chatThreads);
                        threadAdapter.notifyItemRangeChanged(0, chatThreads.size());
                        recyclerViewOnClickData.clear();
                    });
            snackbar.show();
            handler.postDelayed(() -> {
                if (!snackbar.isShownOrQueued() && !isUndo.get() && removesList.size() > 0) {
                    for (int i = 0; i < removesList.size(); i++) {
                        ChatThread chatThread1 = chatThreads.get(removesList.get(i));
                        myViewModel.deleteChatThread(chatThread1);
                        int i1 = myViewModel.deleteByTreadId(chatThread1.getId());
                    }
                    Toast.makeText(requireContext(), "删除成功！🤗", Toast.LENGTH_SHORT).show();
                }
                removesList.clear();
                ThreadAdapter.selectAll = 2;
            }, 5000);
        });
        View root = binding.getRoot();
        binding.floatingActionButton.setOnClickListener(view -> {
            String key = keyShared.getString("key", "");
            if (key != null && !key.isEmpty()) {
                startActivity(new Intent(requireActivity(), ChatActivity.class));
            } else {
                MyUtil.ShowSetKeyDialog(requireActivity(), binding.getRoot(), myViewModel, false);
            }
        });
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        ThreadAdapter.isLongClick = false;
    }


    @Override
    public boolean handleBackPressed() {
        if (ThreadAdapter.isLongClick && binding != null) {
            binding.cancelText.callOnClick();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
        recyclerViewOnClickData.clear();
    }
}