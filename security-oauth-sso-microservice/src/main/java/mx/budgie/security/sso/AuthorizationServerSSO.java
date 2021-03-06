/**
 * 
 */
package mx.budgie.security.sso;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;

import mx.budgie.security.sso.constants.SecurityConstants;

/**
 * Configuracion para la creacion de tokens de autenticacion :
 * AuthorizationEndpoint, TokenEndpoint, CheckTokenEndpoint
 * 
 * @company Budgie Software
 * @author bruce rip
 * @date Jun 15, 2017
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerSSO extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthorizationEndpoint authorizationEndpoint;
	
	@Autowired
	@Qualifier(SecurityConstants.SERVICE_CUSTOM_CLIENT_DETAIL)
	private ClientDetailsService customClientDetailService;

	@Autowired
	@Qualifier(SecurityConstants.SERVICE_CUSTOM_TOKEN_STORE)
	private TokenStore customTokenStore;

	/**
	 * Para buscar usuarios finales (NO CLIENTES). Es una abstraccion para autenticacion de usuarios
	 * https://docs.spring.io/spring-security-oauth2-boot/docs/current-SNAPSHOT/reference/htmlsingle/#oauth2-boot-authorization-server-minimal
	 */
	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(customClientDetailService);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.checkTokenAccess("isAuthenticated()") // Esto regresara tru cuando el usuario no es ANONYMOUS (https://docs.spring.io/spring-security/site/docs/3.0.x/reference/el-access.html)
					.tokenKeyAccess("permitAll()"); 
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager);	
	}

	@PostConstruct
	public void init() {
		authorizationEndpoint.setUserApprovalPage("forward:/oauth/custom_confirm_access");
		this.authorizationEndpoint.setErrorPage("forward:/oauth/custom_error");
	}
}
