package com.tourcool.bean.certify;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年01月16日10:45
 * @Email: 971613168@qq.com
 */
public class CertifyType {
    private String certifyName;
    private int certifyIconId;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isCertified() {
        return certified;
    }

    public void setCertified(boolean certified) {
        this.certified = certified;
    }

    private boolean certified;





    public String getCertifyName() {
        return certifyName;
    }

    public void setCertifyName(String certifyName) {
        this.certifyName = certifyName;
    }

    public int getCertifyIconId() {
        return certifyIconId;
    }

    public void setCertifyIconId(int certifyIconId) {
        this.certifyIconId = certifyIconId;
    }

    public CertifyType(String certifyName, int certifyIconId) {
        this.certifyName = certifyName;
        this.certifyIconId = certifyIconId;
    }
}
