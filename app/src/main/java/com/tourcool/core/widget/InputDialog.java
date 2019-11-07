package com.tourcool.core.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.frame.library.core.log.TourCooLogUtil;
import com.tourcool.smartcity.R;


/**
 * @author :JenkinsZhou
 * @description :自定义输入框
 * @company :途酷科技
 * @date 2019年08月01日10:11
 * @Email: 971613168@qq.com
 */
public class InputDialog {
    public static final String TAG = "InputDialog";
    private Dialog dialog;
    private Context context;
    private AppCompatTextView titleTextView;
    private AppCompatTextView cancelTextView;
    private AppCompatTextView confirmTextView;
    private AppCompatEditText contentEditText;

    public InputDialog(Context context, String title, String content) {
        this.context = context;
        init(title, content);
    }

    public InputDialog(Context context, int title, String content) {
        this.context = context;
        init(context.getString(title), content);
    }

    public InputDialog(Context context, String title, int content) {
        this.context = context;
        init(title, context.getString(content));
    }

    public InputDialog(Context context, int title, int content) {
        this.context = context;
        init(context.getString(title), context.getString(content));
    }

    public void setCancelListener(View.OnClickListener listener) {

        cancelTextView.setOnClickListener(listener);

    }

    public void setConfirmListener(View.OnClickListener listener) {

        confirmTextView.setOnClickListener(listener);

    }

    public String getTitle() {

        return titleTextView.getText().toString();

    }

    public void setTitle(String title) {

        titleTextView.setText(title);

    }

    public String getContent() {
        if (contentEditText.getText() == null) {
            return "";
        }
        return contentEditText.getText().toString();

    }

    public void setContent(String content) {

        contentEditText.setText(content);

    }

    public void dismiss() {

        dialog.dismiss();

    }

    private void init(String title, String content) {

        this.dialog = new Dialog(context, R.style.BaseDialog);
        dialog.setContentView(R.layout.dialog_input_layout);
        dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
        Window window = dialog.getWindow();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        layoutParams.width = (displayMetrics.widthPixels * 1);
        window.setAttributes(layoutParams);
        titleTextView = dialog.findViewById(R.id.titleTextView);
        contentEditText = dialog.findViewById(R.id.contentEditText);
        cancelTextView = dialog.findViewById(R.id.cancelTextView);
        confirmTextView = dialog.findViewById(R.id.confirmTextView);
        titleTextView.setText(title);
        contentEditText.setText(content);
        try {
            contentEditText.setSelection(content.length());
        } catch (Exception e) {
            TourCooLogUtil.e(TAG,"setSelection()异常---->"+ e.toString());
        }

        contentEditText.post(() -> {
            InputMethodManager inputMethodManager = (InputMethodManager) ((Activity) context).getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        });

    }

    public void show() {
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        WindowManager m = window.getWindowManager();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        //宽高可设置具体大小
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        // lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        // lp.width  = 400;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
        // 注释：dialog.onWindowAttributesChanged(lp);
        window.setAttributes(lp);
        // 获取屏幕宽、高用
        Display d = m.getDefaultDisplay();
        // 获取对话框当前的参数值
        WindowManager.LayoutParams p = window.getAttributes();
        Display display = m.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        // 宽度设置为屏幕的0.8
        p.width = (int) (width * 0.8);
        window.setAttributes(p);
        dialog.show();
    }


    public Dialog getInstanstace() {
        return dialog;
    }
}
