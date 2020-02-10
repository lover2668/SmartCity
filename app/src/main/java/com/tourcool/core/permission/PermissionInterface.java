package com.tourcool.core.permission;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年02月10日12:52
 * @Email: 971613168@qq.com
 */
public interface PermissionInterface {

    /**
     * 请求权限成功回调
     */
    void requestPermissionsSuccess(int callBackCode);

    /**
     * 请求权限失败回调
     */
    void requestPermissionsFail(int callBackCode);

}
