package com.ferdu.chtgpt.util;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.ferdu.chtgpt.R;
import com.ferdu.chtgpt.models.ReqModel;
import com.ferdu.chtgpt.models.ResponseModel2;
import com.ferdu.chtgpt.models.TuneModel;
import com.ferdu.chtgpt.viewmodel.MyViewModel;
import com.google.android.material.textfield.TextInputLayout;

import org.commonmark.node.SoftLineBreak;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import io.noties.markwon.AbstractMarkwonPlugin;
import io.noties.markwon.LinkResolverDef;
import io.noties.markwon.Markwon;
import io.noties.markwon.MarkwonConfiguration;
import io.noties.markwon.MarkwonPlugin;
import io.noties.markwon.MarkwonVisitor;
import io.noties.markwon.core.MarkwonTheme;
import io.noties.markwon.ext.tables.TablePlugin;
import io.noties.markwon.image.glide.GlideImagesPlugin;
import io.noties.markwon.linkify.LinkifyPlugin;
import io.noties.markwon.syntax.Prism4jThemeDarkula;
import io.noties.markwon.syntax.SyntaxHighlightPlugin;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.annotations.PrismBundle;

@PrismBundle(
        include = {"json", "java", "c", "csharp", "python"
                , "dart", "sql", "javascript", "cpp", "kotlin", "markdown", "markup"},
        grammarLocatorClassName = ".MyGrammarLocator"
)
public class MyUtil {

    public static Markwon getMarkDown(Context context) {
        final Prism4j prism4j = new Prism4j(new MyGrammarLocator());
        List<MarkwonPlugin> list = new ArrayList<>();
        list.add(LinkifyPlugin.create(Linkify.EMAIL_ADDRESSES | Linkify.WEB_URLS, true));
        list.add(SyntaxHighlightPlugin.create(prism4j, Prism4jThemeDarkula.create()));

        list.add(new AbstractMarkwonPlugin() {
            @Override
            public void configureVisitor(@NonNull MarkwonVisitor.Builder builder) {
                builder.on(SoftLineBreak.class, (visitor, softLineBreak) -> visitor.forceNewLine());
            }
        });
        list.add(new AbstractMarkwonPlugin() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void configureTheme(@NonNull MarkwonTheme.Builder builder) {
                builder.headingBreakHeight(0);
                builder.codeBlockBackgroundColor(context.getTheme().getResources().getColor(R.color.into_back_color));
                Typeface.Builder builder1 = new Typeface.Builder(context.getAssets(), "font/jet_brains_mono_regular.ttf");
                Typeface build = builder1.build();
                builder.codeTextSize(30);
                builder.codeTypeface(build);
                builder.codeBlockMargin(20);
            }
        });
        list.add(GlideImagesPlugin.create(context));
        list.add(new AbstractMarkwonPlugin() {
            @Override
            public void configureConfiguration(@NonNull MarkwonConfiguration.Builder builder) {
                builder.linkResolver(new LinkResolverDef());
            }
        });
        list.add(TablePlugin.create(context));

