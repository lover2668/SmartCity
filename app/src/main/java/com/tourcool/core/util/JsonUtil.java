package com.tourcool.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月28日10:01
 * @Email: 971613168@qq.com
 */
public class JsonUtil {


    ////////////// List集合和JSON相互转换工具类//////////////////////////

    /**
     * 7.通过JSON.toJSONString把List<T>转换为json
     *
     * @param obj
     * @return
     */
    public static <T> String getListToJson(List<T> obj) {
        return JSON.toJSONString(obj);
    }

    /**
     * 8.通过JSONArray.parseArray把json转换为List<T>
     *
     * @param jsonStr
     * @param model
     * @return
     */
    public static <T> List<T> getStringToList(String jsonStr, Class<T> model) {
        return JSONArray.parseArray(jsonStr, model);
    }

    ////////////// 对象和JSON相互转换工具类/////////////////////////

    /**
     * 9.通过JSONObject.toJSONString把对象转换为String
     *
     * @param model
     * @return
     */
    public static <T> String getObjectToJson(T model) {
        return JSONObject.toJSONString(model);
        // User输出: {"cId":"100","pwd":"123456","uName":"lmx"}
        // Map输出:  {"uName":"llmmxx","pwd":"123","cId":"300"}
    }


    /**
     * 10.通过JSONObject.parseObject把String转换为对象
     *
     * @param jsonStr
     * @param model
     * @return
     */
    public static <T> T getStringToObject(String jsonStr, Class<T> model) {
        return JSONObject.parseObject(jsonStr, model);
    }


}
