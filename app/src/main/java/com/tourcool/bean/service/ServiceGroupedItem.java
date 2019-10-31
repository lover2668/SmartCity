package com.tourcool.bean.service;

import com.frame.library.core.widget.linkage.bean.BaseGroupedItem;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年10月29日15:26
 * @Email: 971613168@qq.com
 */
public class ServiceGroupedItem extends BaseGroupedItem<ServiceGroupedItem.ItemInfo> {

    public ServiceGroupedItem(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public ServiceGroupedItem(ServiceGroupedItem.ItemInfo item) {
        super(item);
    }

    public static class ItemInfo extends BaseGroupedItem.ItemInfo {
        private String content;
        private String imgUrl;
        private String desc;

        public ItemInfo(String title, String group, String content) {
            super(title, group);
            this.content = content;
        }

        public ItemInfo(String title, String group, String content, String imgUrl) {
            this(title, group, content);
            this.imgUrl = imgUrl;
        }

        public ItemInfo(String title, String group, String content, String imgUrl, String cost) {
            this(title, group, content, imgUrl);
            this.desc = cost;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getCost() {
            return desc;
        }

        public void setCost(String cost) {
            this.desc = cost;
        }
    }
}
