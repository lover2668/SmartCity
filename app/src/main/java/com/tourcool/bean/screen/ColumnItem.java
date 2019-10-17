package com.tourcool.bean.screen;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年10月16日15:11
 * @Email: 971613168@qq.com
 */
public class ColumnItem {

    /**
     * id : 10
     * name : 错误
     * color : null
     * icon : /smart_file/img/1/1570610485776.png
     * canCustomization : false
     * jumpWay : 2
     * link : null
     * columnType : 2
     * createTime : 1570610487997
     */

    /**
     * ("栏目id")
     */
    private int id;
    /**
     * ("栏目名称")
     */
    private String name;
    /**
     * 栏目颜色
     */
    private Object color;
    /**
     * 栏目的图标
     */
    private String icon;

    /**
     * 是否可被定制
     */
    private boolean canCustomization;
    /**
     * 链接的跳转方式
     */
    private int jumpWay;

    /**
     * 链接 地址
     */
    private String link;

    /**
     * 栏目的类型
     */
    private int columnType;

    /**
     * 栏目的创建时间
     */
    private long createTime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getColor() {
        return color;
    }

    public void setColor(Object color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isCanCustomization() {
        return canCustomization;
    }

    public void setCanCustomization(boolean canCustomization) {
        this.canCustomization = canCustomization;
    }

    public int getJumpWay() {
        return jumpWay;
    }

    public void setJumpWay(int jumpWay) {
        this.jumpWay = jumpWay;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getColumnType() {
        return columnType;
    }

    public void setColumnType(int columnType) {
        this.columnType = columnType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
