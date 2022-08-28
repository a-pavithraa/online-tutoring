package com.apigateway.security;

import org.springframework.stereotype.Component;

@Component
public class JwtIdTokenCredentialsHolder {
    private String idToken;

    public JwtIdTokenCredentialsHolder() {
    }

    public String getIdToken() {
        return this.idToken;
    }

    public JwtIdTokenCredentialsHolder setIdToken(String idToken) {
        this.idToken = idToken;
        return this;
    }
}