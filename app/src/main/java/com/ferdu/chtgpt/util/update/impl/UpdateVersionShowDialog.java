package com.ferdu.chtgpt.util.update.impl;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.ferdu.chtgpt.R;
import com.ferdu.chtgpt.util.update.AppUpdater;
import com.ferdu.chtgpt.util.update.AppUtils;
import com.ferdu.chtgpt.util.update.AppVersionInfoBean;
import com.ferdu.chtgpt.util.update.IDownloadCallback;

import java.io.File;
import java.util.Objects;

public class UpdateVersionShowDialog extends DialogFragment {
    private static final String TAG = "UpdateVersionShowDialog";

    private static final String KEY_APP_VERSION_INFO_BEAN = "app_version_info_bean";

    /**
     * 版本更新信息bean，由show方法传入
     */
    private AppVersionInfoBean appVersionInfoBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            appVersionInfoBean = (AppVersionInfoBean) arguments.getSerializable(KEY_APP_VERSION_INFO_BEAN);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_update_app_version, container, false);
        Objects.requireNonNull(getDialog()).setCanceledOnTouchOutside(false);
        setCancelable(false);
        bindView(view);
        return view;
    }

    private void bindView(View view) {
        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvContent = view.findViewById(R.id.tv_content);
        final TextView tvUpdate = view.findViewById(R.id.tv_update);
        final TextView tvCancel = view.findViewById(R.id.tv_cancel);

        tvTitle.setText(appVersionInfoBean.getTitle());
        tvContent.setText(appVersionInfoBean.getContent());
        tvCancel.setOnClickListener(v -> {
//            if (getContext() instanceof MainActivity) {
//                System.exit(0);
//            }else
                dismiss();
        });
        tvUpdate.setOnClickListener(v -> {
            v.setEnabled(false);

            //安装包的下载地址,选择getCacheDir路径，可以避免存储权限的处理
            final File targetFile = new File(requireActivity().getCacheDir(), "target.apk");
            AppUpdater.getInstance().getINetManager().download(appVersionInfoBean.getUrl(), targetFile, new IDownloadCallback() {
                @Override
                public void onSuccess(File apkFile) {
                    v.setEnabled(true);
                    dismiss();
                    //下载成功
                    Log.d(TAG, "success = " + apkFile.getAbsolutePath());


                    String fileMd5 = AppUtils.getFileMd5(targetFile);
                    Log.d(TAG, "md5 = " + fileMd5);

                    if (fileMd5 != null && fileMd5.equals(appVersionInfoBean.getMd5())) {
                        //校验成功，安装
                        Toast.makeText(getActivity(), "开始安装😊", Toast.LENGTH_SHORT).show();

                        AppUtils.installApk(getActivity(), apkFile);
                    } else {
                        Toast.makeText(getActivity(), "md5检测失败😟", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void progress(int progress) {
                    Log.d(TAG, "progress = " + progress);

                    tvUpdate.setText(progress + "%");
                }

                @Override
                public void onFailure(Throwable throwable) {
                    v.setEnabled(true);

                    throwable.printStackTrace();
                    Toast.makeText(getActivity(), "文件下载失败🥹", Toast.LENGTH_SHORT).show();
                }
            }, UpdateVersionShowDialog.this);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Objects.requireNonNull(getDialog()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        Log.d("tag", "onDismiss: ");
        AppUpdater.getInstance().getINetManager().cancel(this);
    }

    public static void show(FragmentActivity fragmentActivity, AppVersionInfoBean appVersionInfoBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_APP_VERSION_INFO_BEAN, appVersionInfoBean);

        UpdateVersionShowDialog updateVersionShowDialog = new UpdateVersionShowDialog();

        updateVersionShowDialog.setArguments(bundle);

        updateVersionShowDialog.show(fragmentActivity.getSupportFragmentManager(), "updateVersionShowDialog");
    }
}
