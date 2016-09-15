package com.adamd.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring security configuration built over the defaults.
 * <p/>
 * Responsibilities:
 * <ul>
 * <li>Secure routes</li>
 * <li>Manage CSRF protection</li>
 * <li>Configure security tokens if any</li>
 * </ul>
 * <p/>
 * We opt to use stateless security to allow the use of auth-tokens. This is the
 * standard for rest calls.
 * <p/>
 * <b>NB:</b> Some javascript (AngularJs) need read the CSR cookie if ever we
 * require CSR. In this case, enable a csr repository with HttlOnly set to false
 * 
 * @author adamd, darvidem
 * @version 2
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // @formatter:off
	        http
	            .authorizeRequests()
	                // Lock API calls and secure webpages
	                .antMatchers("/api/**").authenticated()
	                .antMatchers("/secure/**").authenticated()
	                // Allow home page and public static resources: HTML, CSS, IMAGES
	                .anyRequest().permitAll()
	            .and()
	                .csrf() // Enable this at a later stage. 
	                    .disable()
	            .sessionManagement()
	                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	            .and()
	                .logout()
	                     .logoutUrl("/logout")
	                     .logoutSuccessUrl("/");
	     // @formatter:on
    }

    @Override
    public void configure(final AuthenticationManagerBuilder auth) throws Exception {
        // @formatter:off		
		auth.inMemoryAuthentication()
		   		.withUser("admin").password("admin").roles("ADMIN")
				.and().withUser("user").password("user").roles("USER");
        // @formatter:on
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }
}
