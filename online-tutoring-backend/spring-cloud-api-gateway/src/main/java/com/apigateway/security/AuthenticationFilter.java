package com.apigateway.security;

import java.util.ArrayList;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.apigateway.RouterValidator;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;

import reactor.core.publisher.Mono;

@RefreshScope
@Component
public class AuthenticationFilter implements GlobalFilter {

	@Autowired
	private RouterValidator routerValidator;
	@Autowired
	private ConfigurableJWTProcessor configurableJWTProcessor;
	@Autowired
	private JwtConfiguration jwtConfiguration;
	private Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		String userName = null;
		String groupName = null;
		logger.info("Inside filter!!!!!!!!!!!");
		try {
			if (routerValidator.isSecured.test(request)) {
				if (this.isAuthMissing(request)) {
					logger.info("authorization is missing");
					return this.onError(exchange, "Authorization header is missing in request",
							HttpStatus.UNAUTHORIZED);
				}
				logger.info("Inside test of routing!!!!!!!!!!!");
				final String token = this.getAuthHeader(request);
				logger.info("token=={}", new Object[] { token });
				if (token == null)
					return this.onError(exchange, "Authorization header is missing in request",
							HttpStatus.UNAUTHORIZED);
				if (token != null) {
					JWTClaimsSet claims = this.configurableJWTProcessor.process(this.getBearerToken(token), null);
					validateIssuer(claims);
					verifyIfIdToken(claims);
					userName = getUserNameFrom(claims);
					groupName = getGroupNameFrom(claims);
					if (userName != null) {

						logger.info("username ={}", new Object[] { userName });
						this.populateRequestWithHeaders(exchange, userName,groupName,token);
					}
				}

				if (userName == null)
					return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);

			}
		} catch (Exception e) {
			e.printStackTrace();

			return this.onError(exchange, e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
		return chain.filter(exchange);
	}

	/* PRIVATE */

	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
		ServerHttpResponse response = exchange.getResponse();
		logger.info(response.toString());
		response.setStatusCode(httpStatus);
		return response.setComplete();
	}

	private String getAuthHeader(ServerHttpRequest request) {
		return request.getHeaders().getOrEmpty("Authorization").get(0);
	}

	private boolean isAuthMissing(ServerHttpRequest request) {
		return !request.getHeaders().containsKey("Authorization");
	}

	private void populateRequestWithHeaders(ServerWebExchange exchange, String userName,String groupName,String accessToken) {

		exchange.getRequest().mutate().header("userName", String.valueOf(userName)).header("groupName", groupName).header("accessToken", accessToken).build();
	}

	private String getUserNameFrom(JWTClaimsSet claims) {
		return claims.getClaims().get(this.jwtConfiguration.getUserNameField()).toString();
	}

	private String getGroupNameFrom(JWTClaimsSet claims) {
		Object groupNameObj = claims.getClaims().get(this.jwtConfiguration.getGroupNameField());
		if (groupNameObj != null && groupNameObj instanceof List
				&& ((List) groupNameObj).stream().noneMatch((o -> !(o instanceof String)))) {
			List<String> groupList = (List<String>)groupNameObj;
			return String.join(",", groupList);
			
		}
			
		return null;
	}

	private void verifyIfIdToken(JWTClaimsSet claims) throws Exception {
		if (!claims.getIssuer().equals(this.jwtConfiguration.getCognitoIdentityPoolUrl())) {
			throw new Exception("JWT Token is not an ID Token");
		}
	}

	private void validateIssuer(JWTClaimsSet claims) throws Exception {
		if (!claims.getIssuer().equals(this.jwtConfiguration.getCognitoIdentityPoolUrl())) {
			throw new Exception(String.format("Issuer %s does not match cognito idp %s", claims.getIssuer(),
					this.jwtConfiguration.getCognitoIdentityPoolUrl()));
		}
	}

	private String getBearerToken(String token) {
		return token.startsWith("Bearer ") ? token.substring("Bearer ".length()) : token;
	}
}
