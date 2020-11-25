package com.tourcool.bean.express;

/**
 * @author :JenkinsZhou
 * @description : 快递物流
 * @company :途酷科技
 * @date 2020年11月25日19:41
 * @Email: 971613168@qq.com
 */
public class ExpressInfo {
    private String datetime;
    private String remark;
    private String zone;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
