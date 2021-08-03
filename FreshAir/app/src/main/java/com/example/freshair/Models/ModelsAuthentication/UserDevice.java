package com.example.freshair.Models.ModelsAuthentication;

public class UserDevice {

    private String userToken;

    private String internetNameProvider;

    private String passwordNameProvider;

    public UserDevice(String userToken, String internetNameProvider, String passwordNameProvider) {
        this.userToken = userToken;
        this.internetNameProvider = internetNameProvider;
        this.passwordNameProvider = passwordNameProvider;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getInternetNameProvider() {
        return internetNameProvider;
    }

    public void setInternetNameProvider(String internetNameProvider) {
        this.internetNameProvider = internetNameProvider;
    }

    public String getPasswordNameProvider() {
        return passwordNameProvider;
    }

    public void setPasswordNameProvider(String passwordNameProvider) {
        this.passwordNameProvider = passwordNameProvider;
    }
}
