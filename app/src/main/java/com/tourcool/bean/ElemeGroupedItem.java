package com.tourcool.bean;

/*
 * Copyright (c) 2018-2019. KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import com.frame.library.core.widget.linkage.bean.BaseGroupedItem;

/**
 * Create by KunMinX at 19/4/27
 */
public class ElemeGroupedItem extends BaseGroupedItem<ElemeGroupedItem.ItemInfo> {

    public ElemeGroupedItem(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public ElemeGroupedItem(ItemInfo item) {
        super(item);
    }

    public static class ItemInfo extends BaseGroupedItem.ItemInfo {
        private String content;
        private String imgUrl;
        private String link;
        private String type;
        private Object children;
        private String columnName;
        private String parentsName;
        private String richContent;
        private int jumpWay;

        public int getJumpWay() {
            return jumpWay;
        }

        public void setJumpWay(int jumpWay) {
            this.jumpWay = jumpWay;
        }

        public String getRichContent() {
            return richContent;
        }

        public void setRichContent(String richContent) {
            this.richContent = richContent;
        }

        public String getParentsName() {
            return parentsName;
        }

        public void setParentsName(String parentsName) {
            this.parentsName = parentsName;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
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

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public ItemInfo(String title, String group, String content,String richContent) {
            super(title, group,richContent);
            this.content = content;
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
    }
}
