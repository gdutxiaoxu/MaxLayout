package com.xj.maxlayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mLlRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        addBtn(HeightSampleActivity.class);
    }

    private <T extends Activity> void addBtn(final Class<T> clz) {
        addBtn(clz, clz.getSimpleName());
    }

    private <T extends Activity> void addBtn(final Class<T> clz, String text) {
        Button button = new Button(MainActivity.this);
        button.setAllCaps(true);
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = 10;
        if (TextUtils.isEmpty(text)) {
            button.setText(clz.getSimpleName());
        } else {
            button.setText(text);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGo(clz);
            }
        });
        mLlRoot.addView(button, layoutParams);
    }


    /**
     * 启动Activity
     */
    public void readyGo(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    private void initView() {
        mLlRoot = findViewById(R.id.ll_root);
    }
}
