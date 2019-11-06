package com.tourcool.bean;

/**
 * @author :JenkinsZhou
 * @description :矩阵实体
 * @company :翼迈科技股份有限公司
 * @date 2019年08月19日23:03
 * @Email: 971613168@qq.com
 */
public class MatrixBean {
    private String matrixIconUrl;
    private String matrixName;
    private String link;
    private int jumpWay;
    private String type;
    private Object children;
    private String columnName;
    private String parentsName;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getChildren() {
        return children;
    }

    public void setChildren(Object children) {
        this.children = children;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getParentsName() {
        return parentsName;
    }

    public void setParentsName(String parentsName) {
        this.parentsName = parentsName;
    }

    public MatrixBean(String matrixIconUrl, String matrixName) {
        this.matrixIconUrl = matrixIconUrl;
        this.matrixName = matrixName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getJumpWay() {
        return jumpWay;
    }

    public void setJumpWay(int jumpWay) {
        this.jumpWay = jumpWay;
    }

    public MatrixBean() {
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
