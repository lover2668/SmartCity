package com.tourcool.core.entity;

import android.text.TextUtils;

import com.tourcool.core.MyApplication;
import com.frame.library.core.util.TourCooUtil;

/**
 * @Author: JenkinsZhou on 2018/11/19 14:17
 * @E-Mail: 971613168@qq.com
 * @Function: 检查新版本实体
 * @Description:
 */
public class UpdateEntity {

    /**
     * versionCode : 230
     * versionName : 2.2.10-beta1
     * url : https://raw.githubusercontent.com/JenkinsZhou/FastLib/master/apk/sample.apk
     * force : true
     * message : 优化:调整ActivityFragmentControl 将状态栏及导航栏控制增加
     * 优化:多状态管理StatusLayoutManager调整完成
     * 优化:滑动返回控制swipeBack功能新增各种回调功能
     * 优化:将原默认配置方法调整到最终实现类功能
     * 优化:其它细节优化
     */
    public int versionCode;
    public String versionName;
    public String url;
    public boolean force;
    public String message;
    public String size;

    public boolean isSuccess() {
        int code = TourCooUtil.getVersionCode(MyApplication.getContext());
        String name = TourCooUtil.getVersionName(MyApplication.getContext());
        if (versionCode > code || (versionCode == code && name.compareTo(versionName) < 0)) {
            return true;
        }
        return false;
    }

    public String getMessage() {
        return TextUtils.isEmpty(versionName)?"": isSuccess() ? force ? "由于系统升级,您的版本已停止服务,请及时更新到最新版本!" + message : "为了更好的为您服务,建议您更新到最新版本!" + message : "当前已是最新版本";
    }

    public CharSequence getSize() {
        return TextUtils.isEmpty(size) ? "下载新版本" : "安装包大小:" + size;
    }

    public CharSequence getTitle() {
        return TextUtils.isEmpty(versionName) ? "下载文件" : "下载新版:V" + versionName;
    }
}
