package com.tourcool.bean;

import com.frame.library.core.widget.linkage.bean.BaseGroupedItem;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月22日11:11
 * @Email: 971613168@qq.com
 */
public class ServiceGroupItem extends BaseGroupedItem {

    public ServiceGroupItem(BaseGroupedItem.ItemInfo info) {
        super(info);
    }

    public static class ItemInfo extends BaseGroupedItem.ItemInfo {
        private String content;
        private String imgUrl;
        private List<MatrixBean> matrixBeanList;


        public ItemInfo(String title, String group, List<MatrixBean> matrixBeanList) {
            super(title, group);
            this.matrixBeanList = matrixBeanList;
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

        public List<MatrixBean> getMatrixBeanList() {
            return matrixBeanList;
        }

        public void setMatrixBeanList(List<MatrixBean> matrixBeanList) {
            this.matrixBeanList = matrixBeanList;
        }
    }

}
