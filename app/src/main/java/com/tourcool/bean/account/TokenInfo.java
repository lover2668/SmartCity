package com.tourcool.bean.account;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年10月25日17:30
 * @Email: 971613168@qq.com
 */
public class TokenInfo {

    /**
     * access_token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NzE5OTU3NDAsInVzZXJfbmFtZSI6IjE4MjU2MDcwNTYzQEFQUCIsImF1dGhvcml0aWVzIjpbIkFQUCJdLCJqdGkiOiI2Y2E2ZmRiMS1kNGQzLTRhNWQtOWVlOS0xZmI0ZTk4MjZlNTIiLCJjbGllbnRfaWQiOiJzbWFydGp3dGNsaWVudCIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdfQ.CQnwKVObaIB7UYcLWVgV31PTuhyEPcYKxe0_nTVXdK8
     * token_type : Bearer
     * refresh_token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiIxODI1NjA3MDU2M0BBUFAiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiYXRpIjoiNmNhNmZkYjEtZDRkMy00YTVkLTllZTktMWZiNGU5ODI2ZTUyIiwiZXhwIjoxNTcxOTk5MzI1LCJhdXRob3JpdGllcyI6WyJBUFAiXSwianRpIjoiOGJkMTZmNGYtY2E0MC00YjI5LWIwNjgtMmY3NzYxYTFjNDBmIiwiY2xpZW50X2lkIjoic21hcnRqd3RjbGllbnQifQ.uVBd6UVccdid6gPvo2Cn0-Y8lki0ioKPLcPHkGlzem4
     * expires_in : 9
     */
    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private boolean hasPassword;

    public boolean isHasPassword() {
        return hasPassword;
    }

    public void setHasPassword(boolean hasPassword) {
        this.hasPassword = hasPassword;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }
}
