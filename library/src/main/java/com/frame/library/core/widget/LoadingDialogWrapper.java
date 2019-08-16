package com.frame.library.core.widget;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.view.WindowManager;
import android.widget.TextView;

import com.aries.library.fast.R;
import com.frame.library.core.util.StackUtil;
import com.aries.ui.util.FindViewUtil;

import java.lang.ref.WeakReference;

/**
 * @Author: JenkinsZhou on 2018/7/23 14:38
 * @E-Mail: 971613168@qq.com
 * Function: 快速创建网络请求loading
 * Description:
 * 1、2018-7-30 10:08:30 将默认Dialog变更为ProgressDialog及新增构造方式
 * 2、2018-8-23 11:19:29 修改{@link #setMessage(CharSequence)}实现方式
 */
public class LoadingDialogWrapper {

    private Dialog mDialog = null;

    private Activity mActivity;
    private final WeakReference<Activity> mReference;

    public LoadingDialogWrapper() {
        this(StackUtil.getInstance().getCurrent());
    }

    public LoadingDialogWrapper(Activity activity) {
        this(activity, new ProgressDialog.Builder(activity)
                .setMessage(R.string.fast_loading)
                .create());
    }

    public LoadingDialogWrapper(Activity activity, Dialog dialog) {
        this.mReference = new WeakReference<>(activity);
        this.mDialog = dialog;
    }

    /**
     * 设置是否可点击返回键关闭dialog
     *
     * @param enable
     * @return
     */
    public LoadingDialogWrapper setCancelable(boolean enable) {
        if (mDialog != null) {
            mDialog.setCancelable(enable);
        }
        return this;
    }

    /**
     * 设置是否可点击dialog以外关闭
     *
     * @param enable
     * @return
     */
    public LoadingDialogWrapper setCanceledOnTouchOutside(boolean enable) {
        if (mDialog != null) {
            mDialog.setCanceledOnTouchOutside(enable);
        }
        return this;
    }

    /**
     * @param msg
     * @return
     */
    public LoadingDialogWrapper setMessage(CharSequence msg) {
        if (mDialog == null) {
            return this;
        }
        if (mDialog instanceof ProgressDialog) {
            ((ProgressDialog) mDialog).setMessage(msg);
        } else {
            TextView textView = FindViewUtil.getTargetView(mDialog.getWindow().getDecorView(), TextView.class);
            if (textView != null) {
                textView.setText(msg);
            }
        }
        return this;
    }

    /**
     * @param msg
     * @return
     */
    public LoadingDialogWrapper setMessage(int msg) {
        mActivity = mReference.get();
        if (mActivity != null) {
            return setMessage(mActivity.getText(msg));
        }
        return this;
    }

    /**
     * @param enable 设置全透明
     * @return
     */
    public LoadingDialogWrapper setFullTrans(boolean enable) {
        if (mDialog != null) {
            WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
            // 黑暗度
            lp.dimAmount = enable ? 0f : 0.5f;
            mDialog.getWindow().setAttributes(lp);
        }
        return this;
    }

    public Dialog getDialog() {
        return mDialog;
    }

    public void show() {
        mActivity = mReference.get();
        if (mActivity != null && mDialog != null && !mActivity.isFinishing()) {
            mDialog.show();
        }
    }

    public void dismiss() {
        mActivity = mReference.get();
        if (mActivity != null && mDialog != null && !mActivity.isFinishing()) {
            mDialog.dismiss();
        }
    }
}
