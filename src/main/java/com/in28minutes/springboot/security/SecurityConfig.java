package com.in28minutes.springboot.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * auth // enable in memory based authentication with a user named //
	 * &quot;user&quot; and &quot;admin&quot;
	 * .inMemoryAuthentication().withUser(&quot;user&quot;).password(&quot;password&quot;).roles(&quot;USER&quot;).and()
	 * .withUser(&quot;admin&quot;).password(&quot;password&quot;).roles(&quot;USER&quot;,
	 * &quot;ADMIN&quot;); }
	 */
	// Autentication user - roles
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("user1").password("password1").roles("USER").and().withUser("admin1")
//				.password("password1").roles("USER", "ADMIN");
		auth.inMemoryAuthentication()
				.passwordEncoder(org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance())
				.withUser("user1").password("secret1").roles("USER").and().withUser("admin1").password("secret1")
				.roles("ADMIN");

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		logger.debug("Using default configure(HttpSecurity). If subclassed this will potentially override subclass configure(HttpSecurity).");

		http.httpBasic()
			.and().authorizeRequests()
			.antMatchers("/surveys/**").hasRole("USER")
				.antMatchers("/users/**").hasRole("USER")
				.antMatchers("/**").hasRole("ADMIN")
			.and().csrf().disable()
			.headers().frameOptions().disable();
		// .anyRequest().authenticated().and().formLogin()
		;
	}

}
