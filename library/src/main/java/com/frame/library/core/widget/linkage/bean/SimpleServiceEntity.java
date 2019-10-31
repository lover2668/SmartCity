package com.frame.library.core.widget.linkage.bean;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年10月30日16:42
 * @Email: 971613168@qq.com
 */
public class SimpleServiceEntity {
    private String groupName;
    private List<Integer> childItemIdList;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Integer> getChildItemIdList() {
        return childItemIdList;
    }

    public void setChildItemIdList(List<Integer> childItemIdList) {
        this.childItemIdList = childItemIdList;
    }
}
