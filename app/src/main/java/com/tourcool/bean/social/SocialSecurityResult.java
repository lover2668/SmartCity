package com.tourcool.bean.social;

import java.util.List;

public class SocialSecurityResult {
    /**
     * 0 成功
     * 1 失败
     */
//    @ApiModelProperty("0 成功    1 失败 ")
    private String result;

    /**
     * 历史记录
     */
//    @ApiModelProperty("历史记录")
    private List<SocialDetail> data;


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<SocialDetail> getData() {
        return data;
    }

    public void setData(List<SocialDetail> data) {
        this.data = data;
    }


}
