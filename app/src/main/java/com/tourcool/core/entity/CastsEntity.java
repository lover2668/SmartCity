package com.tourcool.core.entity;

import java.io.Serializable;

/**
 * @Author: JenkinsZhou on 2018/11/19 14:15
 * @E-Mail: 971613168@qq.com
 * @Function: 电影演员实体
 * @Description:
 */
public class CastsEntity implements Serializable {
    /**
     * alt : https://movie.douban.com/celebrity/1317646/
     * avatars : {"small":"https://img3.doubanio.com/img/celebrity/small/40783.jpg","large":"https://img3.doubanio.com/img/celebrity/large/40783.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/40783.jpg"}
     * name : 戴恩·德哈恩
     * id : 1317646
     */

    public String alt;
    public AvatarsEntity avatars;
    public String name;
    public String id;
}
