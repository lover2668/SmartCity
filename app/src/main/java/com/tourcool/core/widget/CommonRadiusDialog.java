package com.tourcool.core.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tourcool.smartcity.R;

/**
 * @author :JenkinsZhou
 * @description : 带圆角的dialog
 * @company :途酷科技
 * @date 2020年12月24日15:33
 * @Email: 971613168@qq.com
 */
public class CommonRadiusDialog {
    private Context context;
    private Dialog dialog;
    private LinearLayout container;
    private TextView titleTv;
    private TextView msgTv;
    private TextView negBtn;
    private TextView posBtn;
    private Display display;
    private boolean showTitle = false;
    private boolean showMsg = false;
    private boolean showPosBtn = false;
    private boolean showNegBtn = false;

    public CommonRadiusDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_common_radius_15_layout, null);
        // 获取自定义Dialog布局中的控件
        container = view.findViewById(R.id.dialogContainer);
        posBtn = view.findViewById(R.id.posBtn);
        view.findViewById(R.id.ivClosed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        negBtn = view.findViewById(R.id.negBtn);
        titleTv = view.findViewById(R.id.tvTitle);
        msgTv = view.findViewById(R.id.tvContentFirst);
        this.dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        // 调整dialog背景大小
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        int width = dm.widthPixels;
        container.setLayoutParams(
                new FrameLayout.LayoutParams(
                        (int) (width * 0.6),
                        LinearLayout.LayoutParams.WRAP_CONTENT));

    }

    public CommonRadiusDialog setNegativeButton(String text, final View.OnClickListener listener) {
        if (TextUtils.isEmpty(text)) {
            negBtn.setVisibility(View.GONE);
            showNegBtn = false;
        } else {
            negBtn.setText(text);
            negBtn.setVisibility(View.VISIBLE);
            showNegBtn = true;
        }
        negBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
                dialog.dismiss();
            }
        });
        return this;
    }


    public CommonRadiusDialog setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            titleTv.setVisibility(View.GONE);
            showTitle = false;
        } else {
            showTitle = true;
            titleTv.setText(title);
            titleTv.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public CommonRadiusDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public CommonRadiusDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public CommonRadiusDialog setMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            msgTv.setVisibility(View.GONE);
            showMsg = true;
        } else {
            msgTv.setText(msg);
            msgTv.setVisibility(View.VISIBLE);
            showMsg = false;
        }
        return this;
    }


    public CommonRadiusDialog setPositiveButton(String text,
                                                final View.OnClickListener listener) {
        if (TextUtils.isEmpty(text)) {
            showPosBtn = false;
            posBtn.setVisibility(View.GONE);
        } else {
            posBtn.setText(text);
            showPosBtn = true;
            posBtn.setVisibility(View.VISIBLE);
        }
        posBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }

    }

}
