package com.apigateway.security;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(
        prefix = "jwt.aws"
)
public class JwtConfiguration {
    private String userPoolId;
    private String identityPoolId;
    private String jwkUrl;
    private String region;
    private String groupNameField;
    private String userNameField;
    private int connectionTimeout = 2000;
    private int readTimeout = 2000;
    private String httpHeader = "Authorization";

    public JwtConfiguration() {
    	userNameField = "username";
    	groupNameField = "cognito:groups";
    }
    

    public String getJwkUrl() {
        return this.jwkUrl != null && !this.jwkUrl.isEmpty() ? this.jwkUrl : String.format("https://cognito-idp.%s.amazonaws.com/%s/.well-known/jwks.json", this.region, this.userPoolId);
    }

    public String getCognitoIdentityPoolUrl() {
        return String.format("https://cognito-idp.%s.amazonaws.com/%s", this.region, this.userPoolId);
    }

    public String getUserPoolId() {
        return userPoolId;
    }

    public void setUserPoolId(String userPoolId) {
        this.userPoolId = userPoolId;
    }

    public String getIdentityPoolId() {
        return identityPoolId;
    }

    public void setIdentityPoolId(String identityPoolId) {
        this.identityPoolId = identityPoolId;
    }

    public void setJwkUrl(String jwkUrl) {
        this.jwkUrl = jwkUrl;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getUserNameField() {
        return userNameField;
    }

    public void setUserNameField(String userNameField) {
        this.userNameField = userNameField;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public String getGroupNameField() {
		return groupNameField;
	}


	public void setGroupNameField(String groupNameField) {
		this.groupNameField = groupNameField;
	}


	public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public String getHttpHeader() {
        return httpHeader;
    }

    public void setHttpHeader(String httpHeader) {
        this.httpHeader = httpHeader;
    }
}
