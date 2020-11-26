package com.tourcool.bean.constellation;

public class MonthConstellation {


    /**
     * date : 2020年11月
     * name : 双鱼座
     * month : 11
     * health : 健康：本月对待很多事情都粗枝大叶，即使身体不舒服，往往也会大而化之，建议不舒服一定要及时就医。
     * all : 总运势：双鱼在这个月整体运势逐步回升，但是双鱼也会遇到一些挑战，需要他们改变自己的交际方法和处事手段。
     * love : 感情运：本月在感情方面的运势为有伴侣的人双方感情较为稳定，关系和睦，单身者则部分有些旧情难忘。
     * money : 财运：在财利方面的运势为财运较为稳定，踏实工作能够得到相应的回报，偏财上不可投机取巧，
     * work : 工作运：在工作方面的运势为工作上重整旗鼓，效率和质量都会有所提高，而且自身的努力也会让领导对你重拾信心。
     * happyMagic :
     * resultcode : 200
     * error_code : 0
     */

    private String date;
    private String name;
    private int month;
    private String health;
    private String all;
    private String love;
    private String money;
    private String work;
    private String happyMagic;
    private String resultcode;
    private int error_code;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
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

    public String getHappyMagic() {
        return happyMagic;
    }

    public void setHappyMagic(String happyMagic) {
        this.happyMagic = happyMagic;
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
