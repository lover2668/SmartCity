package com.tourcool.bean;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年10月15日16:32
 * @Email: 971613168@qq.com
 */
public class ScreenInfo {


    /**
     * screenId : 1
     * name : 首页
     * idx : 0
     * type : SCREEN
     * children : [{"screenPartId":142,"idx":0,"layoutStyle":3,"detail":{"id":10,"name":"错误","color":null,"icon":"/smart_file/img/1/1570610485776.png","canCustomization":false,"jumpWay":2,"link":null,"columnType":2,"createTime":1570610487997},"children":[{"detail":{"id":3,"icon":"/smart_file/img/1/1570591455697.png","title":"标题11","description":"描述","jumpWay":2,"link":"xxx","authLevel":0,"channelType":0,"createTime":1570524243888},"visible":true,"idx":0,"type":"SUB_CHANNEL","children":null},{"detail":{"id":9,"name":"角色四","color":null,"icon":"/smart_file/img/1/1570610473123.png","canCustomization":false,"jumpWay":2,"link":null,"columnType":3,"createTime":1570610474423},"visible":true,"idx":1,"type":"SUB_COLUMN","children":[{"detail":{"id":7,"icon":"/smart_file/img/1/1571125466750.png","title":"123","description":"测试新增","jumpWay":1,"link":"https://www.baidu.com","authLevel":1,"channelType":2,"createTime":1570590899582},"visible":true,"idx":1,"type":"SUB_CHANNEL","children":null}]},{"detail":{"id":7,"icon":"/smart_file/img/1/1571125466750.png","title":"123","description":"测试新增","jumpWay":1,"link":"https://www.baidu.com","authLevel":1,"channelType":2,"createTime":1570590899582},"visible":true,"idx":1,"type":"SUB_CHANNEL","children":null}],"type":"ScreenPart"}]
     */

    private int screenId;
    private String name;
    private int idx;
    private String type;
    private List<ChildrenBeanX> children;

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

    public List<ChildrenBeanX> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenBeanX> children) {
        this.children = children;
    }

    public static class ChildrenBeanX {
        /**
         * screenPartId : 142
         * idx : 0
         * layoutStyle : 3
         * detail : {"id":10,"name":"错误","color":null,"icon":"/smart_file/img/1/1570610485776.png","canCustomization":false,"jumpWay":2,"link":null,"columnType":2,"createTime":1570610487997}
         * children : [{"detail":{"id":3,"icon":"/smart_file/img/1/1570591455697.png","title":"标题11","description":"描述","jumpWay":2,"link":"xxx","authLevel":0,"channelType":0,"createTime":1570524243888},"visible":true,"idx":0,"type":"SUB_CHANNEL","children":null},{"detail":{"id":9,"name":"角色四","color":null,"icon":"/smart_file/img/1/1570610473123.png","canCustomization":false,"jumpWay":2,"link":null,"columnType":3,"createTime":1570610474423},"visible":true,"idx":1,"type":"SUB_COLUMN","children":[{"detail":{"id":7,"icon":"/smart_file/img/1/1571125466750.png","title":"123","description":"测试新增","jumpWay":1,"link":"https://www.baidu.com","authLevel":1,"channelType":2,"createTime":1570590899582},"visible":true,"idx":1,"type":"SUB_CHANNEL","children":null}]},{"detail":{"id":7,"icon":"/smart_file/img/1/1571125466750.png","title":"123","description":"测试新增","jumpWay":1,"link":"https://www.baidu.com","authLevel":1,"channelType":2,"createTime":1570590899582},"visible":true,"idx":1,"type":"SUB_CHANNEL","children":null}]
         * type : ScreenPart
         */

        private int screenPartId;
        private int idx;
        private int layoutStyle;
        private DetailBean detail;
        private String type;
        private List<ChildrenBean> children;

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

        public DetailBean getDetail() {
            return detail;
        }

        public void setDetail(DetailBean detail) {
            this.detail = detail;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<ChildrenBean> getChildren() {
            return children;
        }

        public void setChildren(List<ChildrenBean> children) {
            this.children = children;
        }

        public static class DetailBean {
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

            private int id;
            private String name;
            private Object color;
            private String icon;
            private boolean canCustomization;
            private int jumpWay;
            private Object link;
            private int columnType;
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

            public Object getLink() {
                return link;
            }

            public void setLink(Object link) {
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

        public static class ChildrenBean {
            /**
             * detail : {"id":3,"icon":"/smart_file/img/1/1570591455697.png","title":"标题11","description":"描述","jumpWay":2,"link":"xxx","authLevel":0,"channelType":0,"createTime":1570524243888}
             * visible : true
             * idx : 0
             * type : SUB_CHANNEL
             * children : null
             */

            private DetailBeanX detail;
            private boolean visible;
            private int idx;
            private String type;
            private Object children;

            public DetailBeanX getDetail() {
                return detail;
            }

            public void setDetail(DetailBeanX detail) {
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

            public static class DetailBeanX {
                /**
                 * id : 3
                 * icon : /smart_file/img/1/1570591455697.png
                 * title : 标题11
                 * description : 描述
                 * jumpWay : 2
                 * link : xxx
                 * authLevel : 0
                 * channelType : 0
                 * createTime : 1570524243888
                 */

                private int id;
                private String icon;
                private String title;
                private String description;
                private int jumpWay;
                private String link;
                private int authLevel;
                private int channelType;
                private long createTime;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
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

                public int getAuthLevel() {
                    return authLevel;
                }

                public void setAuthLevel(int authLevel) {
                    this.authLevel = authLevel;
                }

                public int getChannelType() {
                    return channelType;
                }

                public void setChannelType(int channelType) {
                    this.channelType = channelType;
                }

                public long getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(long createTime) {
                    this.createTime = createTime;
                }
            }
        }
    }
}
