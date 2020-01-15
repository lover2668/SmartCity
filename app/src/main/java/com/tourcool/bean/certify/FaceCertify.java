package com.tourcool.bean.certify;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年01月14日15:57
 * @Email: 971613168@qq.com
 */
public class FaceCertify {


    /**
     * certifyId : 43d67bdb9dca9289ee807070b03ec058
     * authenticationUrl : https://openapi.alipay.com/gateway.do?alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2018100361597260&biz_content=%7B%22certify_id%22%3A%2243d67bdb9dca9289ee807070b03ec058%22%7D&charset=UTF-8&format=json&method=alipay.user.certify.open.certify&sign=Q%2BPiCbiTTIgCmGFG4QKNNP1FnZq%2FpWP9O7DVAcYK3t7K%2BSYr74N6ZeV3w30dIHSFXyZFkibXDCST02jXmgujH1KiDaFZpCI0Hz531CWQONa3%2FtiLc%2FJzNvI%2FAEmHxkbulbQR9xeA3SI4AYRBeHPeoFCVRneVZLK7sY1AVWxuzC965pwv%2FiM7shws%2FlKm%2BQNhLlgBky3TRbWRNbEIx2Oh9hD3OyqRb93JpkXmsaobvU4vBQo0566L2%2FOjlmmEFfNdizxGBNcucske2c2VVz6H5KBCavrBC37fsaw8bVSsgtchMZAY0GuAJyzoFA%2FetzaH4yVUrMO2gv5Ycji7XxaOkw%3D%3D&sign_type=RSA2&timestamp=2020-01-14+15%3A57%3A02&version=1.0
     */

    private String certifyId;
    private String authenticationUrl;
    private String outerOrderNo;

    public String getOuterOrderNo() {
        return outerOrderNo;
    }

    public void setOuterOrderNo(String outerOrderNo) {
        this.outerOrderNo = outerOrderNo;
    }

    public String getCertifyId() {
        return certifyId;
    }

    public void setCertifyId(String certifyId) {
        this.certifyId = certifyId;
    }

    public String getAuthenticationUrl() {
        return authenticationUrl;
    }

    public void setAuthenticationUrl(String authenticationUrl) {
        this.authenticationUrl = authenticationUrl;
    }
}
