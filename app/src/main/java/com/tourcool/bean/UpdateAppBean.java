package com.tourcool.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spanned;
import android.text.TextUtils;

import com.alipay.zoloz.android.phone.mrpc.core.HttpManager;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年01月16日18:01
 * @Email: 971613168@qq.com
 */
public class UpdateAppBean implements Parcelable {

    private static final long serialVersionUID = 1L;

    /**
     * update : Yes
     * new_version : xxxxx
     * apk_url : http://cdn.the.url.of.apk/or/patch
     * update_log : xxxx
     * delta : false
     * new_md5 : xxxxxxxxxxxxxx
     * target_size : 601132
     */
    //是否有新版本
    private String update;
    //新版本号
    private String new_version;
    //新app下载地址
    private String apk_file_url;
    //更新日志
    private String update_log;
    private Spanned spanned_update_log;
    //配置默认更新dialog 的title
    private String update_def_dialog_title;
    //新app大小
    private String target_size;
    //是否强制更新
    private boolean constraint;
    //md5
    private String new_md5;
    //是否增量 暂时不用
    private boolean delta;
    //服务器端的原生返回数据（json）,方便使用者在hasNewApp自定义渲染dialog的时候可以有别的控制，比如：#issues/59
    private String origin_res;
    /**********以下是内部使用的数据**********/

    //网络工具，内部使用
    private HttpManager httpManager;
    private String targetPath;
    private boolean mHideDialog;
    private boolean mShowIgnoreVersion;
    private boolean mDismissNotificationProgress;
    private boolean mOnlyWifi;

    //是否隐藏对话框下载进度条,内部使用
    public boolean isHideDialog() {
        return mHideDialog;
    }

    public void setHideDialog(boolean hideDialog) {
        mHideDialog = hideDialog;
    }

    public boolean isUpdate() {
        return !TextUtils.isEmpty(this.update) && "Yes".equals(this.update);
    }

    public HttpManager getHttpManager() {
        return httpManager;
    }

    public void setHttpManager(HttpManager httpManager) {
        this.httpManager = httpManager;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public boolean isConstraint() {
        return constraint;
    }

    public UpdateAppBean setConstraint(boolean constraint) {
        this.constraint = constraint;
        return this;
    }

    public String getUpdate() {
        return update;
    }

    public UpdateAppBean setUpdate(String update) {
        this.update = update;
        return this;
    }

    public String getNewVersion() {
        return new_version;
    }

    public UpdateAppBean setNewVersion(String new_version) {
        this.new_version = new_version;
        return this;
    }

    public String getApkFileUrl() {
        return apk_file_url;
    }


    public UpdateAppBean setApkFileUrl(String apk_file_url) {
        this.apk_file_url = apk_file_url;
        return this;
    }

    public String getUpdateLog() {
        return update_log;
    }

    public UpdateAppBean setUpdateLog(String update_log) {
        this.update_log = update_log;
        return this;
    }

    public Spanned getSpannedUpdateLog() {
        return spanned_update_log;
    }

    public UpdateAppBean setSpannedUpdateLog(Spanned spanned_update_log) {
        this.spanned_update_log = spanned_update_log;
        return this;
    }

    public String getUpdateDefDialogTitle() {
        return update_def_dialog_title;
    }

    public UpdateAppBean setUpdateDefDialogTitle(String updateDefDialogTitle) {
        this.update_def_dialog_title = updateDefDialogTitle;
        return this;
    }

    public boolean isDelta() {
        return delta;
    }

    public void setDelta(boolean delta) {
        this.delta = delta;
    }

    public String getNewMd5() {
        return new_md5;
    }

    public UpdateAppBean setNewMd5(String new_md5) {
        this.new_md5 = new_md5;
        return this;
    }

    public String getTargetSize() {
        return target_size;
    }

    public UpdateAppBean setTargetSize(String target_size) {
        this.target_size = target_size;
        return this;
    }

    public boolean isShowIgnoreVersion() {
        return mShowIgnoreVersion;
    }

    public void showIgnoreVersion(boolean showIgnoreVersion) {
        mShowIgnoreVersion = showIgnoreVersion;
    }

    public void dismissNotificationProgress(boolean dismissNotificationProgress) {
        mDismissNotificationProgress = dismissNotificationProgress;
    }

    public boolean isDismissNotificationProgress() {
        return mDismissNotificationProgress;
    }

    public boolean isOnlyWifi() {
        return mOnlyWifi;
    }

    public void setOnlyWifi(boolean onlyWifi) {
        mOnlyWifi = onlyWifi;
    }

    public String getOriginRes() {
        return origin_res;
    }

    public UpdateAppBean setOriginRes(String originRes) {
        this.origin_res = originRes;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.update);
        dest.writeString(this.new_version);
        dest.writeString(this.apk_file_url);
        dest.writeString(this.update_log);
        dest.writeParcelable((Parcelable) this.spanned_update_log, flags);
        dest.writeString(this.update_def_dialog_title);
        dest.writeString(this.target_size);
        dest.writeByte(this.constraint ? (byte) 1 : (byte) 0);
        dest.writeString(this.new_md5);
        dest.writeByte(this.delta ? (byte) 1 : (byte) 0);
        dest.writeString(this.origin_res);
        dest.writeParcelable((Parcelable) this.httpManager, flags);
        dest.writeString(this.targetPath);
        dest.writeByte(this.mHideDialog ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mShowIgnoreVersion ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mDismissNotificationProgress ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mOnlyWifi ? (byte) 1 : (byte) 0);
    }

    public UpdateAppBean() {
    }

    protected UpdateAppBean(Parcel in) {
        this.update = in.readString();
        this.new_version = in.readString();
        this.apk_file_url = in.readString();
        this.update_log = in.readString();
        this.spanned_update_log = in.readParcelable(Spanned.class.getClassLoader());
        this.update_def_dialog_title = in.readString();
        this.target_size = in.readString();
        this.constraint = in.readByte() != 0;
        this.new_md5 = in.readString();
        this.delta = in.readByte() != 0;
        this.origin_res = in.readString();
        this.httpManager = in.readParcelable(HttpManager.class.getClassLoader());
        this.targetPath = in.readString();
        this.mHideDialog = in.readByte() != 0;
        this.mShowIgnoreVersion = in.readByte() != 0;
        this.mDismissNotificationProgress = in.readByte() != 0;
        this.mOnlyWifi = in.readByte() != 0;
    }

    public static final Parcelable.Creator<UpdateAppBean> CREATOR = new Parcelable.Creator<UpdateAppBean>() {
        @Override
        public UpdateAppBean createFromParcel(Parcel source) {
            return new UpdateAppBean(source);
        }

        @Override
        public UpdateAppBean[] newArray(int size) {
            return new UpdateAppBean[size];
        }
    };
}
