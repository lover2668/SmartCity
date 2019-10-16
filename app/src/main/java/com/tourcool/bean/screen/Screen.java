package com.tourcool.bean.screen;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年10月15日16:32
 * @Email: 971613168@qq.com
 */
public class Screen {


    /**
     * screenId : 1
     * name : 首页
     * idx : 0
     * type : SCREEN
     * children : [{"screenPartId":142,"idx":0,"layoutStyle":3,"detail":{"id":10,"name":"错误","color":null,"icon":"/smart_file/img/1/1570610485776.png","canCustomization":false,"jumpWay":2,"link":null,"columnType":2,"createTime":1570610487997},"children":[{"detail":{"id":3,"icon":"/smart_file/img/1/1570591455697.png","title":"标题11","description":"描述","jumpWay":2,"link":"xxx","authLevel":0,"channelType":0,"createTime":1570524243888},"visible":true,"idx":0,"type":"SUB_CHANNEL","children":null},{"detail":{"id":9,"name":"角色四","color":null,"icon":"/smart_file/img/1/1570610473123.png","canCustomization":false,"jumpWay":2,"link":null,"columnType":3,"createTime":1570610474423},"visible":true,"idx":1,"type":"SUB_COLUMN","children":[{"detail":{"id":7,"icon":"/smart_file/img/1/1571125466750.png","title":"123","description":"测试新增","jumpWay":1,"link":"https://www.baidu.com","authLevel":1,"channelType":2,"createTime":1570590899582},"visible":true,"idx":1,"type":"SUB_CHANNEL","children":null}]},{"detail":{"id":7,"icon":"/smart_file/img/1/1571125466750.png","title":"123","description":"测试新增","jumpWay":1,"link":"https://www.baidu.com","authLevel":1,"channelType":2,"createTime":1570590899582},"visible":true,"idx":1,"type":"SUB_CHANNEL","children":null}],"type":"ScreenPart"}]
     */
    /**
     * 屏幕的id
     */
    private int screenId;
    /**
     * 屏幕的名称
     */
    private String name;
    private int idx;

    /**
     * 类别,固定为SCREEN
     */
    private String type;
    /**
     * 屏幕下的栏目集合
     */
    private List<ScreenPart> children;

    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ScreenPart> getChildren() {
        return children;
    }

    public void setChildren(List<ScreenPart> children) {
        this.children = children;
    }


}
