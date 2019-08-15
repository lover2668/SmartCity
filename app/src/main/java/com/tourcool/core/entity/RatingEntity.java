package com.tourcool.core.entity;

import java.io.Serializable;

/**
 * @Author: JenkinsZhou on 2018/11/19 14:17
 * @E-Mail: 971613168@qq.com
 * @Function: 电影评价星级
 * @Description:
 */
public class RatingEntity implements Serializable {
    /**
     * max : 10
     * average : 9.6
     * stars : 50
     * min : 0
     */

    public int max;
    public double average;
    public String stars;
    public int min;
}
