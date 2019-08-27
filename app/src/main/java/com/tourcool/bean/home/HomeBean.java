package com.tourcool.bean.home;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月26日14:18
 * @Email: 971613168@qq.com
 */
public class HomeBean {

    /**
     * WEATHER(0, "天气样式"),
     * HORIZONTAL_BANNER(1, "水平滚动Banner样式"),
     * VERTICAL_BANNER(2, "垂直滚动Banner样式"),
     * IMAGE_TEXT_LIST(3, "图（上）文（下）列表样式"),
     * IMAGE(4, "图片样式"),
     * CONTAINS_SUBLISTS(5, "包含子列表样式");
     */
    /**
     * data : {"childList":[{"clickLink":"https://baijiahao.baidu.com/s?id=1642386184407853309","clickType":"URL","icon":"http://medium.tklvyou.cn/assets/img/avatar.png","id":0,"subTitle":"","title":"巴士管家 0"},{"clickLink":"https://baijiahao.baidu.com/s?id=1642386184407853309","clickType":"URL","icon":"http://medium.tklvyou.cn/assets/img/avatar.png","id":0,"subTitle":"","title":"巴士管家 1"},{"clickLink":"https://baijiahao.baidu.com/s?id=1642386184407853309","clickType":"URL","icon":"http://medium.tklvyou.cn/assets/img/avatar.png","id":0,"subTitle":"","title":"巴士管家 2"},{"clickLink":"https://baijiahao.baidu.com/s?id=1642386184407853309","clickType":"URL","icon":"http://medium.tklvyou.cn/assets/img/avatar.png","id":0,"subTitle":"","title":"巴士管家 3"},{"clickLink":"https://baijiahao.baidu.com/s?id=1642386184407853309","clickType":"URL","icon":"http://medium.tklvyou.cn/assets/img/avatar.png","id":0,"subTitle":"","title":"巴士管家 4"}],"clickLink":"","clickType":"","icon":"","title":""}
     * type : IMAGE_TEXT_LIST
     * weather :
     */
    private HomeChildBean data;
    private String type;
    private Weather weather;


    public HomeChildBean getData() {
        return data;
    }

    public void setData(HomeChildBean data) {
        this.data = data;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
