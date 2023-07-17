package com.antonyrain.server.config;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig implements WebMvcConfigurer {
	private final RsaProperties rsaKeys;
	
	public WebSecurityConfig(RsaProperties rsaKeys) {
		this.rsaKeys = rsaKeys;
	}

	@ConfigurationProperties(prefix ="rsa")
	public record RsaProperties(RSAPrivateKey privateKey, RSAPublicKey publicKey) {
	}

	@Bean
	JwtEncoder jwtEncoder() {
		JWK jwk = new RSAKey
			.Builder(rsaKeys.publicKey())
			.privateKey(rsaKeys.privateKey())
			.build();
		JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
			return new NimbusJwtEncoder(jwkSource);
	}
	
	@Bean
	JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
	}

	@Bean
  	public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
  }

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

    @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable());

		http.sessionManagement((session) -> session
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		http.exceptionHandling((exceptions) -> exceptions
			.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
			.accessDeniedHandler(new BearerTokenAccessDeniedHandler()));

		http.authorizeHttpRequests((requests) -> requests
			.requestMatchers("/**")
			.permitAll());
		
		http.logout((logout) -> logout
			.permitAll());

		return http.build();
	}

	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("*");
    }
}