        return Markwon.builder(context)
                .usePlugins(list)
                .build();
    }

    public static Markwon getMarkDown2(Context context) {
        final Prism4j prism4j = new Prism4j(new MyGrammarLocator());
        List<MarkwonPlugin> list = new ArrayList<>();
        list.add(new AbstractMarkwonPlugin() {
            @Override
            public void configureVisitor(@NonNull MarkwonVisitor.Builder builder) {
                builder.on(SoftLineBreak.class, (visitor, softLineBreak) -> visitor.forceNewLine());
            }
        });
        list.add(SyntaxHighlightPlugin.create(prism4j, Prism4jThemeDarkula.create()));
        list.add(new AbstractMarkwonPlugin() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void configureTheme(@NonNull MarkwonTheme.Builder builder) {
                builder.headingBreakHeight(0);
                builder.codeBlockMargin(50);
                builder.blockMargin(20);
                Typeface.Builder builder1 = new Typeface.Builder(context.getAssets(), "font/jet_brains_mono_regular.ttf");
                Typeface build = builder1.build();
                builder.codeTypeface(build);
                builder.codeBackgroundColor(context.getTheme().getResources().getColor(R.color.white_black3));
                builder.codeTextColor(context.getTheme().getResources().getColor(R.color.black_white));
                builder.codeBlockTextColor(context.getTheme().getResources().getColor(R.color.black_white));
                builder.codeBlockBackgroundColor(context.getTheme().getResources().getColor(R.color.into_back_color));
            }
        });
        list.add(new AbstractMarkwonPlugin() {
            @Override
            public void configureConfiguration(@NonNull MarkwonConfiguration.Builder builder) {
                builder.linkResolver(new LinkResolverDef());
            }
        });
        list.add(TablePlugin.create(context));
        // use TableAwareLinkMovementMethod to handle clicks inside tables,
        // wraps LinkMovementMethod internally
        //  .usePlugin(MovementMethodPlugin.create(TableAwareMovementMethod.create())))
        return Markwon.builder(context)
                .usePlugins(list)
                .build();
    }

    public static void ShowSetKeyDialog(FragmentActivity context, View rootView, MyViewModel myViewModel, boolean isTry) {
        View view = LayoutInflater.from(context).inflate(R.layout.key_dialog, (ViewGroup) rootView, false);
        ProgressBar progressBar = view.findViewById(R.id.progressBar4);
        EditText editText = ((TextInputLayout) view.findViewById(R.id.keyEdit)).getEditText();
        TextView explainText = view.findViewById(R.id.explainText);
        TextView noKeyText = view.findViewById(R.id.isNoKey);

        String negativeText = "试用看看";
        if (!isTry) {
            negativeText = "取消";
        } else {
            explainText.append(context.getString(R.string.dialog_explain_try));
        }
        noKeyText.setOnClickListener(v -> {
            if (editText != null) {
                if (explainText.getVisibility() == View.GONE) {
                    explainText.setVisibility(View.VISIBLE);
                    noKeyText.setText("收起");
                } else {
                    explainText.setVisibility(View.GONE);
                    noKeyText.setText("没有密钥？");
                }
            }

        });
        SharedPreferences sharedPreferences = getShared(context);

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialog)
                //.setTitle("设置密钥")
                .setPositiveButton("设置好了", (dialogInterface, i) -> {
                    if (editText == null) {
                        return;
                    }
                    if (editText.getText().toString().trim().isEmpty()) {
                        //不满足条件，“确定”按钮无效
                        try {
                            Field field = Objects.requireNonNull(dialogInterface.getClass().getSuperclass()).getDeclaredField("mShowing");
                            field.setAccessible(true);
                            field.set(dialogInterface, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("1324Tag", "onCreate: " + e);
                        }
                        editText.setError("请填写完整");
                        Toast.makeText(context, "请填写完整🤨", Toast.LENGTH_SHORT).show();

                    } else if (editText.getText().toString().contains("sk-")) {
                        progressBar.setVisibility(View.VISIBLE);
                        Log.d("TAG1213", "onCreate: " + sharedPreferences.getString("token", ""));
                        LiveData<Boolean> isSuccessLiveData = requestTest(myViewModel, editText.getText().toString(), new ReqModel(), context);
                        isSuccessLiveData.observe(context, isSuccess -> {
                            // 在这里根据isSuccess的值做你想做的事情
                            if (isSuccess) {
                                SharedPreferences.Editor edit = getShared(context).edit();
                                edit.putString("key", "Bearer " + editText.getText().toString());
                                edit.apply();
                                Toast.makeText(context, "操作成功！🤗", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                try {
                                    Field field = Objects.requireNonNull(dialogInterface.getClass().getSuperclass()).getDeclaredField("mShowing");
                                    field.setAccessible(true);
                                    field.set(dialogInterface, true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                dialogInterface.dismiss();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                editText.setError("错误密钥，检查你的密钥");
                                Toast.makeText(context, "错误密钥，检查你的密钥🤨", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        progressBar.setVisibility(View.GONE);
                        editText.setError("错误密钥，检查你的密钥");
                        Toast.makeText(context, "错误密钥，检查你的密钥🤨", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        Field field = Objects.requireNonNull(dialogInterface.getClass().getSuperclass()).getDeclaredField("mShowing");
                        field.setAccessible(true);
                        field.set(dialogInterface, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("1324Tag", "onCreate: " + e);
                    }
                    //dialogInterface.dismiss();

                })
                .setNegativeButton(negativeText, ((dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    try {
                        Field field = Objects.requireNonNull(dialogInterface.getClass().getSuperclass()).getDeclaredField("mShowing");
                        field.setAccessible(true);
                        field.set(dialogInterface, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));

        builder.setView(view);
        builder.create().show();

        if (builder.create().isShowing()) {
            builder.create().dismiss();
        }
    }

    public static SharedPreferences getShared(Context context) {
        return context.getSharedPreferences("key_1", MODE_PRIVATE);
    }

    private static long lastClickTime = 0L;
    private static long lastClickTime2 = 0L;

    public static boolean isNotFastClick(int delayTime) {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        long l = curClickTime - lastClickTime;
        if (l >= delayTime) {
            flag = true;
        }
        return flag;
    }

    public static void saveLastClick(long curClickTime) {
        lastClickTime = curClickTime;
    }

    public static LiveData<Boolean> requestTest(MyViewModel myViewModel, String key, ReqModel reqModel, FragmentActivity context) {
        // ProgressBar progressBar = new MyProgressBar(context);
        if (Objects.equals(key, "key")) {
            key = MyUtil.getShared(context).getString("key", "");
        }
        LiveData<ResponseModel2> responseDataLiveData = myViewModel.getTex("Bearer " + key, reqModel);
        String finalKey = key;
        return Transformations.map(responseDataLiveData, responseData -> {
            if (responseData.getErrorMessage() == null) {
                if (responseData.getErrorParent() != null) {
                    Toast.makeText(context, responseData.getErrorParent().getError().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("requestTest", "requestTest: responseData.getError() != null\n " + responseData.getErrorParent().getError().getMessage() + "\n");
                    return false;
                }
                Log.d("requestTest", "requestTest: responseData.getChoices() \n\n " + responseData.getChoices() + "\n");
                Log.d("requestTest", "requestTest: reqModel.toString \n\n " + reqModel.toString() + "\n");
                Log.d("requestTest", "requestTest: key \n\n " + finalKey + "\n");

                return responseData.getChoices() != null;
            } else {
                Toast.makeText(context, responseData.getErrorMessage(), Toast.LENGTH_SHORT).show();
                Log.d("requestTest", "requestTest: responseData.getErrorMessage() =! null \n\n " + responseData.getErrorMessage() + "\n");
                return false;
            }
        });

    }

    public static boolean isNotFastClick2(int delayTime) {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        long l = curClickTime - lastClickTime2;
        if (l >= delayTime) {
            flag = true;
        }
        lastClickTime2 = curClickTime;
        return flag;
    }

    public static TuneModel tuneModel = new TuneModel();

    public static void requestTest(MyViewModel myViewModel, String key, ReqModel reqModel
            , FragmentActivity context, OnRequestCompletedListener listener) {
        if (Objects.equals(key, "key")) {
            key = MyUtil.getShared(context).getString("key", "");
        }
        String finalKey = key;
        AtomicReference<String> errorMessage = new AtomicReference<>("");
        myViewModel.getTex(key, reqModel).observe(context, responseData -> {
            if (responseData.getErrorMessage() == null) {
                if (responseData.getErrorParent() != null ) {
                    errorMessage.set(responseData.getErrorParent().getError().getMessage());
                    Toast.makeText(context, responseData.getErrorParent().getError().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("requestTest", "requestTest: responseData.getError() != null\n " + responseData.getErrorParent().getError().getMessage() + "\n");
                    listener.onRequestCompleted(false, responseData.getErrorParent().getError().getMessage());

                }  else {
                    Log.d("requestTest", "requestTest: responseData.getChoices() \n\n " + responseData.getChoices() + "\n");
                    Log.d("requestTest", "requestTest: reqModel.toString \n\n " + reqModel.toString() + "\n");
                    Log.d("requestTest", "requestTest: key \n\n " + finalKey + "\n");
                    listener.onRequestCompleted(responseData.getChoices() != null, errorMessage.get());
                }
            } else {
                Toast.makeText(context, responseData.getErrorMessage(), Toast.LENGTH_SHORT).show();
                Log.d("requestTest", "requestTest: responseData.getErrorMessage() =! null \n\n " + responseData.getErrorMessage() + "\n");
                listener.onRequestCompleted(false, responseData.getErrorMessage());
            }
        });
    }

    public interface OnRequestCompletedListener {
        void onRequestCompleted(boolean isSuccess, String errorMassage);
    }

}
