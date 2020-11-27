package com.tourcool.bean.driver;

/**
 * @author :JenkinsZhou
 * @description : 违章详情
 * @company :途酷科技
 * @date 2020年11月27日14:14
 * @Email: 971613168@qq.com
 */
public class DriverAgainstDetail {
    /**
     *
     {
     "date": "2013-12-29 11:57:29",
     "area": "316省道53KM+200M",
     "act": "16362 : 驾驶中型以上载客载货汽车、校车、危险物品运输车辆以外的其他机动车在高速公路以外的道路上行驶超过规定时速20%以上未达50%的",
     "code": "",
     "fen": "6",
     "money": "100",
     "handled": "0",
     "archiveno": "320294Y000276124",
     "wzcity": "广东深圳",
     "cjjg": "***交警队"
     }
     */
    private String act;
    private String archiveno;
    private String area;
    private String cjjg;
    private String code;
    private String date;
    private String fen;
    private String handled;
    private String handledStr;
    private String money;
    private String wzcity;

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getArchiveno() {
        return archiveno;
    }

    public void setArchiveno(String archiveno) {
        this.archiveno = archiveno;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCjjg() {
        return cjjg;
    }

    public void setCjjg(String cjjg) {
        this.cjjg = cjjg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFen() {
        return fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
    }

    public String getHandled() {
        return handled;
    }

    public void setHandled(String handled) {
        this.handled = handled;
    }

    public String getHandledStr() {
        return handledStr;
    }

    public void setHandledStr(String handledStr) {
        this.handledStr = handledStr;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getWzcity() {
        return wzcity;
    }

    public void setWzcity(String wzcity) {
        this.wzcity = wzcity;
    }

    /**
     * province : HB
     * city : HB_HD
     * hphm : 冀DHL327
     * hpzl : 02
     * lists : [{"date":"2013-12-29 11:57:29","area":"316省道53KM+200M","act":"16362 : 驾驶中型以上载客载货汽车、校车、危险物品运输车辆以外的其他机动车在高速公路以外的道路上行驶超过规定时速20%以上未达50%的","code":"","fen":"6","money":"100","handled":"0","archiveno":"320294Y000276124","wzcity":"广东深圳","cjjg":"***交警队"}]
     */
}
