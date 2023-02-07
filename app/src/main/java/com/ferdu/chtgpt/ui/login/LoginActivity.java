package com.ferdu.chtgpt.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ferdu.chtgpt.databinding.ActivityLoginBinding;
import com.ferdu.chtgpt.models.data.User;
import com.ferdu.chtgpt.ui.MainActivity;
import com.ferdu.chtgpt.util.CountDownTimerUtil;
import com.ferdu.chtgpt.viewmodel.MyViewModel;

import java.util.Objects;

import cn.leancloud.LCException;
import cn.leancloud.LCUser;
import cn.leancloud.types.LCNull;
import cn.leancloud.utils.ErrorUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LoginActivity extends AppCompatActivity {
    //private boolean isExecute = false;
    private ActivityLoginBinding binding;
    //private int aTry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MyViewModel meViewModel =
                new ViewModelProvider(this).get(MyViewModel.class);
        binding.materialToolbar3.setNavigationOnClickListener(v -> onBackPressed());

        Intent intent = getIntent();
        //aTry = intent.getIntExtra("tryMode", 0);
        binding.loginButton.setOnClickListener(v -> {
            if (!checkInput(binding.textInputLayout.getEditText(), binding.textInputLayout2.getEditText()))
                return;
            binding.loginButton.setVisibility(View.INVISIBLE);
            binding.progressBar3.setVisibility(View.VISIBLE);

            String emailS = binding.textInputLayout.getEditText().getText().toString().trim();
            String passS = binding.textInputLayout2.getEditText().getText().toString().trim();
            if (binding.loginButton.getText().equals("我已确认邮箱验证")) {
                login(meViewModel
                        , emailS, passS, true);
                binding.progressBar3.setVisibility(View.GONE);
                binding.loginButton.setVisibility(View.VISIBLE);
                return;
            }
            LCUser user = new LCUser();
            // 等同于 user.put("username", "Tom")
            user.setUsername(emailS);
            user.setPassword(passS);
            // 可选
            user.setEmail(emailS);
            // user.setMobilePhoneNumber("+8613565306806");
            // user.put("key", "");
            user.signUpInBackground().subscribe(new Observer<LCUser>() {
                public void onSubscribe(Disposable disposable) {


                }
                public void onNext(LCUser user) {
                    // 注册成功
                    System.out.println("注册成功。objectId：" + user.getObjectId());
                    Toast.makeText(LoginActivity.this, "邮箱向邮箱发送了验证消息，请注意查收😊", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    binding.textView26.setVisibility(View.VISIBLE);
                    new CountDownTimerUtil(binding.textView26, 100000, 1000).start();
                    binding.loginButton.setText("我已确认邮箱验证");
                }

                public void onError(Throwable throwable) {
                    // 注册失败（通常是因为用户名已被使用）
                    LCException lcException = ErrorUtils.propagateException(throwable);
                    if (999 == lcException.getCode()) {
                        login(meViewModel, emailS, passS, false);
                    } else
                        Toast.makeText(LoginActivity.this, throwable.getMessage() + "🤨", Toast.LENGTH_SHORT).show();
                }

                public void onComplete() {
                }
            });
            binding.loginButton.setVisibility(View.VISIBLE);
            binding.progressBar3.setVisibility(View.GONE);
        });

    }

    public boolean checkInput(EditText emailEditText, EditText passwordEditText) {
        if (emailEditText == null || passwordEditText == null) {
            Toast.makeText(this, "可能出错了😟", Toast.LENGTH_SHORT).show();
            return false;
        }
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (email.isEmpty() || !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$")) {
            emailEditText.setError("无效电子邮件");
            return false;
        } else if (password.isEmpty() || password.length() < 8) {
            passwordEditText.setError("密码至少8个字符");
            return false;
        }
        return true;
    }

    private void login(MyViewModel meViewModel, String emailS, String passS, boolean isRegister) {

        LCUser.logIn(emailS, passS).subscribe(new Observer<LCUser>() {
            public void onSubscribe(Disposable disposable) {
            }

            public void onNext(LCUser user) {
                // 登录成功
                boolean emailVerified = (boolean) user.get("emailVerified");
                if (!isRegister) {
                    User user1 = new User();
                    String username = user.getUsername();
                    user1.setUserName(username);
                    user1.setMoney((Double) user.get("money"));
                    user1.setTokens((Integer) user.get("tokens"));
                    meViewModel.deleteUser();
                    meViewModel.insertUser(user1);
                    Toast.makeText(LoginActivity.this, "登录成功！🤗", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                  //  FullscreenActivity.jumpClass = "";
                    finish();
                } else if (!emailVerified) {
                    Toast.makeText(LoginActivity.this, "先进行邮箱验证", Toast.LENGTH_SHORT).show();
                } else {
                    User user1 = new User();
                    user1.setUserName(user.getUsername());
                    user1.setMoney((Double) user.get("money"));
                    user1.setTokens((Integer) user.get("tokens"));
                    meViewModel.deleteUser();
                    meViewModel.insertUser(user1);
                    Toast.makeText(LoginActivity.this, "登录成功！🤗", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                   // FullscreenActivity.jumpClass = "";
                    finish();
                }

            }
            public void onError(Throwable throwable) {
                // 登录失败（可能是密码错误）
                LCException lcException = ErrorUtils.propagateException(throwable);
                if (Objects.requireNonNull(lcException.getMessage()).contains("Email address isn't verified")) {
                    binding.textView26.setVisibility(View.VISIBLE);
                    Toast.makeText(LoginActivity.this, "电子邮件地址未验证🤨", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(LoginActivity.this, throwable.getMessage() + "🤨", Toast.LENGTH_SHORT).show();
            }

            public void onComplete() {
            }
        });
        binding.textView26.setOnClickListener(v -> LCUser.requestEmailVerifyInBackground(emailS).subscribe(new Observer<LCNull>() {
            public void onSubscribe(Disposable disposable) {
            }

            public void onNext(LCNull nul) {
                // 成功调用
                new CountDownTimerUtil(binding.textView26, 100000, 1000).start();
            }

            public void onError(Throwable throwable) {
                // 调用出错
                Toast.makeText(LoginActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }

            public void onComplete() {
            }
        }));
    }
}