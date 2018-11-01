package me.personal.integration.gitlab.entities;

/**
 * Created by zhongyi on 2018/10/25.
 */
public class Gitlab {
    public final String ACCESS_TOKEN;

    public String getACCESS_TOKEN() {
        return ACCESS_TOKEN;
    }
    public Gitlab(String accessToken){
        this.ACCESS_TOKEN = accessToken;
    }
}
