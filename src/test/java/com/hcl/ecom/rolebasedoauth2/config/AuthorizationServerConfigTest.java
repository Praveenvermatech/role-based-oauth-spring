package com.hcl.ecom.rolebasedoauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.test.context.ActiveProfiles;

import com.hcl.ecom.rolebasedoauth2.util.AppConstatnt;


@Configuration
@EnableAuthorizationServer
@ActiveProfiles("test")
public class AuthorizationServerConfigTest extends AuthorizationServerConfigurerAdapter {

    private AuthenticationManager authenticationManager;
    
    @Autowired
	private PasswordEncoder passwordEncoder;


    @Autowired
    public AuthorizationServerConfigTest(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.checkTokenAccess("permitAll()");
        oauthServer.allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        super.configure(clients);
        clients.inMemory()
                .withClient(AppConstatnt.CLIENT_ID)
                .secret(passwordEncoder.encode(AppConstatnt.CLIENT_SECRET))
                .authorizedGrantTypes(AppConstatnt.GRANT_TYPE_PASSWORD);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        super.configure(endpoints);
        endpoints.authenticationManager(this.authenticationManager);
    }
}
