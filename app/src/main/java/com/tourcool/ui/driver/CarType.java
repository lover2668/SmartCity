package com.tourcool.ui.driver;

import com.contrarywind.interfaces.IPickerViewData;

/**
 * @author :JenkinsZhou
 * @description : 车辆类型
 * @company :途酷科技
 * @date 2020年11月27日15:50
 * @Email: 971613168@qq.com
 */
public class CarType  implements IPickerViewData {
    private String type;
    private String typeDesc;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    @Override
    public String getPickerViewText() {
        return typeDesc;
    }

    public CarType(String type, String typeDesc) {
        this.type = type;
        this.typeDesc = typeDesc;
    }
}
