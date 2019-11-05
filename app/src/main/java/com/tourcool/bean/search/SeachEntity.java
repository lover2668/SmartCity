package com.tourcool.bean.search;

import com.tourcool.bean.screen.Channel;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年11月05日10:53
 * @Email: 971613168@qq.com
 */
public class SeachEntity {


    /**
     * channels : [{"id":37,"icon":"/smart_file/img/1/1571899491772.png","circleIcon":"/smart_file/img/1/1571899494551.png","title":"巴士管家","description":"网上查询 在线购票","jumpWay":1,"link":"https://www.baidu.com","authLevel":0,"channelType":2,"createTime":1571884712129}]
     * articles : null
     */

    private Object articles;
    private List<Channel> channels;

    public Object getArticles() {
        return articles;
    }

    public void setArticles(Object articles) {
        this.articles = articles;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }


}
