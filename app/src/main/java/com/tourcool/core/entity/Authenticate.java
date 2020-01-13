package com.tourcool.core.entity;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年01月13日10:19
 * @Email: 971613168@qq.com
 */
public class Authenticate {


    /**
     * type : 1
     * description : 短信
     * authenticated : true
     */

    private int type;
    private String description;
    private boolean authenticated;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
