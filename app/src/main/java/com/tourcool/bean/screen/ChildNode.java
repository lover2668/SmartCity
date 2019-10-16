package com.tourcool.bean.screen;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年10月16日16:06
 * @Email: 971613168@qq.com
 */
public class ChildNode {
    /**
     * detail : {"id":3,"icon":"/smart_file/img/1/1570591455697.png","title":"标题11","description":"描述","jumpWay":2,"link":"xxx","authLevel":0,"channelType":0,"createTime":1570524243888}
     * visible : true
     * idx : 0
     * type : SUB_CHANNEL
     * children : null
     */

    private Channel detail;
    private boolean visible;
    /**
     * 序号
     */
    private int idx;
    private String type;
    private Object children;

    public Channel getDetail() {
        return detail;
    }

    public void setDetail(Channel detail) {
        this.detail = detail;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
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

    public Object getChildren() {
        return children;
    }

    public void setChildren(Object children) {
        this.children = children;
    }
}
