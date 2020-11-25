package com.tourcool.bean.express;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description : JenkinsZhou
 * @company :途酷科技
 * @date 2020年11月25日21:53
 * @Email: 971613168@qq.com
 */
public class ExpressBean {

    /**
     * com :
     * company :
     * list : [{"datetime":"","remark":"","zone":""}]
     * no :
     * status :
     * status_detail :
     */

    private String com;
    private String company;
    private String no;
    private String status;
    private String status_detail;
    private List<ExpressInfo> list;

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_detail() {
        return status_detail;
    }

    public void setStatus_detail(String status_detail) {
        this.status_detail = status_detail;
    }

    public List<ExpressInfo> getList() {
        return list;
    }

    public void setList(List<ExpressInfo> list) {
        this.list = list;
    }

}
