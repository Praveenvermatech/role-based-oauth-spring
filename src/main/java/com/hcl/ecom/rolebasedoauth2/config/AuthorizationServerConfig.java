package com.hcl.ecom.rolebasedoauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.hcl.ecom.rolebasedoauth2.util.AppConstatnt;


@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey("as466gf");
		return converter;
	}

	@Bean
	public TokenStore tokenStore() {
		TokenStore token = new JwtTokenStore(accessTokenConverter());
		return token;
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {

		configurer
				.inMemory()
				.withClient(AppConstatnt.CLIENT_ID)
				.secret(passwordEncoder.encode(AppConstatnt.CLIENT_SECRET))
				.authorizedGrantTypes(AppConstatnt.GRANT_TYPE_PASSWORD, AppConstatnt.AUTHORIZATION_CODE, AppConstatnt.REFRESH_TOKEN, AppConstatnt.IMPLICIT )
				.scopes(AppConstatnt.SCOPE_READ, AppConstatnt.SCOPE_WRITE, AppConstatnt.TRUST);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {

        endpoints
				.authenticationManager(authenticationManager)
				.accessTokenConverter(accessTokenConverter());
	}


}