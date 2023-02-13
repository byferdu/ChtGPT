package com.ferdu.chtgpt.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.ferdu.chtgpt.databinding.FragmentAddExampleBinding;
import com.ferdu.chtgpt.models.ReqModel;
import com.ferdu.chtgpt.models.data.Model;
import com.ferdu.chtgpt.ui.home.PromptModel;
import com.ferdu.chtgpt.util.MyUtil;
import com.ferdu.chtgpt.viewmodel.MyViewModel;

import java.util.Objects;

import cn.leancloud.LCObject;
import cn.leancloud.utils.StringUtil;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddExampleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddExampleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddExampleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddExampleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddExampleFragment newInstance(String param1, String param2) {
        AddExampleFragment fragment = new AddExampleFragment();
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
        MyViewModel myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        FragmentAddExampleBinding binding = FragmentAddExampleBinding.inflate(inflater, container, false);
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        if (getArguments() != null) {
            int insertType = getArguments().getInt("insertType", -1);

            switch (insertType) {
                case 0:
                    binding.textInputLayout.setHint("输入提示标题");
                    binding.textInputLayout2.setHint("输入提示");
                    binding.toolbar.setTitle("添加提示");
                    binding.checkBox.setVisibility(View.VISIBLE);
                    binding.explainText2.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    binding.textInputLayout.setHint("输入模型,如：text-davinci-003");
                    binding.textInputLayout2.setHint("输入概述");
                    binding.toolbar.setTitle("添加模型");
                    break;
                case 2:
                    binding.textInputLayout.setHint("输入密钥");
                    binding.textInputLayout2.setVisibility(View.GONE);
                    binding.toolbar.setTitle("添加密钥");
                    break;
                default:
                    Toast.makeText(requireContext(), "出错了", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(binding.getRoot()).navigateUp();
                    break;
            }
            binding.confrimBtn.setOnClickListener(v -> {
                String text = Objects.requireNonNull(binding.textInputLayout.getEditText()).getText().toString();
                String text1 = Objects.requireNonNull(binding.textInputLayout2.getEditText()).getText().toString();
                if (text.trim().isEmpty() || text1.trim().isEmpty()) {
                    binding.textInputLayout.setError("不能为空");
                    binding.textInputLayout2.setError("不能为空");
                    return;
                }
                switch (insertType) {
                    case 0:
                        if (binding.checkBox.isChecked()) {
                            // 构建对象
                            LCObject todo = new LCObject("Prompt");
                            todo.put("act", text);
                            todo.put("prompt", text1);
                            todo.saveInBackground().subscribe(new Observer<LCObject>() {
                                public void onSubscribe(Disposable disposable) {
                                }

                                public void onNext(LCObject todo) {
                                    // 成功保存之后，执行其他逻辑
                                    System.out.println("保存成功。objectId：" + todo.getObjectId());
                                }

                                public void onError(Throwable throwable) {
                                    // 异常处理
                                }

                                public void onComplete() {
                                }
                            });
                        }
                        myViewModel.insertPrompts(new PromptModel("63e1d" + StringUtil.getRandomString(7), text, text1));
                        Toast.makeText(requireContext(), "添加成功！", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(binding.getRoot()).navigateUp();
                        break;
                    case 1:
                        ReqModel reqModel = new ReqModel();
                        reqModel.setModel(text);
                        if (MyUtil.requestTest(myViewModel, "key", reqModel, requireActivity())) {
                            myViewModel.insertModel(new Model(text,text1));
                            Navigation.findNavController(binding.getRoot()).navigateUp();
                        }
                        break;
                    case 2:
                        binding.textInputLayout.setHint("输入密钥");
                        binding.textInputLayout2.setVisibility(View.GONE);
                        binding.toolbar.setTitle("addKey");
                        break;
                    default:
                        Toast.makeText(requireContext(), "出错了", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(binding.getRoot()).navigateUp();
                        break;
                }
            });
        }

        return binding.getRoot();
    }
}