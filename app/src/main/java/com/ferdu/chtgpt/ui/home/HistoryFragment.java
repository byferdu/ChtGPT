package com.ferdu.chtgpt.ui.home;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ferdu.chtgpt.R;
import com.ferdu.chtgpt.databinding.FragmentHistoryBinding;
import com.ferdu.chtgpt.models.data.ChatThread;
import com.ferdu.chtgpt.ui.MainActivity;
import com.ferdu.chtgpt.ui.chat.ChatActivity;
import com.ferdu.chtgpt.ui.chat.ChatFragment;
import com.ferdu.chtgpt.viewmodel.MyViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public class HistoryFragment extends Fragment implements BackPressedListener {

    private FragmentHistoryBinding binding;
    public static Map<Integer, Boolean> recyclerViewOnClickData=new HashMap<>();
    Handler handler = new Handler(Looper.getMainLooper());
    public static BackPressedListener pressedListener;

    private View viewById;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        MyViewModel myViewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);

        List<ChatThread> chatThreadsList = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        binding.recyclerThread.setLayoutManager(gridLayoutManager);


        if (getActivity() instanceof ChatActivity) {
            binding.recyclerThread.setLayoutManager(new LinearLayoutManager(requireContext()));
        }else {
            viewById = requireActivity().findViewById(R.id.nav_view);
            viewById.setVisibility(View.GONE);
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
                if (getActivity() instanceof ChatActivity) {
                    DrawerLayout drawer = requireActivity().findViewById(R.id.chat_active);
                    if (ChatFragment.treadChanged != null) {
                        if (drawer.isOpen()) {
                            drawer.close();
                            ChatFragment.treadChanged.onChanged(chatThread);
                            Log.d("NOACTIVITY", "onCreateView: "+getActivity());
                        }
                    }
                    return;
                }
                Bundle bundle = new Bundle();
                int id = chatThread.getId();
                bundle.putInt("threadId",id);
                if (isAdded()) {

                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_historyFragment_to_chatActivity, bundle);
                }

                Log.d("NOACTIVITY", "onCreateView: "+getActivity());
            } else {
                if (recyclerViewOnClickData.containsKey(position)) recyclerViewOnClickData.replace(position, adro);
                else  recyclerViewOnClickData.put(position, adro);
                threadAdapter.notifyItemChanged(position);
                //threadAdapter.notifyDataSetChanged();
            }
            Log.d("TAGClick", "onCreateView: onclik????");
        });
        threadAdapter.setOnItemLongClickListener((int pos) -> {
            binding.searchChats.setVisibility(View.GONE);
            binding.selectAllCheck.setVisibility(View.VISIBLE);
            binding.deleteButton.setVisibility(View.VISIBLE);
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
            if (ThreadAdapter.isLongClick) {
                binding.searchChats.setVisibility(View.VISIBLE);
                binding.selectAllCheck.setVisibility(View.GONE);
                binding.deleteButton.setVisibility(View.GONE);
                ThreadAdapter.isLongClick = false;
                recyclerViewOnClickData.clear();
                threadAdapter.notifyDataSetChanged();
            }else {
                if (requireActivity() instanceof ChatActivity) {
                    DrawerLayout drawer = requireActivity().findViewById(R.id.chat_active);
                    drawer.close();
                }else requireActivity().onBackPressed();
            }
        });

        binding.deleteButton.setOnClickListener(v -> {
            if (HistoryFragment.recyclerViewOnClickData.size() < 1) {
                Toast.makeText(requireContext(), "删除前需选择条目", Toast.LENGTH_SHORT).show();
                return;
            }
            List<Integer> removesList = new ArrayList<>();
                recyclerViewOnClickData.forEach((p,b)->{
                    if (b) {
                        removesList.add(p);
                    }
                });
            if (removesList.size() < 1) {
                Toast.makeText(requireContext(), "删除前需选择条目", Toast.LENGTH_SHORT).show();
                return;
            }
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
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "删除成功！🤗", Toast.LENGTH_SHORT).show();
                    }
                }
                removesList.clear();
                ThreadAdapter.selectAll = 2;
            }, 5000);
        });
        //           String key = keyShared.getString("key", "");
//            if (key != null && !key.isEmpty()) {
//                startActivity(new Intent(requireActivity(), ChatActivity.class));
//            } else {
//                MyUtil.ShowSetKeyDialog(requireActivity(), binding.getRoot(), myViewModel, false);
//            }
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pressedListener = this;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ThreadAdapter.isLongClick = false;
        if (requireActivity() instanceof MainActivity)
        viewById.setVisibility(View.VISIBLE);
    }




    @Override
    public boolean handleBackPressed() {
        if (ThreadAdapter.isLongClick && binding != null) {
            binding.cancelText.callOnClick();
            binding.searchChats.setVisibility(View.VISIBLE);
            binding.selectAllCheck.setVisibility(View.GONE);
            binding.deleteButton.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // binding = null;
        recyclerViewOnClickData.clear();
    }



}