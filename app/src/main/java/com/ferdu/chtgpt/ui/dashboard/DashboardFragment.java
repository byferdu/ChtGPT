package com.ferdu.chtgpt.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ferdu.chtgpt.R;
import com.ferdu.chtgpt.databinding.FragmentDashboardBinding;
import com.ferdu.chtgpt.models.data.Example2;
import com.ferdu.chtgpt.models.data.Types;
import com.ferdu.chtgpt.ui.chat.ChatActivity;
import com.ferdu.chtgpt.util.MyUtil;
import com.ferdu.chtgpt.util.interfces.MyItemClickListener;
import com.ferdu.chtgpt.viewmodel.MyViewModel;

import java.util.ArrayList;
import java.util.List;


public class DashboardFragment extends Fragment implements MyItemClickListener<Example2> {

    private FragmentDashboardBinding binding;
    private List<Example2> example2s = new ArrayList<>();
    private MyViewModel dashboardViewModel1;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel1 = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(MyViewModel.class);
        ExampleAdapter exampleAdapter = new ExampleAdapter(this);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        binding.recyclerView2.setAdapter(exampleAdapter);
        View root = binding.getRoot();
        dashboardViewModel1.getExamples().observe(requireActivity()
                , exampleAdapter::submitList);
        dashboardViewModel1.getTypes().observe(requireActivity(), types -> {
            List<String> list = new ArrayList<>();
            list.add("全部");
            for (Types type : types) {
                list.add(type.type);
            }
            binding.appCompatSpinner.setAdapter(new ArrayAdapter<>(requireActivity(), R.layout.spinner_item, list));
        });
        binding.searchExample.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                dashboardViewModel1.getExOnQuery(s).observe(requireActivity(), example2s1 -> {
                    if (example2s1 != null) {
                       int itemCount = exampleAdapter.getItemCount();
                        example2s = example2s1;
                        if (example2s1.size() != itemCount) {
                            exampleAdapter.submitList(example2s1);
                        }
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                dashboardViewModel1.getExOnQuery(s).observe(requireActivity(), example2s1 -> {
                    int itemCount = exampleAdapter.getItemCount();
                    if (example2s1.size() != itemCount) {
                        exampleAdapter.submitList(example2s1);
                    }
                });
                return true;
            }
        });
        binding.appCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (binding.recyclerView2.getAdapter() != null) {
                    if (binding.appCompatSpinner.getSelectedItem().toString().equals("全部")
                            || binding.appCompatSpinner.getSelectedItemPosition() == 0) {
                        dashboardViewModel1.getExamples().observe(requireActivity()
                                , exampleAdapter::submitList);
                        return;
                    }
                    example2s.clear();

                    dashboardViewModel1.getExOnTypes(binding.appCompatSpinner.getSelectedItem().toString()).observe(
                            getViewLifecycleOwner(), example2s1 -> {
                        int itemCount = exampleAdapter.getItemCount();
                        if (example2s1.size() != itemCount) {
                            exampleAdapter.submitList(example2s1);
                            exampleAdapter.notifyItemRangeChanged(0, example2s1.size());
                            for (Example2 example2 : example2s1) {
                                Log.d("TAG222", "onItemSelected: "+example2.getTitle());
                            }
                        }
                    });
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                dashboardViewModel1.getExamples().observe(requireActivity()
                        , exampleAdapter::submitList);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onItemClicked(Example2 example2,boolean adr,int position) {
       // NavController navController = Navigation.findNavController(binding.getRoot());
        // Bundle bundle = new Bundle();
        // bundle.putInt("exampleId",example2.getId());
//        if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.dashboardFragment) {
//          navController.navigate(R.id.action_Afragment_to_Bfragment)
//        }
//          navController.navigate(R.id.action_dashboardFragment_to_play_Fragment,bundle);
        String key = MyUtil.getShared(requireContext()).getString("key", "");
        if (key != null && !key.isEmpty()) {
            Intent intent = new Intent(requireActivity(), ChatActivity.class);
            intent.putExtra("exampleId", example2.getId());
            dashboardViewModel1.getTypesOnEx(example2.getId()).observe(requireActivity(), types -> {
                StringBuilder v = new StringBuilder();
                if (types.size() > 0) {
                    for (int i = 0; i < types.size(); i++) {
                        v.append(types.get(i).getType());
                        if (i == types.size() - 1) {
                            break;
                        }
                        v.append(",");
                    }
                    intent.putExtra("types", v.toString());
                    startActivity(intent);
                }
            });
        } else {
            MyUtil.ShowSetKeyDialog(requireActivity(),binding.getRoot(), dashboardViewModel1,false);
        }
    }
}