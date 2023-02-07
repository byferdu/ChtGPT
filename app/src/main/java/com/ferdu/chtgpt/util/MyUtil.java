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

import com.ferdu.chtgpt.R;
import com.ferdu.chtgpt.models.ReqModel;
import com.ferdu.chtgpt.viewmodel.MyViewModel;
import com.google.android.material.textfield.TextInputLayout;

import org.commonmark.node.SoftLineBreak;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.noties.markwon.AbstractMarkwonPlugin;
import io.noties.markwon.LinkResolverDef;
import io.noties.markwon.Markwon;
import io.noties.markwon.MarkwonConfiguration;
import io.noties.markwon.MarkwonPlugin;
import io.noties.markwon.MarkwonVisitor;
import io.noties.markwon.core.MarkwonTheme;
import io.noties.markwon.ext.tables.TablePlugin;
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
                    if (editText != null) {
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
                            myViewModel.getTex("Bearer " + editText.getText().toString()
                                    , new ReqModel()).observe(context, responseData -> {
                                if (responseData != null) {
                                    if (responseData.getChoices() != null) {
                                        SharedPreferences.Editor edit = sharedPreferences.edit();
                                        edit.putString("key", "Bearer " + editText.getText().toString());
                                        edit.apply();
                                        Toast.makeText(context, "保存成功！🤗", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(context, "检查密钥🤨", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    editText.setError("错误密钥，检查你的密钥");
                                    Toast.makeText(context, "错误密钥，检查你的密钥🤨", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
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
                    }
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
}
