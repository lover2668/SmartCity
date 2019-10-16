package com.tourcool.bean.screen;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年10月16日15:55
 * @Email: 971613168@qq.com
 */
public class ScreenPart {

    /**
     *
     * screenPartId : 142
     * idx : 0
     * layoutStyle : 3
     * detail : {"id":10,"name":"错误","color":null,"icon":"/smart_file/img/1/1570610485776.png","canCustomization":false,"jumpWay":2,"link":null,"columnType":2,"createTime":1570610487997}
     * children : [{"detail":{"id":3,"icon":"/smart_file/img/1/1570591455697.png","title":"标题11","description":"描述","jumpWay":2,"link":"xxx","authLevel":0,"channelType":0,"createTime":1570524243888},"visible":true,"idx":0,"type":"SUB_CHANNEL","children":null},{"detail":{"id":9,"name":"角色四","color":null,"icon":"/smart_file/img/1/1570610473123.png","canCustomization":false,"jumpWay":2,"link":null,"columnType":3,"createTime":1570610474423},"visible":true,"idx":1,"type":"SUB_COLUMN","children":[{"detail":{"id":7,"icon":"/smart_file/img/1/1571125466750.png","title":"123","description":"测试新增","jumpWay":1,"link":"https://www.baidu.com","authLevel":1,"channelType":2,"createTime":1570590899582},"visible":true,"idx":1,"type":"SUB_CHANNEL","children":null}]},{"detail":{"id":7,"icon":"/smart_file/img/1/1571125466750.png","title":"123","description":"测试新增","jumpWay":1,"link":"https://www.baidu.com","authLevel":1,"channelType":2,"createTime":1570590899582},"visible":true,"idx":1,"type":"SUB_CHANNEL","children":null}]
     * type : ScreenPart
     */

    /**
     * 当前屏幕块的id
     */
    private int screenPartId;

    /**
     * idx
     */
    private int idx;
    /**
     * 栏目的布局样式
     */
    private int layoutStyle;
    /**
     * 栏目的明细
     */
    private ColumnItem detail;
    private String type;

    /**
     * 栏目下频道或子栏目的列表
     */
    private List<ChildNode> children;

    public int getScreenPartId() {
        return screenPartId;
    }

    public void setScreenPartId(int screenPartId) {
        this.screenPartId = screenPartId;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getLayoutStyle() {
        return layoutStyle;
    }

    public void setLayoutStyle(int layoutStyle) {
        this.layoutStyle = layoutStyle;
    }

    public ColumnItem getDetail() {
        return detail;
    }

    public void setDetail(ColumnItem detail) {
        this.detail = detail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ChildNode> getChildren() {
        return children;
    }

    public void setChildren(List<ChildNode> children) {
        this.children = children;
    }
}
