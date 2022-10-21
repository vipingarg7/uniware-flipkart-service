package com.uniware.integrations.client.dto.api.responseDto;

import com.google.gson.annotations.SerializedName;
import com.uniware.integrations.client.dto.BaseResponse;

/**
 * Created by vipin on 26/05/22.
 */
public class AuthTokenResponse extends BaseResponse {

    @SerializedName("accessToken")
    private String accessToken;
    @SerializedName("tokenType")
    private String tokenType;
    @SerializedName("refreshToken")
    private String refreshToken;
    @SerializedName("expiresIn")
    private Long expiresIn;
    @SerializedName("scope")
    private String scope;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override public String toString() {
        return "AuthTokenResponse{" + "accessToken='" + accessToken + '\'' + ", tokenType='" + tokenType + '\''
                + ", refreshToken='" + refreshToken + '\'' + ", expiresIn=" + expiresIn + ", scope='" + scope + '\''
                + '}';
    }

}
