package com.nongsandd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
@EnableOAuth2Sso
public class NongsanddApplication extends WebSecurityConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(NongsanddApplication.class, args);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();

		http.authorizeRequests()
				.antMatchers("/","/gia/**")
				.permitAll()
				.anyRequest()
				.authenticated();

		http.authorizeRequests()
				.antMatchers("/admin/**").hasAnyRole("ADMIN")
				.antMatchers("/NguoiDung/**").hasAnyRole("USER")
				.antMatchers("/NhaBuon/**").hasAnyRole("TRADER").anyRequest().authenticated()
				.and().logout().logoutSuccessUrl("/").permitAll();
	}
}
