package com.tourcool.bean;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2019年08月19日23:03
 * @Email: 971613168@qq.com
 */
public class MatrixBean {
    private String matrixIconUrl;
    private String matrixName;

    public MatrixBean(String matrixIconUrl, String matrixName) {
        this.matrixIconUrl = matrixIconUrl;
        this.matrixName = matrixName;
    }

    public String getMatrixIconUrl() {
        return matrixIconUrl;
    }

    public void setMatrixIconUrl(String matrixIconUrl) {
        this.matrixIconUrl = matrixIconUrl;
    }

    public String getMatrixName() {
        return matrixName;
    }

    public void setMatrixName(String matrixName) {
        this.matrixName = matrixName;
    }
}
