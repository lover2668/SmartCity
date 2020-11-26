package com.tourcool.bean.constellation;

public class WeekConstellation {


    /**
     * name : 白羊座
     * date : 2014年06月29日-2014年07月05日
     * weekth : 27
     * health :
     * job : 求职：虽有新想法，但心态求稳当，容易低就。但较有可能从家人处获得的机会。
     * love : 恋情：之前积累的想法和感受，本周选择说出来。沟通机会增多，亦有可能以争吵的方式出现。 单身的，在聚会闲谈中可望获得更多缘分。
     * money : 财运：虽有自己的理财想法，但总体受控于家人或家族的财务计划。受木星支撑，有机会得到 家人的支援。但是土逆仍然显示你有债务加大的风险。置业房产出现时机，较大可能是家人出首期，你来月 供。
     * work : 工作：水逆在本周结束，之前耽误、错过的出现弥补机会。职场进入休整状态，有调部门或岗位 的可能。
     * resultcode : 200
     * error_code : 0
     */

    private String name;
    private String date;
    private int weekth;
    private String health;
    private String job;
    private String love;
    private String money;
    private String work;
    private String resultcode;
    private int error_code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getWeekth() {
        return weekth;
    }

    public void setWeekth(int weekth) {
        this.weekth = weekth;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getLove() {
        return love;
    }

    public void setLove(String love) {
        this.love = love;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }
}
