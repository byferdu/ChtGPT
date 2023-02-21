package com.ferdu.chtgpt.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.ferdu.chtgpt.databinding.FragmentExampleDetailBinding;
import com.ferdu.chtgpt.models.data.Example2;
import com.ferdu.chtgpt.util.MyUtil;

import io.noties.markwon.Markwon;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExampleDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExampleDetailFragment extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public ExampleDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExampleDetailFragment.
     */

    public static ExampleDetailFragment newInstance(String param1, String param2) {
        ExampleDetailFragment fragment = new ExampleDetailFragment();
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
        FragmentExampleDetailBinding binding = FragmentExampleDetailBinding.inflate(inflater, container, false);
        binding.mToolbarExD.setNavigationOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });
        if (getArguments() != null) {
            Example2 exam_detail = getArguments().getParcelable("exam_detail");
            String[] types1 = getArguments().getStringArray("types");
            StringBuilder types = new StringBuilder();
            if (types1!=null && types1.length > 0) {
                for (String s : types1) {
                    types.append(" `").append(s).append("` &#160; ");
                }
            }
            String stop = "";
            if (exam_detail.getStop().length()>0) {
               stop= "| 停止序列 | `"+exam_detail.getStop()+"`|\n";
            }

            String content = "## **"+exam_detail.getTitle()+"**\n" +
                    "<br>\n" +
                    "\n" +
                    ""+types+" \n" +
                    "<br>\n" +
                    "<br>\n" +
                    "\n" +
                    "#### "+exam_detail.getSummary()+"\n" +
                    "<br>\n" +
                    "\n" +
                    "### **提示符**\n" +
                    "<br>\n" +
                    "\n" +
                    "~~~ \n"+exam_detail.getPrompt()+" \n ~~~" +
                    "\n" +
                    "<br>\n" +
                    "\n" +
                    "### **响应**\n" +
                    "<br>\n" +
                    "\n" +
                    "~~~ \n"+exam_detail.getResponse()+" \n ~~~" +
                    "\n" +
                    "<br>\n" +
                    "\n" +
                    "### **设置**\n" +
                    "<br>\n" +
                    "\n" +
                    "| 参数名      | 参数值 |\n" +
                    "| ----------- | ----------- |\n" +
                    "| 模型 | "+exam_detail.getModel()+"|\n" +
                    "| 最大词元 | "+exam_detail.getMax_tokens()+" |\n" +
                    "| 温度 | "+exam_detail.getTemperature()+" |\n" +
                    "| Top p | "+exam_detail.getTop_p()+" |\n" +
                    "| 频率惩罚 | "+exam_detail.getFrequency_penalty()+" |\n" +
                    "| 存在惩罚 | "+exam_detail.getPresence_penalty()+" |\n" +
                    stop;
            Markwon markDown = MyUtil.getMarkDown2(requireContext());
            markDown.setMarkdown(binding.exampleDetailText, content);

        }

        return binding.getRoot();
    }
}